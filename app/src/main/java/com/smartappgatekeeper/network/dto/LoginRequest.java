package com.smartappgatekeeper.network.dto;

/**
 * Login request DTO
 */
public class LoginRequest {
    private String email;
    private String password;
    private String deviceId;
    private String deviceType;
    
    public LoginRequest() {}
    
    public LoginRequest(String email, String password, String deviceId, String deviceType) {
        this.email = email;
        this.password = password;
        this.deviceId = deviceId;
        this.deviceType = deviceType;
    }
    
    // Getters and setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    
    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }
}
