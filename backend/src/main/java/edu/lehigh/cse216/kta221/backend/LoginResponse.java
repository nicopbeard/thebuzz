package edu.lehigh.cse216.kta221.backend;

public class LoginResponse {
    public String userId;
    public String sessionToken;

    public LoginResponse(String userId, String sessionToken) {
        this.userId = userId;
        this.sessionToken = sessionToken;
    }
}
