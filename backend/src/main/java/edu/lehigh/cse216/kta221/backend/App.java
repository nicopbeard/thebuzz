package edu.lehigh.cse216.kta221.backend;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import spark.Spark;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import java.util.*;
// Import Google's JSON library
import com.google.gson.*;


/**
 * For now, our app creates an HTTP server that can only get and add data.
 */
public class App {

    private final static int TOKEN_ERROR = 1;
    private static final int UPDATE_ERROR = -1;
    private static final String ERROR = "error";
    private static final String OK = "ok";
    private static Map<String, String> userIdToToken = new HashMap<>();

    public static void main(String[] args) {

        Map<String, String> env = System.getenv();
        String ip = "ec2-174-129-220-12.compute-1.amazonaws.com";
        //String ip = "localhost";
        String port = "5432";
        String user = "qnwrtcuewzcdpe";
        String pass = "a8bc2fbf3637a0fcded45cb4a148de56a37dd37cc3c694145d863374f2ee77a0";


        // Get the port on which to listen for requests
        Spark.port(getIntFromEnv("PORT", 4567));
        // NB: Gson is thread-safe.  See
        // https://stackoverflow.com/questions/10380835/is-it-ok-to-use-gson-instance-as-a-static-field-in-a-model-bean-reuse
        final Gson gson = new Gson();

        System.out.println(ip + " " + port + " " + user + " " + pass);
        Database db = Database.getDatabase(ip, port, user, pass);
        if(db == null) { return; }
        System.out.println("Connected to db");

        // Set up the location for serving static files.  If the STATIC_LOCATION
        // environment variable is set, we will serve from it.  Otherwise, serve
        // from "/web"
        String static_location_override = System.getenv("STATIC_LOCATION");
        if (static_location_override == null) {
            Spark.staticFileLocation("/web");
        } else {
            Spark.staticFiles.externalLocation(static_location_override);
        }
        // Set up the location for serving static files
        Spark.staticFileLocation("/web");

        // Set up a route for serving the main page
        Spark.get("/", (req, res) -> {
            res.redirect("/index.html");
            return "";
        });

        //GET route for all messages and associated comments
        Spark.get("/messages", (request, response) -> {
            response.status(200);
            response.type("application/json");

            MessageRequest req = gson.fromJson(request.body(), MessageRequest.class);

            String status = OK;

            if(!validToken(req.userId, req.googleToken)) {
                status = ERROR;
            }

            ArrayList<Database.MessageRow> messages = db.messageAll();
            Hashtable<Integer, ArrayList<Database.Comment>> comments = db.commentAll();
            for(Database.MessageRow msg: messages) {
                if(comments.contains(msg.id)){
                    msg.addComments(comments.get(msg.id));
                }
            }

            return gson.toJson(new StructuredResponse(status, null, status.equals(OK)? messages : null));
        });

        // JSON from the body of the request, turn it into a SimpleRequest
        // object, extract the title and message, insert them, and return the 
        // ID of the newly created row.
        Spark.post("/messages", (request, response) -> {
            response.status(200);
            response.type("application/json");

            MessageRequest req = gson.fromJson(request.body(), MessageRequest.class);

            //Validate token
            if(!validToken(req.userId, req.googleToken)) {
                return gson.toJson(new StructuredMessageResponse(ERROR, TOKEN_ERROR));
            }

            int newId = db.insertMessage(req.senderId, req.text, 0, 0);

            //NB: createEntry checks for null title and message
            if (newId == UPDATE_ERROR) {
                return gson.toJson(new StructuredMessageResponse(ERROR, newId));
            } else {
                return gson.toJson(new StructuredMessageResponse(OK, newId));
            }
        });

        Spark.put("/like", (request, response) -> {
            response.status(200);
            response.type("application/json");

            VoteRequest req = gson.fromJson(request.body(), VoteRequest.class);

            if(!validToken(req.userId, req.sessionToken)) {
                return gson.toJson(new StructuredMessageResponse("error", TOKEN_ERROR));
            }

            int result = db.insertLike(req.userId, req.msgId);
            return result == UPDATE_ERROR? gson.toJson(new StructuredResponse(ERROR, "Element Already Added: ", result))
                    :  gson.toJson(new StructuredResponse(OK, "Created Element: ", result));
        });

        //This route should never get hit anymore?
        //TODO ask
        Spark.put("/newUser", (request, response) ->{
            response.status(200);
            response.type("application/json");

            NewUser nu = gson.fromJson(request.body(), NewUser.class);
            String googleToken = nu.googleToken;


            if(validateGoogleToken(googleToken, db) == null){
                return gson.toJson(new StructuredMessageResponse("error", TOKEN_ERROR));
            }

            return gson.toJson(new StructuredResponse("ok", null, null));
        });


        //TAKES: Json object with fields "googleToken"
        //TODO how do we send them back their userId
         Spark.put("/login", (request, response) ->{
            response.status(200);
            response.type("application/json");

             NewUser user_ = gson.fromJson(request.body(), NewUser.class);
             String googleToken = user_.googleToken;

             String userId;

            if((userId = validateGoogleToken(googleToken, db)) == null) {
                return gson.toJson(new StructuredResponse(ERROR, null, TOKEN_ERROR));
            }

            String sessionKey = db.createSessionKey();
            userIdToToken.put(userId, sessionKey);
            return gson.toJson(new StructuredResponse(OK, null, new LoginResponse(userId, sessionKey)));
         });


        // PUT route for updating a row in the DataStore.  This is almost 
        // exactly the same as POST
        //Takes in object with fields: "userId", "msgId", "sessionToken"
        Spark.put("/dislike", (request, response) -> {
            response.status(200);
            response.type("application/json");

            VoteRequest req = gson.fromJson(request.body(), VoteRequest.class);

            if(!validToken(req.userId, req.sessionToken)) {
                return gson.toJson(new StructuredMessageResponse(ERROR, TOKEN_ERROR));
            }

            int result = db.insertDislike(req.userId, req.msgId);
            if (result == -1) {
                return gson.toJson(new StructuredResponse(ERROR, "Element Already Added: ", result));
            } else {
                return gson.toJson(new StructuredResponse(OK, "Created Element: ", result));
            }
        });


        //No functionality for current android system as of 10/27 so not making it compatable with OAuth2
        Spark.put("/messages/:id", (request, response) -> {
            // If we can't get an ID or can't parse the JSON, Spark will send
            // a status 500
            int idx = Integer.parseInt(request.params("id"));
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            // DataRow result = dataStore.updateOne(idx, req.mTitle, req.mMessage);
            int result = db.updateOne(idx, req.mMessage, req.mTitle);
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to update row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, result));
            }
        });

        //No functionality for delete in current android system as of 10/27 so not making it compatable with OAuth2
        Spark.delete("/messages/:id", (request, response) -> {
            // If we can't get an ID, Spark will send a status 500
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int res = db.deleteRow(idx);
            System.out.println("Deleted ID: " + idx);
            // NB: we won't concern ourselves too much with the quality of the 
            //     message sent on a successful delete
            if (res <= 0) {
                return gson.toJson(new StructuredResponse("error", "unable to delete row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, null));
            }
        });

        // DELETE route for removing a row from the DataStore
        Spark.delete("/like", (request, response) -> {
            response.status(200);
            response.type("application/json");

            VoteRequest req = gson.fromJson(request.body(), VoteRequest.class);

            if(!validToken(req.userId, req.sessionToken)) {
                //TODO do we even need this method?
            }

            int res = db.deleteLike(req.userId, req.msgId);
            // NB: we won't concern ourselves too much with the quality of the 
            //     message sent on a successful delete
            if (res <= 0) {
                return gson.toJson(new StructuredResponse("error", "Element not in table, cannot delete userID: " + req.userId + " msgID: " + req.msgId, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, null));
            }
        });

        // DELETE route for removing a row from the DataStore
        Spark.delete("/dislike", (request, response) -> {
    
            VoteRequest req = gson.fromJson(request.body(), VoteRequest.class);

            System.out.println("Attempting to delete like with userId: " + req.userId + " msgId: " + req.msgId);
            response.status(200);
            response.type("application/json");
            int res = db.deleteDislike(req.userId, req.msgId);
            // NB: we won't concern ourselves too much with the quality of the 
            //     message sent on a successful delete
            if (res <= 0) {
                return gson.toJson(new StructuredResponse("error", "Element not in table, cannot delete userID: " + req.userId + " msgID: " + req.msgId, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, null));
            }
        });
    }

    /**
 * Get an integer environment varible if it exists, and otherwise return the
 * default value.
 * 
 * @param envar      The name of the environment variable to get.
 * @param defaultVal The integer value to use as the default if envar isn't found
 * 
 * @returns The best answer we could come up with for a value for envar
 */
    static int getIntFromEnv(String envar, int defaultVal) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get(envar) != null) {
            return Integer.parseInt(processBuilder.environment().get(envar));
        }
        return defaultVal;
    }

    //This method also updates the database for new users
    public static String validateGoogleToken(String idTokenString, Database db) {

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())

                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList("729841771303-h2118dlldms407v0jnbc7pfdadksf466.apps.googleusercontent.com"))
                .build();

    // (Receive idTokenString by HTTPS POST)
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                Payload payload = idToken.getPayload();

                // Print user identifier
                String userId = payload.getSubject();
                System.out.println("User ID: " + userId);

                //Always attempt to insert the user into the db -- worst case scenario, they're already in there
                db.insertUser(userId, (String) payload.get("name"), payload.getEmail());

                // Get profile information from payload
                String email = payload.getEmail();
                if(!email.contains("@lehigh.edu")) {
                    return null;
                }
                boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
                String name = (String) payload.get("name");
                String pictureUrl = (String) payload.get("picture");
                String locale = (String) payload.get("locale");
                String familyName = (String) payload.get("family_name");
                String givenName = (String) payload.get("given_name");
            } else {
                System.out.println("Invalid ID token.");
                return null;
            }
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static boolean validToken(String userId, String token) {
        return userIdToToken.containsKey(userId) && userIdToToken.get(userId).equals(token);
    }
}