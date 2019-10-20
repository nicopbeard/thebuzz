package edu.lehigh.cse216.kta221.backend;
// package edu.lehigh.cse216.kta221.admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.net.*;
import java.time.*;
import java.sql.Timestamp;

import java.util.ArrayList;

import org.apache.commons.lang3.RandomStringUtils.*;
import java.util.Hashtable;
import org.springframework.security.crypto.bcrypt.BCrypt;
 
public class Database {
   
    /**
     * The connection to the database.  When there is no connection, it should
     * be null.  Otherwise, there is a valid open connection
     */
    private Connection mConnection;

    /**
     * A prepared statement for getting all data in the database
     */
    private PreparedStatement mSelectAll;

    /**
     * A prepared statement for getting one row from the database
     */
    private PreparedStatement mSelectOne;

    /**
     * A prepared statement for deleting a row from the database
     */
    private PreparedStatement mDeleteOne;

    /**
     * A prepared statement for inserting into the database
     */
    private PreparedStatement mInsertOne;

    /**
     * A prepared statement for updating a single row in the database
     */
    private PreparedStatement mUpdateOne;

    private PreparedStatement mCreateCommentTable;

    /**
     * A prepared statement for creating the message table in our database
     */
    private PreparedStatement mCreateTable;

        /**
     * A prepared statemebnet for creating the likes table in our database
     */
    private PreparedStatement mCreateMessageTable;

    /**
     * A prepared statement for adding a message to the msgData in our database
     */
    private PreparedStatement mInsertOneMessage;

    /**
     * A prepared statemebnet for creating the likes table in our database
     */
    private PreparedStatement mCreateLikeTable;

    /**
     * A prepared statemebnet for creating the dislikes table in our database
     */
    private PreparedStatement mCreateDislikesTable;

        /**
     * A prepared statemebnet for creating the user table in our database
     */
    private PreparedStatement mCreateUserTable;



        /**
     * A prepared statemebnet for getting the messages table in our database
     */
    private PreparedStatement mSelectAllMessages;

    /**
     * A prepared statement for dropping the table in our database
     */
    private PreparedStatement mDropTable;

    /**
     *  A prepared statement for adding a user to the userData table and returning the unique userID
     */
    private PreparedStatement getUserId;

    /**
     * A prepared statment for adding the userID and messageID 
     * of a like to track who liked what message
     */
    private PreparedStatement likeMessage;

        /**
     * A prepared statment for adding the userID and messageID 
     * of a like to track who disliked what message
     */
    private PreparedStatement dislikeMessage;

    /**
     * A prepared statment for deleting a like on a message
     */
    private PreparedStatement deleteMessageLike;

    /**
     * A prepared statment for releting a like on a message
     */
    private PreparedStatement deleteMessageDislike;


    /**
     * A prepared statment for incrementing up a like on a message
     */
    private PreparedStatement addLikeToMessage;
    /**
     * A prepared statment for incrementing down a like on a message
     */
    private PreparedStatement removeLikeToMessage;

    /**
     * A prepared statment for incrementing up a dislike on a message
     */

    private PreparedStatement addDislikeToMessage;

    /**
     * A prepared statment for incrementing down a dislike on a message
     */

    private PreparedStatement removeDislikeToMessage;


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
        /**
         * The ID of this row of the database
         */
        int id;
        /**
         * The subject stored in this row
         */
        int senderId;
        /**
         * The message stored in this row
         */
        
        String text;

        String tStamp;

        int nUpVotes;

        int nDownVotes;

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

            db.getUserId = db.mConnection.prepareStatement("INSERT INTO userData (userId, name, passHash, username, email) VALUES (default, ?, ?, ?, ?) RETURNING *");

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

            db.mCreateCommentTable = db.mConnection.prepareStatement(
                    "CREATE TABLE comments (commentID SERIAL PRIMARY KEY, msgID int REFERENCES msgdata(msgId), text VARCHAR(500) NOT NULL, " +
                            "tStamp timestamp NOT NULL)");
            
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


        /**
     * THIS WILL NEED TO BE CHANGED ONCE WE GET USERNAME/PASSWORD
     * @return A unique user id for the current session 
     */
    int insertUser(int userID, String name, String passHash, String username, String email) {
        int userId = -1; 

        try {
            getUserId.setInt(1, userID);
            getUserId.setString(2, name);
            getUserId.setString(3, passHash);
            getUserId.setString(4, username);
            getUserId.setString(5, email);
            
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
     String createSessionKey()
     {
         String token = RandomStringUtils.randomAlphanumeric(10);
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

   Boolean passwordAuth(String password)
   {
       String hashed = passwordHasher(password);
        return BCrypt.checkpw(password, hashed );
   }

   String passwordHasher(String plain_password)
   {
       String hashed_password = BCrypt.hashpw(plain_password, BCrypr.gensalt());
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