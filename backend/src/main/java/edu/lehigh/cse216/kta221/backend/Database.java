package edu.lehigh.cse216.kta221.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.net.*;
import java.sql.Timestamp;

import java.util.ArrayList;

import org.apache.commons.lang3.RandomStringUtils;
import java.util.Hashtable;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class Database {
   
    /**
     * The connection to the database.  When there is no connection, it should
     * be null.  Otherwise, there is a valid open connection
     */
    private Connection mConnection;
    private PreparedStatement mSelectAll;
    private PreparedStatement mSelectOne;
    private PreparedStatement mDeleteOne;
    private PreparedStatement mInsertOne;
    private PreparedStatement mUpdateOne;
    private PreparedStatement mInsertOneMessage;
    private PreparedStatement mSelectAllMessages;
    private PreparedStatement mDropTable;
    private PreparedStatement getUserId;
    private PreparedStatement likeMessage;
    private PreparedStatement dislikeMessage;
    private PreparedStatement deleteMessageLike;
    private PreparedStatement deleteMessageDislike;
    private PreparedStatement addLikeToMessage;
    private PreparedStatement removeLikeToMessage;
    private PreparedStatement addDislikeToMessage;
    private PreparedStatement removeDislikeToMessage;
    private PreparedStatement selectAllComments;



    Hashtable<Integer, String> cache = new Hashtable<Integer, String>();


    /**
     * RowData is like a struct in C: we use it to hold data, and we allow 
     * direct access to its fields.  In the context of this Database, RowData 
     * represents the data we'd see in a row.
     * 
     * We make RowData a static class of Database because we don't really want
     * to encourage users to think of RowData as being anything other than an
     * abstract representation of a row of the database.  RowData and the 
     * Database are tightly coupled: if one changes, the other should too.
     */
    public static class RowData {
        /**
         * The ID of this row of the database
         */
        int mId;
        /**
         * The subject stored in this row
         */
        String mSubject;
        /**
         * The message stored in this row
         */
        String mMessage;

        /**
         * Construct a RowData object by providing values for its fields
         */
        public RowData(int id, String subject, String message) {
            mId = id;
            mSubject = subject;
            mMessage = message;
        }
    }

    public static class MessageRow {
        int id;
        int senderId;
        String text;
        String tStamp;
        int nUpVotes;
        int nDownVotes;
        ArrayList<Comment> comments;

        /**
         * Construct a MessageRow object by providing values for its fields
         */
        public MessageRow(int id, int senderId, String text, String tStamp, int nUpVotes, int nDownVotes) {
            this.id = id; 
            this.senderId = senderId;
            this.text = text;
            this.tStamp = tStamp;
            this.nUpVotes = nUpVotes;
            this.nDownVotes = nDownVotes;
            comments = new ArrayList<>();
        }

        public void addComments(ArrayList<Comment> comments) {
            this.comments = comments;
        }

    }

    public static class Comment{
        int commentId;

        int msgId;

        String text;

        String tStamp;

        public Comment(int commentId, int msgId, String text, String tStamp) {
            this.commentId = commentId;
            this.msgId = msgId;
            this.text = text;
            this.tStamp = tStamp;
        }
    }


    public static class UserRow {

        int userId;

        String name;

        String password;


        /**
         * Construct a MessageRow object by providing values for its fields
         */
        public UserRow(int userId, String name, String password) {
            this.userId = userId;
            this.name = name;
            this.password = password;
        }
    }

    /**
     * The Database constructor is private: we only create Database objects 
     * through the getDatabase() method.
     */
    private Database() {
    }

    /**
     * Get a fully-configured connection to the database
     * 
     * @param ip   The IP address of the database server
     * @param port The port on the database server to which connection requests
     *             should be sent
     * @param user The user ID to use when connecting
     * @param pass The password to use when connecting
     * 
     * @return A Database object, or null if we cannot connect properly
     */
    static Database getDatabase(String ip, String port, String user, String pass) {
        Database db = new Database();
        Map<String, String> env = System.getenv();
        String db_url = "postgres://qnwrtcuewzcdpe:a8bc2fbf3637a0fcded45cb4a148de56a37dd37cc3c694145d863374f2ee77a0@ec2-174-129-220-12.compute-1.amazonaws.com:5432/dcl2b7tskpghar";
        //String db_url = env.get("DATABASE_URL");

        // Give the Database object a connection, fail if we cannot get one
        try {
            Class.forName("org.postgresql.Driver");
            URI dbUri = new URI(db_url);
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";
            Connection conn = DriverManager.getConnection(dbUrl, username, password);
            if (conn == null) {
                System.err.println("Error: DriverManager.getConnection() returned a null object");
                return null;
            }
            db.mConnection = conn;

        } catch (SQLException e) {
            System.err.println("Error: DriverManager.getConnection() threw a SQLException");
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Unable to find postgresql driver");
            return null;
        } catch (URISyntaxException s) {
            System.out.println("URI Syntax Error");
            return null;
        } catch (Exception e) {
            System.out.println(String.format("Unknown exception in Database.java: %s", e.getMessage()));
        }

        // Attempt to create all of our prepared statements.  If any of these 
        // fail, the whole getDatabase() call should fail
        try {
            // NB: we can easily get ourselves in trouble here by typing the
            //     SQL incorrectly.  We really should have things like "tblData"
            //     as constants, and then build the strings for the statements
            //     from those constants.

            // Note: no "IF NOT EXISTS" or "IF EXISTS" checks on table 
            // creation/deletion, so multiple executions will cause an exception
            // db.mCreateTable = db.mConnection.prepareStatement(
            //         "CREATE TABLE tblData (id SERIAL PRIMARY KEY, subject VARCHAR(50) "
            //         + "NOT NULL, message VARCHAR(500) NOT NULL)");

            // db.mCreateMessageTable = db.mConnection.prepareStatement(
            //         "CREATE TABLE msgData (id SERIAL PRIMARY KEY, senderID int NOT NULL, "
            //         + "text VARCHAR(50) NOT NULL,"
            //         + "tStamp timestamp NOT NULL, "
            //         + "numUpVotes int NOT NULL, "
            //         + "numDownVotes int NOT NULL)");

            db.mInsertOneMessage = db.mConnection.prepareStatement(
                "INSERT INTO msgData (msgId, userId, text, tStamp, numUpVotes, numDownVotes) VALUES (default, ?, ?, ?, ?, ?) RETURNING *");

            
            db.mSelectAllMessages = db.mConnection.prepareStatement("SELECT msgId, userId, text, tStamp, numUpVotes, numDownVotes FROM msgData");
            
            // db.mLastAdded = db.mConnection.prepareStatement("SELECT LAST (id) FROM msgData");

            db.getUserId = db.mConnection.prepareStatement("INSERT INTO userData (userid,name,passhash, username, email) VALUES (?, ?, ?,?, ?) RETURNING *");

            db.mDropTable = db.mConnection.prepareStatement("DROP TABLE tblData");


            db.likeMessage = db.mConnection.prepareStatement(
                "INSERT INTO likeData (userId, msgId)"
                + " VALUES ( (SELECT userId from userData WHERE userId=?), (SELECT msgId from msgData WHERE msgId=?) )");


            db.dislikeMessage = db.mConnection.prepareStatement(
                "INSERT INTO dislikeData (userId, msgId)"
                + " VALUES ( (SELECT userId from userData WHERE userId=?), (SELECT msgId from msgData WHERE msgId=?) )");


            db.deleteMessageLike = db.mConnection.prepareStatement("DELETE FROM likeData WHERE userId = ? and msgId = ?");

            db.deleteMessageDislike = db.mConnection.prepareStatement("DELETE FROM dislikeData WHERE userId = ? and msgId = ?");

            
            db.addLikeToMessage = db.mConnection.prepareStatement("UPDATE msgData SET numupvotes = numupvotes + 1 WHERE msgId = ?");
            db.removeLikeToMessage = db.mConnection.prepareStatement("UPDATE msgData SET numupvotes = numupvotes - 1 WHERE msgId = ?");
            db.addDislikeToMessage = db.mConnection.prepareStatement("UPDATE msgData SET numdownvotes = numdownvotes + 1 WHERE msgId = ?");
            db.removeDislikeToMessage = db.mConnection.prepareStatement("UPDATE msgData SET numdownvotes = numdownvotes - 1 WHERE msgId = ?");


            // Standard CRUD operations
            db.mDeleteOne = db.mConnection.prepareStatement("DELETE FROM tblData WHERE id = ?");
            db.mInsertOne = db.mConnection.prepareStatement("INSERT INTO tblData VALUES (default, ?, ?)");
            db.mSelectAll = db.mConnection.prepareStatement("SELECT id, subject, message FROM tblData");
            db.mSelectOne = db.mConnection.prepareStatement("SELECT * from tblData WHERE id=?");
            db.mUpdateOne = db.mConnection.prepareStatement("UPDATE tblData SET message = ?, subject = ? WHERE id = ?");
           
            //getting the comments
            db.selectAllComments = db.selectAllComments.prepareStatement("SELECT * from comments ORDER BY msgid, tstamp;");

        } catch (SQLException e) {
            System.err.println("Error creating prepared statement");
            e.printStackTrace();
            db.disconnect();
            return null;
        }
        return db;
    }

    /**
     * Close the current connection to the database, if one exists.
     * 
     * NB: The connection will always be null after this call, even if an 
     *     error occurred during the closing operation.
     * 
     * @return True if the connection was cleanly closed, false otherwise
     */
    boolean disconnect() {
        if (mConnection == null) {
            System.err.println("Unable to close connection: Connection was null");
            return false;
        }
        try {
            mConnection.close();
        } catch (SQLException e) {
            System.err.println("Error: Connection.close() threw a SQLException");
            e.printStackTrace();
            mConnection = null;
            return false;
        }
        mConnection = null;
        return true;
    }

    HashTable<Integer, ArrayList<Database.Comment>> commentAll()
    {    
        Hashtable<Integer, ArrayList<Database.Comment>> commentHash = new HashTable<Integer, ArrayList<Database.Comment>>();
        
        try {
            ResultSet rs = selectAllComments.executeQuery();
            while (rs.next()) {
                Database.Comment c = new Database.Comment(rs.getInt("commentid"), rs.getInt("msgid"), rs.getString("text"),rs.getString("tstamp"));
                if(commentHash.containsKey(c.msgId))
                {
                    commentHash.get(c.msgId).add(c);
                }
                else{
                    commentHash.put(c.msgId, new ArrayList<Database.Comment>(Arrays.asList(c)));
                }
            }
            rs.close();
            return commentHash;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

        /**
     * THIS WILL NEED TO BE CHANGED ONCE WE GET USERNAME/PASSWORD
     * @return A unique user id for the current session 
     */
    int insertUser(int userID, String passHash) {
        int userId = -1; 

        try {
            getUserId.setInt(1, userID);
            getUserId.setString(2, "tempName");
            getUserId.setString(3, passHash);
            getUserId.setString(4, "tempUserName");
            getUserId.setString(5, "tempEmail");

           
            ResultSet rs = getUserId.executeQuery();
            // MessageRow returnRow = new MessageRow(rs.getInt("id"), rs.getInt("senderID"), rs.getString("text"), rs.getString("tStamp"), rs.getInt("numUpVotes"), rs.getInt("numDownVotes"));
            while (rs.next()){
                userId = rs.getInt("userId");
            };
            rs.close();
            return userId;
            // System.out.println("Database output: "+ subject + ":" + message);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }

    /**
     * 
     * @param userId userId which liked the message
     * @param msgId messageId of the message whitch the user liked
     * @return Int value of the number of rows which were updated 
     */
    int insertLike(int userId, int msgId){
        int res = -1;
        try {
            System.out.println("Searching for: " + userId + "and: "+ msgId);
            likeMessage.setInt(1, userId);
            likeMessage.setInt(2, msgId);
            res = likeMessage.executeUpdate();
            if(res == 1) {
                addLikeToMessage.setInt(1, msgId);
                addLikeToMessage.executeUpdate();
            } else {
                System.out.println("Could not like message");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 
     * @param userId userId which liked the message
     * @param msgId messageId of the message whitch the user liked
     * @return Int value of the number of rows which were updated 
     */
    int insertDislike(int userId, int msgId){
        int res = -1;
        try {
            System.out.println("Searching for: " + userId + "and: "+ msgId);
            dislikeMessage.setInt(1, userId);
            dislikeMessage.setInt(2, msgId);
            res = dislikeMessage.executeUpdate();
            if(res == 1) {
                addDislikeToMessage.setInt(1, msgId);
                addDislikeToMessage.executeUpdate();
            } else {
                System.out.println("Could not dislike message");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }



    /**
     * Insert a row into the database
     * 
     * @param subject The subject for this new row
     * @param message The message body for this new row
     * 
     * @return The number of rows that were inserted
     */
    int insertRow(String subject, String message) {
        int count = 0;
        try {
            mInsertOne.setString(1, subject);
            mInsertOne.setString(2, message);
            count += mInsertOne.executeUpdate();
            System.out.println("Database output: "+ subject + ":" + message);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }


    int insertMessage(int senderID, String text, int nUpVotes, int nDownVotes) {
        //LocalDateTime currTime = LocalDateTime.now();
        Date date = new Date();
        Timestamp currTime = new Timestamp(date.getTime());
        int id = -1;
        try {
            mInsertOneMessage.setInt(1, senderID);
            mInsertOneMessage.setString(2, text);
            mInsertOneMessage.setTimestamp(3, currTime);
            mInsertOneMessage.setInt(4, nUpVotes);
            mInsertOneMessage.setInt(5, nDownVotes);
            ResultSet rs = mInsertOneMessage.executeQuery();
            // MessageRow returnRow = new MessageRow(rs.getInt("id"), rs.getInt("senderID"), rs.getString("text"), rs.getString("tStamp"), rs.getInt("numUpVotes"), rs.getInt("numDownVotes"));
            while (rs.next()){
                id = rs.getInt("msgID");
            };
            rs.close();
            return id;
            // System.out.println("Database output: "+ subject + ":" + message);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    /**
     * Query the database for a list of all subjects and their IDs
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<RowData> selectAll() {
        ArrayList<RowData> res = new ArrayList<RowData>();
        try {
            ResultSet rs = mSelectAll.executeQuery();
            while (rs.next()) {
                res.add(new RowData(rs.getInt("id"), rs.getString("subject"), rs.getString("message")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

        /**
     * Query the database for a list of all subjects and their IDs
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<MessageRow> messageAll() {
        ArrayList<MessageRow> res = new ArrayList<MessageRow>();
        try {
            ResultSet rs = mSelectAllMessages.executeQuery();
            while (rs.next()) {
                res.add(new MessageRow(rs.getInt("msgid"), rs.getInt("userid"), rs.getString("text"), rs.getString("tStamp"), rs.getInt("numUpVotes"), rs.getInt("numDownVotes") ));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }








    /**
     * Get all data for a specific row, by ID
     * 
     * @param id The id of the row being requested
     * 
     * @return The data for the requested row, or null if the ID was invalid
     */
    RowData selectOne(int id) {
        RowData res = null;
        try {
            mSelectOne.setInt(1, id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                res = new RowData(rs.getInt("id"), rs.getString("subject"), rs.getString("message"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Delete a row by ID
     * 
     * @param id The id of the row to delete
     * 
     * @return The number of rows that were deleted.  -1 indicates an error.
     */
    int deleteRow(int id) {
        System.out.println("Deleting index: " + id);
        int res = -1;
        try {
            mDeleteOne.setInt(1, id);
            res = mDeleteOne.executeUpdate();
            System.out.println("Delete Result: " + res);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    int deleteLike(int userId, int msgId) {
        System.out.println("Deleting index: " + userId + " " + msgId);
        int res = -1;
        try {
            deleteMessageLike.setInt(1, userId);
            deleteMessageLike.setInt(2, msgId);
            res = deleteMessageLike.executeUpdate();
            System.out.println("Delete Like Result: " + res);
            if(res==1) {
                removeLikeToMessage.setInt(1, msgId);
                removeLikeToMessage.executeUpdate();
            } else {
                System.out.println("Could not un-like message");
            }
        
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    int deleteDislike(int userId, int msgId) {
        System.out.println("Deleting index: " + userId + " " + msgId);
        int res = -1;
        try {
            deleteMessageDislike.setInt(1, userId);
            deleteMessageDislike.setInt(2, msgId);
            res = deleteMessageDislike.executeUpdate();
            System.out.println("Delete Dislike Result: " + res);
            if(res==1) {
                removeDislikeToMessage.setInt(1, msgId);
                removeDislikeToMessage.executeUpdate();
            } else {
                System.out.println("Could not un-dislike message");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    int updateOne(int id, String message, String subject) {
        int res = -1;
        try {
            mUpdateOne.setString(1, message);
            mUpdateOne.setString(2, subject);
            mUpdateOne.setInt(3, id);


            res = mUpdateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    
    // Creates a session key that will be put in the HashTable
    //Used when adding a new user
     String createSessionKey(int userId)
     {
         RandomStringUtils a = new RandomStringUtils();
         String token = a.randomAlphanumeric(10);
         cache.put(userId, token);
         System.out.println("ADDING TO THE CACHE"+cache.toString());
         return token;
     }

     // Used during login to assign a new key to the Userid
     // Makes sure user is only logged in on one place at a time
     //Returns the new token if
     String replaceKey(int userId)
     {
        RandomStringUtils a = new RandomStringUtils();
         String token = a.randomAlphanumeric(10);
         cache.replace(userId, token);
         System.out.println("CACHE REPLACE TEST"+cache.toString());
         return token;
     }

   Boolean tokenAuth(Integer userId, String token)
   {
       boolean doesMap = false;
       String testToken = cache.get(userId);
       if(testToken.equals(token))
            doesMap = true;
        
        return (cache.contains(token) && doesMap);
   }

   Boolean hasKey(Integer userId)
   {
       return cache.containsKey(userId);
   }

   Boolean passwordAuth(String password)
   {
        return BCrypt.checkpw(password, passwordHasher(password) );
   }

   String passwordHasher(String plain_password)
   {
       String hashed_password = BCrypt.hashpw(plain_password, BCrypt.gensalt());
       return hashed_password;
   }



    /**
     * Create tblData.  If it already exists, this will print an error
     */
    // void createTable() {
    //     try {
    //         mCreateTable.execute();
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    // }

    // void createMessageTable() {
    //     try {
    //         mCreateMessageTable.execute();
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    // }

    /**
     * Remove tblData from the database.  If it does not exist, this will print
     * an error.
     */
    void dropTable() {
        try {
            mDropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}