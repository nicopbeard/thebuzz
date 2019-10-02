package edu.lehigh.cse216.kta221.admin;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Timestamp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Map;

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
     * A prepared statement for getting all data in the database
     */
    private PreparedStatement mSelectAllMsg;
    /**
     * A prepared statement for getting all data in the database
     */
    private PreparedStatement mSelectAllUsers;

    /**
     * A prepared statement for getting one row from the database
     */
    private PreparedStatement mSelectOne;
    private PreparedStatement mSelectOneMsg;    
    private PreparedStatement mSelectOneUser;

    /**
     * Select one user's upvote/downvote data
     */
    private PreparedStatement mSelectOneUserUpvote;
    private PreparedStatement mSelectOneUserDownvote;
    
    /**
     * Select one message's upvote/downvote data
     */
    private PreparedStatement mSelectOneMsgUpvote;
    private PreparedStatement mSelectOneMsgDownvote;
    

    /**
     * A prepared statement for deleting a row from the database
     */
    private PreparedStatement mDeleteOne;

    /**
     * A prepared statement for deleting a row from the message database
     */
    private PreparedStatement mDeleteOneMsg;

    /**
     * A prepared statement for deleting a row from the user database
     */
    private PreparedStatement mDeleteOneUser;

    /**
     * A prepared statement for inserting into the database
     */
    private PreparedStatement mInsertOne;

    /**
     * A prepared statement for updating a single row in the database
     */
    private PreparedStatement mUpdateOne;

    /**
     * A prepared statement for dropping the table in our database
     */
    private PreparedStatement mDropTable;

    /**
     * A prepared statement for creating the table in our database
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
     * A prepared statement for adding a user to the userData in our database
     */
    private PreparedStatement mInsertOneUser;

    /**
     * A prepared statemebnet for creating the likes table in our database
     */
    private PreparedStatement mCreateLikeData;

    /**
     * A prepared statemebnet for creating the dislikes table in our database
     */
    private PreparedStatement mCreateDislikeData;

    private PreparedStatement mCreateCommentTable;

    /**
     * A prepared statemebnet for creating the user table in our database
     */
    private PreparedStatement mCreateUserTable;

    /**
     * A prepared statemebnet for dropping the message table in our database
     */
    private PreparedStatement mDropMessageTable;

    /**
     * A prepared statemebnet for dropping the user table in our database
     */
    private PreparedStatement mDropUserTable;

    /**
     * A prepared statemebnet for dropping the likedata table in our database
     */
    private PreparedStatement mDropLikeData;

    /**
     * A prepared statemebnet for dropping the dislikedata table in our database
     */
    private PreparedStatement mDropDislikeData;

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

    public static class MsgData {
        int mMsgID;
        int mUserID;
        String mText;
        java.sql.Timestamp mTstamp;
        int mNumUpvotes;
        int mNumDownvotes;

        public MsgData(int msgID, int userID, String text, java.sql.Timestamp timestamp, int numUpvotes,
                int numDownvotes) {
            mMsgID = msgID;
            mUserID = userID;
            mText = text;
            mTstamp = timestamp;
            mNumUpvotes = numUpvotes;
            mNumDownvotes = numDownvotes;
        }
    }

    public static class UserData {
        int mUserID;
        String mName;
        String mPassword;

        public UserData(int userID, String name, String password) {
            mUserID = userID;
            mName = name;
            mPassword = password;
        }
    }

    public static class LikeData {
        int mUserID;
        int mMsgID;

        public LikeData(int userID, int msgID) {
            mUserID = userID;
            mMsgID = msgID;
        }
    }

    public static class DislikeData {
        int mUserID;
        int mMsgID;

        public DislikeData(int userID, int msgID) {
            mUserID = userID;
            mMsgID = msgID;
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
        // Create an un-configured Database object
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

            //message table
            db.mCreateMessageTable = db.mConnection.prepareStatement(
                    "CREATE TABLE msgData (msgID SERIAL PRIMARY KEY, userID int NOT NULL, "
                    + "text VARCHAR(500) NOT NULL,"
                    + "tStamp timestamp NOT NULL, "
                    + "numUpvotes int NOT NULL, "
                    + "numDownVotes int NOT NULL)");

            //user table        
            db.mCreateUserTable = db.mConnection.prepareStatement(
                    "CREATE TABLE userData (userID SERIAL PRIMARY KEY, name VARCHAR(50) NOT NULL, "
                    + "password VARCHAR(50) NOT NULL");

            //dislikeData         
            db.mCreateLikeData = db.mConnection.prepareStatement(
                "CREATE TABLE likeData (userID int REFERENCES userdata(msgID), msgID int REFERENCES msgdata(userID), PRIMARY KEY (userID, msgID)");
            //likeData
            db.mCreateDislikeData = db.mConnection.prepareStatement(
                "CREATE TABLE dislikedata (userID int REFERENCES userdata(msgID), msgID int REFERENCES msgdata(userID), PRIMARY KEY (userID, msgID)");

            //comment table
            db.mCreateCommentTable = db.mConnection.prepareStatement(
                    "CREATE TABLE comments (commentID SERIAL PRIMARY KEY, msgID int REFERENCES msgdata(msgId)), text VARCHAR(500) NOT NULL, " +
                            "tStamp timestamp NOT NULL,");



            db.mInsertOneMessage = db.mConnection.prepareStatement("INSERT INTO msgData VALUES (?, ? ,?, ?, ?, ?)");
            db.mInsertOneUser = db.mConnection.prepareStatement("INSERT INTO userData VALUES (?, ?, ?)");
            
            //drop table commands           
            db.mDropMessageTable = db.mConnection.prepareStatement("DROP TABLE msgData");
            db.mDropUserTable = db.mConnection.prepareStatement("DROP TABLE userData");
            db.mDropLikeData = db.mConnection.prepareStatement("DROP TABLE likeData");
            db.mDropDislikeData = db.mConnection.prepareStatement("DROP TABLE dislikeData");

            //delete row commands
            db.mDeleteOneMsg = db.mConnection.prepareStatement("DELETE FROM msgData WHERE msgID = ?");
            db.mDeleteOneUser = db.mConnection.prepareStatement("DELETE FROM userData WHERE userID = ?");

            //select table commands
            db.mSelectAllMsg = db.mConnection.prepareStatement("SELECT * FROM msgData");
            db.mSelectAllUsers = db.mConnection.prepareStatement("SELECT * FROM userData");

            //select one row commands
            db.mSelectOneUserUpvote = db.mConnection.prepareStatement("SELECT * FROM likeData where userID = ?");
            db.mSelectOneUserDownvote = db.mConnection.prepareStatement("SELECT * FROM dislikeData where userID = ?");

            db.mSelectOneMsgUpvote = db.mConnection.prepareStatement("SELECT * FROM likeData where msgID = ?");
            db.mSelectOneMsgDownvote = db.mConnection.prepareStatement("SELECT * FROM dislikeData where msgID = ?");

            // Standard CRUD operations
            db.mDeleteOne = db.mConnection.prepareStatement("DELETE FROM tblData WHERE id = ?");
            db.mInsertOne = db.mConnection.prepareStatement("INSERT INTO tblData VALUES (default, ?, ?)");
            db.mSelectAll = db.mConnection.prepareStatement("SELECT id, subject FROM tblData");
            db.mSelectOne = db.mConnection.prepareStatement("SELECT * from tblData WHERE id=?");
            db.mUpdateOne = db.mConnection.prepareStatement("UPDATE tblData SET message = ? WHERE id = ?");
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Insert a message into msgData
     *  
     * @return The number of rows that were inserted
     */
    int insertMsgRow(int msgID, int userID, String text, java.sql.Timestamp tstamp, int numUpvotes, int numDownvotes) {
        int count = 0;
        try {
            mInsertOneMessage.setInt(1, msgID);
            mInsertOneMessage.setInt(2, userID);
            mInsertOneMessage.setString(3, text);
            mInsertOneMessage.setTimestamp(4, tstamp);
            mInsertOneMessage.setInt(5, numUpvotes);
            mInsertOneMessage.setInt(6, numDownvotes);
            count += mInsertOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }    

    /**
     * Insert a user into userData
     * 
     * @return The number of rows that were inserted
     */
    int insertUserRow(int userID, String name, String password) {
        int count = 0;
        try {
            mInsertOneUser.setInt(1, userID);
            mInsertOneUser.setString(2, name);
            mInsertOneUser.setString(3, password);
            count += mInsertOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
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
                res.add(new RowData(rs.getInt("id"), rs.getString("subject"), null));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Query the database for all messages and corresponding data
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<MsgData> selectAllMsg() {
        ArrayList<MsgData> res = new ArrayList<MsgData>();
        try {
            ResultSet rs = mSelectAllMsg.executeQuery();
            while (rs.next()) {
                res.add(new MsgData(rs.getInt("msgID"), rs.getInt("userID"), rs.getString("text"), 
                rs.getTimestamp("tstamp"), rs.getInt("numUpvotes"), rs.getInt("numDownvotes")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Query the database for a list of all users
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<UserData> selectAllUsers() {
        ArrayList<UserData> res = new ArrayList<UserData>();
        try {
            ResultSet rs = mSelectAllUsers.executeQuery();
            while (rs.next()) {
                res.add(new UserData(rs.getInt("userID"), rs.getString("name"), rs.getString("password")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Query the database for a list of what each person upvoted
     * 
     * @return All rows, as an ArrayList
     */
   
     ArrayList<LikeData> selectAllLikes() {
        ArrayList<LikeData> res = new ArrayList<LikeData>();
        try {
            ResultSet rs = mSelectAllUsers.executeQuery();
            while (rs.next()) {
                res.add(new LikeData(rs.getInt("userID"), rs.getInt("msgID")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Query the database for a list of what each person downvoted
     * 
     * @return All rows, as an ArrayList
     */
   
    ArrayList<DislikeData> selectAllDislikes() {
        ArrayList<DislikeData> res = new ArrayList<DislikeData>();
        try {
            ResultSet rs = mSelectAllUsers.executeQuery();
            while (rs.next()) {
                res.add(new DislikeData(rs.getInt("userID"), rs.getInt("msgID")));
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
     * Get all data for a specific row, by ID
     * 
     * @return The data for the requested row, or null if the ID was invalid
     */
    UserData selectOneUser(int userID) {
        UserData res = null;
        try {
            mSelectOneUser.setInt(1, userID);
            ResultSet rs = mSelectOneUser.executeQuery();
            if (rs.next()) {
                res = new UserData(rs.getInt("userID"), rs.getString("name"), rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Get all data for a specific row, by ID
     * 
     * @return The data for the requested row, or null if the ID was invalid
     */
    MsgData selectOneMsg(int msgID) {
        MsgData res = null;
        try {
            mSelectOneMsg.setInt(1, msgID);
            ResultSet rs = mSelectOneMsg.executeQuery();
            if (rs.next()) {
                res = new MsgData(rs.getInt("msgID"), rs.getInt("userID"), rs.getString("text"), rs.getTimestamp("tstamp"),
                rs.getInt("numUpvotes"), rs.getInt("numDownvotes"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Get all upvote data for a specific user, by ID
     * 
     * @param userID userID to retrieve information from
     * @return The data for the requested user, or null if the ID was invalid
     */
    LikeData selectOneUserUpvote(int userID) {
        LikeData res = null;
        try {
            mSelectOneUserUpvote.setInt(1, userID);
            ResultSet rs = mSelectOneUserUpvote.executeQuery();
            if (rs.next()) {
                res = new LikeData(rs.getInt("userID"), rs.getInt("msgID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    
    /**
     * Get all downvote data for a specific user, by ID
     * 
     * @param userID userID to retrieve information from
     * @return The data for the requested user, or null if the ID was invalid
     */
    DislikeData selectOneUserDownvote(int userID) {
        DislikeData res = null;
        try {
            mSelectOneUserDownvote.setInt(1, userID);
            ResultSet rs = mSelectOneUserDownvote.executeQuery();
            if (rs.next()) {
                res = new DislikeData(rs.getInt("userID"), rs.getInt("msgID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Get all upvote data for a specific message, by ID
     * 
     * @param userID userID to retrieve information from
     * @return The data for the requested user, or null if the ID was invalid
     */
    LikeData selectOneMsgUpvote(int msgID) {
        LikeData res = null;
        try {
            mSelectOneMsgUpvote.setInt(2, msgID);
            ResultSet rs = mSelectOneMsgUpvote.executeQuery();
            if (rs.next()) {
                res = new LikeData(rs.getInt("userID"), rs.getInt("msgID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    
    /**
     * Get all downvote data for a specific message, by ID
     * 
     * @param userID userID to retrieve information from
     * @return The data for the requested user, or null if the ID was invalid
     */
    DislikeData selectOneMsgDownvote(int userID) {
        DislikeData res = null;
        try {
            mSelectOneMsgDownvote.setInt(1, userID);
            ResultSet rs = mSelectOneMsgDownvote.executeQuery();
            if (rs.next()) {
                res = new DislikeData(rs.getInt("userID"), rs.getInt("msgID"));
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
        int res = -1;
        try {
            mDeleteOne.setInt(1, id);
            res = mDeleteOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Delete a row from msgData by msgID
     * 
     * @param msgID The id of the row to delete
     * 
     * @return The number of rows that were deleted.  -1 indicates an error.
     */
    int deleteMsgRow(int msgID) {
        int res = -1;
        try {
            mDeleteOneMsg.setInt(1, msgID);
            res = mDeleteOneMsg.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    
    /**
     * Delete a row from msgData by msgID
     * 
     * @param userID The id of the row to delete
     * 
     * @return The number of rows that were deleted.  -1 indicates an error.
     */
    int deleteUserRow(int userID) {
        int res = -1;
        try {
            mDeleteOneUser.setInt(1, userID);
            res = mDeleteOneUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Update the message for a row in the database
     * 
     * @param id The id of the row to update
     * @param message The new message contents
     * 
     * @return The number of rows that were updated.  -1 indicates an error.
     */
    int updateOne(int id, String message) {
        int res = -1;
        try {
            mUpdateOne.setString(1, message);
            mUpdateOne.setInt(2, id);
            res = mUpdateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Create tblData.  If it already exists, this will print an error
     */

    void createUserTable() {
        try {
            mCreateUserTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void createMessageTable() {
        try {
            mCreateMessageTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void createLikeData() {
        try {
            mCreateLikeData.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void createDislikeData() {
        try {
            mCreateDislikeData.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void createCommentTable(){
        try{
            mCreateCommentTable.execute();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

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

    /**
     * Remove msgData from the database.  If it does not exist, this will print
     * an error.
     */
    void dropMessageTable() {
        try {
            mDropMessageTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove userData from the database.  If it does not exist, this will print
     * an error.
     */
    void dropUserTable() {
        try {
            mDropUserTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove likeData from the database.  If it does not exist, this will print
     * an error.
     */
    void dropLikeData() {
        try {
            mDropLikeData.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove dislikeData from the database.  If it does not exist, this will print
     * an error.
     */
    void mDropDislikeData() {
        try {
            mDropDislikeData.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}