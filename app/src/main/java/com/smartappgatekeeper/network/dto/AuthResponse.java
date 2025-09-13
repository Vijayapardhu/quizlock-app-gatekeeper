package com.smartappgatekeeper.network.dto;

/**
 * Authentication response DTO
 */
public class AuthResponse {
    private String token;
    private String refreshToken;
    private long expiresIn;
    private String userId;
    private String message;
    private boolean success;
    
    public AuthResponse() {}
    
    public AuthResponse(String token, String refreshToken, long expiresIn, String userId, String message, boolean success) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.userId = userId;
        this.message = message;
        this.success = success;
    }
    
    // Getters and setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    
    public long getExpiresIn() { return expiresIn; }
    public void setExpiresIn(long expiresIn) { this.expiresIn = expiresIn; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
}
