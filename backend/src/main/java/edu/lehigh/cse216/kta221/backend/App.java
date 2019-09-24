package edu.lehigh.cse216.kta221.backend;

import spark.Spark;
import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
// Import Google's JSON library
import com.google.gson.*;
import java.util.ArrayList;
import java.sql.DriverManager;



/**
 * For now, our app creates an HTTP server that can only get and add data.
 */
public class App {
    public static void main(String[] args) {

        Map<String, String> env = System.getenv();
        String ip = env.get("POSTGRES_IP");
        String port = env.get("POSTGRES_PORT");
        String user = env.get("POSTGRES_USER");
        String pass = env.get("POSTGRES_PASS");


        // Get the port on which to listen for requests
        Spark.port(getIntFromEnv("PORT", 4567));
        // gson provides us with a way to turn JSON into objects, and objects
        // into JSON
        //
        // NB: it must be final, so that it can be accessed from our lambdas
        //
        // NB: Gson is thread-safe.  See 
        // https://stackoverflow.com/questions/10380835/is-it-ok-to-use-gson-instance-as-a-static-field-in-a-model-bean-reuse
        final Gson gson = new Gson();

        // dataStore holds all of the data that has been provided via HTTP 
        // requests
        //
        // NB: every time we shut down the server, we will lose all data, and 
        //     every time we start the server, we'll have an empty dataStore,
        //     with IDs starting over from 0.
        final DataStore dataStore = new DataStore();
        System.out.println(ip + " " + port + " " + user + " " + pass);
        Database db = Database.getDatabase(ip, port, user, pass);
        db.createTable();
        db.createMessageTable();

        // db.createTable(); 

        if(db == null)
            return;
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





        // GET route that returns all message titles and Ids.  All we do is get 
        // the data, embed it in a StructuredResponse, turn it into JSON, and 
        // return it.  If there's no data, we return "[]", so there's no need 
        // for error handling.
        Spark.get("/messages", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            System.out.println(request);
            ArrayList<Database.MessageRow> result = db.messageAll();
            System.out.println(result);

            System.out.println("GET RESULT LENGTH: " + result.size());



            for(Database.MessageRow item : result) {
                System.out.println("ID: " + item.id);
                System.out.println("SENDER_ID: " + item.senderId);
                System.out.println("TEXT: "+ item.text);
                System.out.println("TIMESTAMP: "+ item.tStamp);
                System.out.println("UP_VOTES: "+ item.nUpVotes);
                System.out.println("UP_VOTES: "+ item.nDownVotes);
                System.out.println("Request Body: "+ item.toString());
            }


            System.out.println("GET MESSAGES");
            response.status(200);
            // db.selectAll();
            response.type("application/json");
            //NEED TO CONVERT STRING HERE
            // return gson.toJson(new StructuredResponse("ok", null, gson.toJson(result)));
            return gson.toJson(new StructuredResponse("ok", null, result));

        });

        // GET route that returns everything for a single row in the DataStore.
        // The ":id" suffix in the first parameter to get() becomes 
        // request.params("id"), so that we can get the requested row ID.  If 
        // ":id" isn't a number, Spark will reply with a status 500 Internal
        // Server Error.  Otherwise, we have an integer, and the only possible 
        // error is that it doesn't correspond to a row with data.
        Spark.get("/messages/:id", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            Database.RowData data = db.selectOne(idx);
            // DataRow data = dataStore.readOne(idx);
            if (data == null) {
                return gson.toJson(new StructuredResponse("error", idx + " not found", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, data));
            }
        });


        Spark.post("/user", (request, response) -> {
            System.out.println(request);
            UserRequest req = gson.fromJson(request.body(), UserRequest.class);
            System.out.println("NAME"+ req.name);
            System.out.println("PASSWORD: "+ req.password);

            response.status(200);
            response.type("application/json");

            int userId = db.insertUser(req.name, req.password);
            if (userId == -1) {
                return gson.toJson(new StructuredUserResponse("error", userId, req.name));
            } else {
                return gson.toJson(new StructuredUserResponse("ok", userId, req.name));
            }

        });

        // POST route for adding a new element to the DataStore.  This will read
        // JSON from the body of the request, turn it into a SimpleRequest 
        // object, extract the title and message, insert them, and return the 
        // ID of the newly created row.
        Spark.post("/messages", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal 
            // Server Error
            MessageRequest req = gson.fromJson(request.body(), MessageRequest.class);
            System.out.println("SENDER_ID: " + req.senderId);
            System.out.println("TEXT: "+ req.text);
            System.out.println("UP_VOTES: "+ req.nUpVotes);
            System.out.println("UP_VOTES: "+ req.nDownVotes);
            System.out.println("Request Body: "+ req.toString());

            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            //     describes the error.
            response.status(200);
            response.type("application/json");

            // db.insertRow(req.mTitle, req.mMessage);
            // db.insertMessage(123, "THIS IS TEST TEXT", 5, 1);
            int newId = db.insertMessage(req.senderId, req.text, req.nUpVotes, req.nDownVotes);

            //NB: createEntry checks for null title and message
            if (newId == -1) {
                return gson.toJson(new StructuredMessageResponse("error", newId));
            } else {
                return gson.toJson(new StructuredMessageResponse("ok", newId));
            }
        });

        // PUT route for updating a row in the DataStore.  This is almost 
        // exactly the same as POST
        Spark.put("/like", (request, response) -> {
            // int idx = Integer.parseInt(request.params("id"));
            VoteRequest req = gson.fromJson(request.body(), VoteRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            // DataRow result = dataStore.updateOne(idx, req.mTitle, req.mMessage);
            int result = db.insertLike(req.userId, req.msgId);
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "Element Already Added: ", result));
            } else {
                return gson.toJson(new StructuredResponse("ok", "Created Element: ", result));
            }
        });

        // PUT route for updating a row in the DataStore.  This is almost 
        // exactly the same as POST
        Spark.put("/dislike", (request, response) -> {
            // int idx = Integer.parseInt(request.params("id"));
            VoteRequest req = gson.fromJson(request.body(), VoteRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            // DataRow result = dataStore.updateOne(idx, req.mTitle, req.mMessage);
            int result = db.insertDislike(req.userId, req.msgId);
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "Element Already Added: ", result));
            } else {
                return gson.toJson(new StructuredResponse("ok", "Created Element: ", result));
            }
        });

        // PUT route for updating a row in the DataStore.  This is almost 
        // exactly the same as POST
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

        // DELETE route for removing a row from the DataStore
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
            boolean result = dataStore.deleteOne(idx);
            if (res <= 0) {
                return gson.toJson(new StructuredResponse("error", "unable to delete row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, null));
            }
        });

        // DELETE route for removing a row from the DataStore
        Spark.delete("/like", (request, response) -> {
           
            VoteRequest req = gson.fromJson(request.body(), VoteRequest.class);

            System.out.println("Attempting to delete like with userId: " + req.userId + " msgId: " + req.msgId);
            response.status(200);
            response.type("application/json");
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
 * @envar      The name of the environment variable to get.
 * @defaultVal The integer value to use as the default if envar isn't found
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
}