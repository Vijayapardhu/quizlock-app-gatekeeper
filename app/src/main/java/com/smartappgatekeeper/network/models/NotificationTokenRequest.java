package com.smartappgatekeeper.network.models;

import com.google.gson.annotations.SerializedName;

public class NotificationTokenRequest {
    @SerializedName("token")
    private String token;
    
    @SerializedName("device_type")
    private String deviceType;

    // Constructors
    public NotificationTokenRequest() {}

    public NotificationTokenRequest(String token, String deviceType) {
        this.token = token;
        this.deviceType = deviceType;
    }

    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }
}
