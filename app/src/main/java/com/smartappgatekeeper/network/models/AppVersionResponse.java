package com.smartappgatekeeper.network.models;

import com.google.gson.annotations.SerializedName;

public class AppVersionResponse {
    @SerializedName("success")
    private boolean success;
    
    @SerializedName("message")
    private String message;
    
    @SerializedName("version")
    private String version;
    
    @SerializedName("build_number")
    private int buildNumber;
    
    @SerializedName("update_available")
    private boolean updateAvailable;
    
    @SerializedName("download_url")
    private String downloadUrl;

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    public int getBuildNumber() { return buildNumber; }
    public void setBuildNumber(int buildNumber) { this.buildNumber = buildNumber; }
    
    public boolean isUpdateAvailable() { return updateAvailable; }
    public void setUpdateAvailable(boolean updateAvailable) { this.updateAvailable = updateAvailable; }
    
    public String getDownloadUrl() { return downloadUrl; }
    public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }
}
