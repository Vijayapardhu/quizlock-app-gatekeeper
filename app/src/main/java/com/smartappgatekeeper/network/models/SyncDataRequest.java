package com.smartappgatekeeper.network.models;

import com.google.gson.annotations.SerializedName;

public class SyncDataRequest {
    @SerializedName("user_data")
    private String userData;
    
    @SerializedName("timestamp")
    private long timestamp;
    
    @SerializedName("device_id")
    private String deviceId;

    // Constructors
    public SyncDataRequest() {}

    public SyncDataRequest(String userData, long timestamp, String deviceId) {
        this.userData = userData;
        this.timestamp = timestamp;
        this.deviceId = deviceId;
    }

    // Getters and Setters
    public String getUserData() { return userData; }
    public void setUserData(String userData) { this.userData = userData; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
}
