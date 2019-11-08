package edu.lehigh.cse216.phase0;
import java.util.ArrayList;

class MessageInfo {
    private final static int NOT_SET = -1;

    private int msgNum;
    private String sender;
    private String msg;
    private ArrayList<String> comments;
    private int upvotes;
    private int downvotes;

    MessageInfo(int msgNum, String sender, String msg, ArrayList<String> comments, int numUpvotes, int numDownvotes) {
        this.msgNum = msgNum;
        this.sender = sender;
        this.msg = msg;
        this.comments = comments;
        this.upvotes = numUpvotes;
        this.downvotes = numDownvotes;
    }

    //Constructor for creating object for msgs user sent so you don't have to go to server
    MessageInfo(String sender, String msg) {
        this(NOT_SET, sender, msg, new ArrayList<String>(), 0, 0);
    }

    public void addUpvote() {
        upvotes++;
    }
    public void remUpvote() {
        upvotes--;
    }
    public void addDownvote() {
        downvotes++;
    }
    public void remDownvote() {
        downvotes--;
    }

    public int msgNum() { return msgNum; }
    public String sender() { return sender; }
    public String msg() { return msg; }
    public ArrayList<String> comments() { return comments; }
    public int upvotes() { return upvotes; }
    public int downvotes() { return downvotes; }

    public void msgNum(int num) {
        this.msgNum = num;
    }
}
