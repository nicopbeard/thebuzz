package edu.lehigh.cse216.kta221.backend;

/**
 * SimpleRequest provides a format for clients to present title and message 
 * strings to the server.
 * 
 * NB: since this will be created from JSON, all fields must be public, and we
 *     do not need a constructor.
 */
public class MessageRequest {

    public int senderId;

    public String text;

    public int nUpVotes;

    public int nDownVotes;
}