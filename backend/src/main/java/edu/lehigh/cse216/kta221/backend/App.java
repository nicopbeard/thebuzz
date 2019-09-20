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
            ArrayList<Database.RowData> result = db.selectAll();
            System.out.println(result);

            System.out.println("GET RESULT LENGTH: " + result.size());



            for(Database.RowData item : result) {
                System.out.println("MESSAGE: " + item.mMessage);
                System.out.println("SUBJECT: " + item.mSubject);
                System.out.println("ID: " + item.mId);
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

        // POST route for adding a new element to the DataStore.  This will read
        // JSON from the body of the request, turn it into a SimpleRequest 
        // object, extract the title and message, insert them, and return the 
        // ID of the newly created row.
        Spark.post("/messages", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal 
            // Server Error
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            System.out.println("SUBJECT: " + req.mTitle);
            System.out.println("MESSAGE: "+ req.mMessage);
            System.out.println("Request Body: "+ req.toString());

            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            //     describes the error.
            response.status(200);
            response.type("application/json");

            db.insertRow(req.mTitle, req.mMessage);

            // NB: createEntry checks for null title and message
            int newId = dataStore.createEntry(req.mTitle, req.mMessage);
            if (newId == -1) {
                return gson.toJson(new StructuredResponse("error", "error performing insertion", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", "" + newId, null));
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