package edu.lehigh.cse216.kta221.admin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Map;

/**
 * App is our basic admin app.  For now, it is a demonstration of the six key 
 * operations on a database: connect, insert, update, query, delete, disconnect
 */
public class App {

    /**
     * Print the menu for our program
     */
    static void menu() {
        System.out.println("Main Menu");
        System.out.println("  [T] Create a table");
        System.out.println("  [D] Drop a table");
        System.out.println("  [U] Query for a specific user");
        System.out.println("  [M] Query for a specific message");
        System.out.println("  [*] Query for all rows of a table");
        System.out.println("  [-] Delete a row");
        System.out.println("  [+] Insert a new row");
        System.out.println("  [~] Update a row");
        System.out.println("  [q] Quit Program");
        System.out.println("  [?] Help (this message)");
    }

    /**
     * Ask the user to enter a menu option; repeat until we get a valid option
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * 
     * @return The character corresponding to the chosen menu option
     */
    static char prompt(BufferedReader in) {
        // The valid actions:
        String actions = "TD1*-+~q?";

        // We repeat until a valid single-character option is selected        
        while (true) {
            System.out.print("[" + actions + "] :> ");
            String action;
            try {
                action = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            if (action.length() != 1)
                continue;
            if (actions.contains(action)) {
                return action.charAt(0);
            }
            System.out.println("Invalid Command");
        }
    }

    /**
     * Ask the user to enter a String message
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * 
     * @return The string that the user provided.  May be "".
     */
    static String getString(BufferedReader in, String message) {
        String s;
        try {
            System.out.print(message + " :> ");
            s = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return s;
    }

    /**
     * Ask the user to enter an integer
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * 
     * @return The integer that the user provided.  On error, it will be -1
     */
    static int getInt(BufferedReader in, String message) {
        int i = -1;
        try {
            System.out.print(message + " :> ");
            i = Integer.parseInt(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * The main routine runs a loop that gets a request from the user and
     * processes it
     * 
     * @param argv Command-line options.  Ignored by this program.
     */
    public static void main(String[] argv) {
        // get the Postgres configuration from the environment
        Map<String, String> env = System.getenv();
        String ip = env.get("POSTGRES_IP");
        String port = env.get("POSTGRES_PORT");
        String user = env.get("POSTGRES_USER");
        String pass = env.get("POSTGRES_PASS");
        
        // Get a fully-configured connection to the database, or exit 
        // immediately
        Database db = Database.getDatabase(ip, port, user, pass);
        if (db == null)
            return;

        // Start our basic command-line interpreter:
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            // Get the user's request, and do it
            //
            // NB: for better testability, each action should be a separate
            //     function call
            char action = prompt(in);
            if (action == '?') {
                menu();
            } else if (action == 'q') {
                break;
            } else if (action == 'T') {
                String id = "";
                while (!(id.equals("U") && !(id.equals("M") && !(id.equals("L") && !(id.equals("D"))))))
                {
                    id = getString(in, "Enter the table you would like to make (U for user, M for msg, L for likes, D for dislikes");
                }
                if (id.equals("U")) {
                    db.createUserTable();
                } else if (id.equals("M")) {
                    db.createMessageTable();
                } else if (id.equals("L")) {
                    db.createLikeData();
                } else if (id.equals("D")) {
                    db.createDislikeData();
                }
            } else if (action == 'D') {
                String id = "";
                while (!(id.equals("U") && !(id.equals("M") && !(id.equals("L") && !(id.equals("D"))))))
                {
                    id = getString(in, "Enter the table you would like to drop (U for user, M for msg, L for likes, D for dislikes");
                }
                if (id.equals("U")) {
                    db.dropUserTable();
                } else if (id.equals("M")) {
                    db.dropMessageTable();
                } else if (id.equals("L")) {
                    db.dropLikeData();
                } else if (id.equals("D")) {
                    db.mDropDislikeData();
                }
            } else if (action == 'U') {
                int id = getInt(in, "Enter the user ID");
                if (id == -1)
                    continue;
                Database.UserData res = db.selectOneUser(id);
                if (res != null) {
                    System.out.println(" [" + res.mUserID + "] " + res.mName + " " + res.mPassword);
                }
            } else if (action == 'M') {
                int id = getInt(in, "Enter the message ID");
                if (id == -1)
                    continue;
                Database.MsgData res = db.selectOneMsg(id);
                if (res != null) {
                    System.out.println(" [" + res.mMsgID + "] " + res.mText + " " + res.mTstamp + " " + res.mNumUpvotes + " "
                     + res.mNumDownvotes);
                }
            } else if (action == '1') {
                int id = getInt(in, "Enter the row ID");
                if (id == -1)
                    continue;
                Database.RowData res = db.selectOne(id);
                if (res != null) {
                    System.out.println("  [" + res.mId + "] " + res.mSubject);
                    System.out.println("  --> " + res.mMessage);
                }
            } else if (action == '*') {
                String id = "";
                while (!(id.equals("U") && !(id.equals("M") && !(id.equals("L") && !(id.equals("D"))))))
                {
                    id = getString(in, "Enter the table you would like to query (U for user, M for msg, L for likes, D for dislikes");
                }
                if (id.equals("U")) {
                    ArrayList<Database.UserData> res = db.selectAllUsers();
                    if (res == null)
                        continue;
                    System.out.println(" Current User Contents");
                    System.out.println(" ---------------------");
                    for (Database.UserData rd : res) {
                        System.out.println(" [" + rd.mUserID + "] " + rd.mName + " " + rd.mPassword);
                    }
                } else if (id.equals("M")) {
                    ArrayList<Database.MsgData> res = db.selectAllMsg();
                    if (res == null)
                        continue;
                    System.out.println(" Current Message Contents");
                    System.out.println(" ------------------------");
                    for (Database.MsgData rd : res) {
                        System.out.println(" [" + rd.mMsgID + "] " + rd.mUserID + " " + rd.mText + " " + rd.mTstamp + " " + 
                            rd.mNumUpvotes + " " + rd.mNumDownvotes);
                    }
                } else if (id.equals("L")) {
                    ArrayList<Database.LikeData> res = db.selectAllLikes();
                    if (res == null)
                        continue;
                    System.out.println(" Current Upvote Contents");
                    System.out.println(" -----------------------");
                    for (Database.LikeData rd : res) {
                        System.out.println(" [" + rd.mUserID + "] " + rd.mMsgID);
                    }
                } else if (id.equals("D")) {
                    ArrayList<Database.DislikeData> res = db.selectAllDislikes();
                    if (res == null)
                        continue;
                    System.out.println(" Current Downvote Contents");
                    System.out.println(" -------------------------");
                    for (Database.DislikeData rd : res) {
                        System.out.println(" [" + rd.mUserID + "] " + rd.mMsgID);
                    }
                }
            } else if (action == '*') {
                ArrayList<Database.RowData> res = db.selectAll();
                if (res == null)
                    continue;
                System.out.println("  Current Database Contents");
                System.out.println("  -------------------------");
                for (Database.RowData rd : res) {
                    System.out.println("  [" + rd.mId + "] " + rd.mSubject);
                }
            } else if (action == '-') {
                int id = getInt(in, "Enter the row ID");
                if (id == -1)
                    continue;
                int res = db.deleteRow(id);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows deleted");
            } else if (action == '+') {
                String subject = getString(in, "Enter the subject");
                String message = getString(in, "Enter the message");
                if (subject.equals("") || message.equals(""))
                    continue;
                int res = db.insertRow(subject, message);
                System.out.println(res + " rows added");
            } else if (action == '~') {
                int id = getInt(in, "Enter the row ID :> ");
                if (id == -1)
                    continue;
                String newMessage = getString(in, "Enter the new message");
                int res = db.updateOne(id, newMessage);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows updated");
            }
        }
        // Always remember to disconnect from the database when the program 
        // exits
        db.disconnect();
    }
}