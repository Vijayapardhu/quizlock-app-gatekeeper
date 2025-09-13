package com.smartappgatekeeper.network.models;

import com.google.gson.annotations.SerializedName;

public class RefreshTokenRequest {
    @SerializedName("refresh_token")
    private String refreshToken;
    
    @SerializedName("device_id")
    private String deviceId;

    // Constructors
    public RefreshTokenRequest() {}

    public RefreshTokenRequest(String refreshToken, String deviceId) {
        this.refreshToken = refreshToken;
        this.deviceId = deviceId;
    }

    // Getters and Setters
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
