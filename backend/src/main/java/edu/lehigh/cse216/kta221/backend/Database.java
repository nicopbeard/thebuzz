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
    private PreparedStatement updateMsg;
    private PreparedStatement deleteMessage;
    private PreparedStatement mSelectAllMessages;
    private PreparedStatement mInsertOneFile;
    private PreparedStatement mSelectAllFiles;
    private PreparedStatement getMsgId;
    private PreparedStatement getFileId;
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

        int mId;

        String mSubject;
        String mMessage;

        public RowData(int id, String subject, String message) {
            mId = id;
            mSubject = subject;
            mMessage = message;
        }
    }

    public static class MessageRow {
        int id;
        String senderId;
        String text;
        String tStamp;
        int nUpVotes;
        int nDownVotes;
        float longitude;
        float latitude;
        ArrayList<Comment> comments;
        com.google.api.services.drive.model.File file;

        /**
         * Construct a MessageRow object by providing values for its fields
         */
        public MessageRow(int id, String senderId, String text, String tStamp, int nUpVotes, int nDownVotes, float longitude, float latitude) {
            this.id = id;
            this.senderId = senderId;
            this.text = text;
            this.tStamp = tStamp;
            this.nUpVotes = nUpVotes;
            this.nDownVotes = nDownVotes;
            this.longitude = longitude;
            this.latitude = latitude;
            comments = new ArrayList<>();
        }

        public void addComments(ArrayList<Comment> comments) {
            this.comments = comments;
        }

        public void addFiles(com.google.api.services.drive.model.File file) {
            this.file = file;
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

    public static class File{
        int msgId;

        String fileid;

        public File(int msgId, String fileid) {
            this.msgId = msgId;
            this.fileid = fileid;
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
                "INSERT INTO msgData (msgId, userId, text, tStamp, numUpVotes, numDownVotes, longitude, latatitude) VALUES (default, ?, ?, ?, ?, ?, ?, ?) RETURNING *");

            
            db.mSelectAllMessages = db.mConnection.prepareStatement("SELECT msgId, userId, text, tStamp, numUpVotes, numDownVotes, longitude, latitude FROM msgData");
            
            // db.mLastAdded = db.mConnection.prepareStatement("SELECT LAST (id) FROM msgData");

            db.mInsertOneFile = db.mConnection.prepareStatement("INSERT INTO filedata (msgid, fileid) VALUES (?, ?) RETURNING *");

            db.mSelectAllFiles = db.mConnection.prepareStatement("SELECT msgid, fileid FROM filedata");

            db.getMsgId = db.mConnection.prepareStatement("SELECT msgid FROM filedata WHERE msgid = ?");

            db.getFileId = db.mConnection.prepareStatement("SELECT fileid FROM filedata WHERE msgid = ?");

            db.getUserId = db.mConnection.prepareStatement("INSERT INTO userData (userid,name,passhash, username, email, longitude, latitude) VALUES (?, ?, ?,?, ?, ?, ?) RETURNING *");

            db.mDropTable = db.mConnection.prepareStatement("DROP TABLE tblData");


            db.likeMessage = db.mConnection.prepareStatement(
                "INSERT INTO likeData (userId, msgId)"
                + " VALUES ( (SELECT userId from userData WHERE userId=?), (SELECT msgId from msgData WHERE msgId=?) )");


            db.dislikeMessage = db.mConnection.prepareStatement(
                "INSERT INTO dislikeData (userId, msgId)"
                + " VALUES ( (SELECT userId from userData WHERE userId=?), (SELECT msgId from msgData WHERE msgId=?) )");


            db.deleteMessageLike = db.mConnection.prepareStatement("DELETE FROM likeData WHERE userId = ? and msgId = ?");

            db.deleteMessageDislike = db.mConnection.prepareStatement("DELETE FROM dislikeData WHERE userId = ? and msgId = ?");

            db.deleteMessage = db.mConnection.prepareStatement("DELETE FROM msgdata WHERE msgId = ?; DELETE FROM comments WHERE msgId = ?; DELETE FROM filedata WHERE msgId = ?; DELETE FROM likedata WHERE msgId = ?; DELETE FROM dislikedata WHERE msgId = ?");

            db.addLikeToMessage = db.mConnection.prepareStatement("UPDATE msgData SET numupvotes = numupvotes + 1 WHERE msgId = ?");
            db.removeLikeToMessage = db.mConnection.prepareStatement("UPDATE msgData SET numupvotes = numupvotes - 1 WHERE msgId = ?");
            db.addDislikeToMessage = db.mConnection.prepareStatement("UPDATE msgData SET numdownvotes = numdownvotes + 1 WHERE msgId = ?");
            db.removeDislikeToMessage = db.mConnection.prepareStatement("UPDATE msgData SET numdownvotes = numdownvotes - 1 WHERE msgId = ?");
            db.updateMsg = db.mConnection.prepareStatement("UPDATE msgdata SET text = ? WHERE msgId = ?");


            // Standard CRUD operations
            db.mDeleteOne = db.mConnection.prepareStatement("DELETE FROM tblData WHERE id = ?");
            db.mInsertOne = db.mConnection.prepareStatement("INSERT INTO tblData VALUES (default, ?, ?)");
            db.mSelectAll = db.mConnection.prepareStatement("SELECT id, subject, message FROM tblData");
            db.mSelectOne = db.mConnection.prepareStatement("SELECT * from tblData WHERE id=?");
            db.mUpdateOne = db.mConnection.prepareStatement("UPDATE tblData SET message = ?, subject = ? WHERE id = ?");
           
            //getting the comments
            db.selectAllComments = db.mConnection.prepareStatement("SELECT * from comments ORDER BY msgid, tstamp;");

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

    Hashtable<Integer, ArrayList<Comment>> commentAll()
        {
        Hashtable<Integer, ArrayList<Database.Comment>> commentHash = new Hashtable<Integer, ArrayList<Database.Comment>>();
        
        try {
            ResultSet rs = selectAllComments.executeQuery();
            while (rs.next()) {
                Database.Comment c = new Database.Comment(rs.getInt("commentid"), rs.getInt("msgid"), rs.getString("text"),rs.getString("tstamp"));
                System.out.println("Comment ID ="+ c.commentId);
                System.out.println("Message ID ="+ c.msgId);
                if(commentHash.containsKey(c.msgId))
                {
                   // System.out.println("COMMENT ALL HASH TEST 777");
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
        //TODO change db so userId is now a varchar
    boolean insertUser(String userID, String name, String email, float longitude, float latitude) {
        try { 
            getUserId.setString(1, userID);
            getUserId.setString(2, name);
            getUserId.setString(3, "passHash");
            getUserId.setString(4, "tempUserName");
            getUserId.setString(5, email);
            getUserId.setFloat(6, longitude);
            getUserId.setFloat(7, latitude);
           
            ResultSet rs = getUserId.executeQuery();
            while (rs.next()){
            };
            rs.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 
     * @param userId userId which liked the message
     * @param msgId messageId of the message whitch the user liked
     * @return Int value of the number of rows which were updated 
     */
    int insertLike(String userId, int msgId){
        int res = -1;
        try {
            System.out.println("Searching for: " + userId + "and: "+ msgId);
            likeMessage.setString(1, userId);
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
    int insertDislike(String userId, int msgId){
        int res = -1; //placeholder value -- doesn't mean anything
        try {
            dislikeMessage.setString(1, userId);
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

    int insertMessage(String senderID, String text, int nUpVotes, int nDownVotes, float longitude, float latitude) {
        //LocalDateTime currTime = LocalDateTime.now();
        Date date = new Date();
        Timestamp currTime = new Timestamp(date.getTime());
        int id = -1;
        try {
            mInsertOneMessage.setString(1, senderID);
            mInsertOneMessage.setString(2, text);
            mInsertOneMessage.setTimestamp(3, currTime);
            mInsertOneMessage.setInt(4, nUpVotes);
            mInsertOneMessage.setInt(5, nDownVotes);
            mInsertOneMessage.setFloat(6, longitude);
            mInsertOneMessage.setFloat(7, latitude);
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

    int insertFile(int msgId, String fileId) {
        int id = -1;
        try {
            mInsertOneFile.setInt(1, msgId);
            mInsertOneFile.setString(2, fileId);
            ResultSet rs = mInsertOneFile.executeQuery();
            while (rs.next()){
                id = rs.getInt("msgid");
            };
            rs.close();
            return id;
            // System.out.println("Database output: "+ subject + ":" + message);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    int getMsgIdFromFile(int msgId) {
        int id = -1;
        try {
            getMsgId.setInt(1, msgId);
            ResultSet rs = getMsgId.executeQuery();
            while (rs.next()){
                id = rs.getInt("msgid");
            };
            rs.close();
            return id;
            // System.out.println("Database output: "+ subject + ":" + message);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    String getFileId(int msgId) {
        String id = null;
        try {
            getFileId.setInt(1, msgId);
            ResultSet rs = getFileId.executeQuery();
            while (rs.next()){
                id = rs.getString("fileid");
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
    ArrayList<MessageRow> messageAll() {
        ArrayList<MessageRow> res = new ArrayList<MessageRow>();
        try {
            ResultSet rs = mSelectAllMessages.executeQuery();
            while (rs.next()) {
                res.add(new MessageRow(rs.getInt("msgid"), rs.getString("userid"), rs.getString("text"), rs.getString("tStamp"), rs.getInt("numUpVotes"), rs.getInt("numDownVotes"), rs.getFloat("longitude"), rs.getFloat("latitude") ));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    ArrayList<File> fileAll() {
        ArrayList<File> res = new ArrayList<File>();
        try {
            ResultSet rs = mSelectAllFiles.executeQuery();
            while (rs.next()) {
                res.add(new File(rs.getInt("msgid"), rs.getString("fileid")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
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

    int deleteMsg(int msgId) {
        System.out.println("Deleting msg: " + msgId);
        int res = -1;
        try {
            deleteMessage.setInt(1, msgId);
            deleteMessage.setInt(2, msgId);
            deleteMessage.setInt(3, msgId);
            deleteMessage.setInt(4, msgId);
            deleteMessage.setInt(5, msgId);
            res = deleteMessage.executeUpdate();
            System.out.println("Delete Result: " + res);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    int deleteLike(String userId, int msgId) {
        System.out.println("Deleting index: " + userId + " " + msgId);
        int res = -1;
        try {
            deleteMessageLike.setString(1, userId);
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

    int deleteDislike(String userId, int msgId) {
        System.out.println("Deleting index: " + userId + " " + msgId);
        int res = -1;
        try {
            deleteMessageDislike.setString(1, userId);
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

    int updateMsg(int msgId, String text) {
        int res = -1;
        try {
            updateMsg.setString(1, text);
            updateMsg.setInt(2, msgId);
            res = updateMsg.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    
    // Creates a session key that will be put in the HashTable
    //Used when adding a new user
     String createSessionKey()
     {
         RandomStringUtils a = new RandomStringUtils();
         String token = a.randomAlphanumeric(10);
         return token;
     }


   String passwordHasher(String plain_password)
   {
       String hashed_password = BCrypt.hashpw(plain_password, BCrypt.gensalt());
       return hashed_password;
   }

}