package edu.lehigh.cse216.phase0;

class CommentInfo {
    private final static int NOT_SET = -1;

    private int commentId;
    private int msgId;
    private String msg;

    CommentInfo(int commentId, int msgId, String msg) {
        this.commentId = commentId;
        this.msgId = msgId;
        this.msg = msg;
    }

    //Constructor for creating object for msgs user sent so you don't have to go to server
    CommentInfo(int msgId, String msg) {
        this(NOT_SET, msgId, msg);
    }

    public int commentId() { return commentId; }
    public int msgId() { return msgId; }
    public String msg() { return msg; }
}
