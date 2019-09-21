package edu.lehigh.cse216.phase0;


class DataFromVolley {
    private final int msgNum;
    private final String sender;
    private final String msg;
    private final int numUpvotes;
    private final int numDownvotes;

    DataFromVolley(int msgNum, String sender, String msg, int numUpvotes, int numDownvotes) {
        this.msgNum = msgNum;
        this.sender = sender;
        this.msg = msg;
        this.numUpvotes = numUpvotes;
        this.numDownvotes = numDownvotes;
    }

    public int msgNum() { return msgNum; }
    public String sender() { return sender; }
    public String msg() { return msg; }
    public int numUpVotes() { return numUpvotes; }
    public int numDownvotes() { return numDownvotes; }
}
