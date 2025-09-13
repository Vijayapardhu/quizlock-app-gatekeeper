package com.smartappgatekeeper.network.models;

import com.google.gson.annotations.SerializedName;

public class SyncDataResponse {
    @SerializedName("success")
    private boolean success;
    
    @SerializedName("message")
    private String message;
    
    @SerializedName("user_data")
    private String userData;
    
    @SerializedName("timestamp")
    private long timestamp;

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getUserData() { return userData; }
    public void setUserData(String userData) { this.userData = userData; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
