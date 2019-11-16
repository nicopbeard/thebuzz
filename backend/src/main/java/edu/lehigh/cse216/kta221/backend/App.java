package edu.lehigh.cse216.kta221.backend;

import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import spark.Spark;

import com.fasterxml.jackson.core.JsonFactory;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.auth.AuthInfo;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;

import java.lang.InterruptedException;
import java.net.InetSocketAddress;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeoutException;

import java.util.*;
// Import Google's JSON library
import com.google.gson.*;

import org.omg.CORBA.portable.InputStream;


/**
 * For now, our app creates an HTTP server that can only get and add data.
 */
public class App {

    private final static int TOKEN_ERROR = 1;
    private static final int UPDATE_ERROR = -1;
    private static final String ERROR = "error";
    private static final String OK = "ok";
    private static Map<String, String> userIdToToken = new HashMap<>();
    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    //private static Drive drive;
    private static HttpTransport httpTransport;
    private static FileDataStoreFactory dataStoreFactory;

    public static void main(String[] args) {

        Map<String, String> env = System.getenv();
        String ip = "ec2-174-129-220-12.compute-1.amazonaws.com";
        //String ip = "localhost";
        String port = "5432";
        String user = "qnwrtcuewzcdpe";
        String pass = "a8bc2fbf3637a0fcded45cb4a148de56a37dd37cc3c694145d863374f2ee77a0";

        List<InetSocketAddress> servers = AddrUtil.getAddresses(System.getenv("MEMCACHIER_SERVERS").replace(",", " "));
        AuthInfo authInfo = AuthInfo.plain(System.getenv("MEMCACHIER_USERNAME"), System.getenv("MEMCACHIER_PASSWORD"));

        MemcachedClientBuilder builder = new XMemcachedClientBuilder(servers);

        // Configure SASL auth for each server
        for(InetSocketAddress server : servers) {
            builder.addAuthInfo(server, authInfo);
        }

        // Use binary protocol
        builder.setCommandFactory(new BinaryCommandFactory());
        // Connection timeout in milliseconds (default: )
        builder.setConnectTimeout(1000);
        // Reconnect to servers (default: true)
        builder.setEnableHealSession(true);
        // Delay until reconnect attempt in milliseconds (default: 2000)
        builder.setHealSessionInterval(2000);

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

          //  if(!validToken(req.userId, req.googleToken)) {
           //     status = ERROR;
           // }

            ArrayList<Database.MessageRow> messages = db.messageAll();
            Hashtable<Integer, ArrayList<Database.Comment>> comments = db.commentAll();
            for(ArrayList<Database.Comment> comment: comments.values()){
                for(Database.Comment comm: comment) {
                    System.out.println("comment id: " + comm.commentId + ". It's msgId is " + comm.msgId);
                }
            }
            for(Database.MessageRow msg: messages) {
                System.out.println("Message id is: " + msg.id);
                if(comments.containsKey(msg.id)){
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
            // if(!validToken(req.userId, req.googleToken)) {
            //     return gson.toJson(new StructuredMessageResponse(ERROR, TOKEN_ERROR));
            // }
            System.out.println(req.file);
            if(req.fileName != null)
            {
                try
                {
                    FileOutputStream fos = new FileOutputStream("c:");
                    fos.write(req.file.toString().getBytes());
                    fos.close();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                // File fileMetadata = new File();
                // fileMetadata.setName(req.fileName);
                // java.io.File filePath = new java.io.File("files/photo.jpg");
                // FileContent mediaContent = new FileContent("image/jpeg", filePath);
                // File file = driveService.files().create(fileMetadata, mediaContent)
                //     .setFields("id")
                //     .execute();
                // System.out.println("File ID: " + file.getId());
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


            String userId = validateGoogleToken(googleToken, db);
            if(userId == null)
            {
                return gson.toJson(new StructuredMessageResponse("error", TOKEN_ERROR));
            }
            else
            {
                return gson.toJson(new StructuredResponse("ok", null, null));
            }
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
            try {
                MemcachedClient mc = builder.build();
                try {
                    mc.set(sessionKey, 0, userId);
                    String val = mc.get(sessionKey);
                    System.out.println(val);
                } catch (TimeoutException te) {
                    System.err.println("Timeout during set or get: " + te.getMessage());
                } catch (InterruptedException ie) {
                    System.err.println("Interrupt during set or get: " + ie.getMessage());
                } catch (MemcachedException me) {
                    System.err.println("Memcached error during get or set: " + me.getMessage());
                }
            } catch (IOException ioe) {
                System.err.println("Couldn't create a connection to MemCachier: " + ioe.getMessage());
            }
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

    // private static File insertFile(Drive service, String title, String description, String parentId, String mimeType, String fileType)
    // {
    //     Rope stringGroup = new Rope();
    //     stringGroup.setTitle(title);
    //     stringGroup.setDescription(description);
    //     stringGroup.setMimeType(mimeType);

    //     if(parentId != null && parentId.length() > 0)
    //     {
    //         body.setParents(Arrays.asList(new ParentReference().setId(parentId)));
    //     }

    //     java.io.File fileContent = new java.io.File(stringGroup);
    //     FileContent mediaContent = new FileContent(mimeType, fileContent);
    // }

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
                .setAudience(Arrays.asList("134181037844-18emho3bt0dlqt83u92rrtkk0kfolha7.apps.googleusercontent.com",
                        "134181037844-37lmk8rscmd0uqff6g1gu4b5q2su6f8i.apps.googleusercontent.com"))
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
                return userId;
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