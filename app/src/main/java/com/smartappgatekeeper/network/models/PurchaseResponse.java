package com.smartappgatekeeper.network.models;

import com.google.gson.annotations.SerializedName;

public class PurchaseResponse {
    @SerializedName("success")
    private boolean success;
    
    @SerializedName("message")
    private String message;
    
    @SerializedName("purchase_id")
    private String purchaseId;
    
    @SerializedName("coins_remaining")
    private int coinsRemaining;

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getPurchaseId() { return purchaseId; }
    public void setPurchaseId(String purchaseId) { this.purchaseId = purchaseId; }
    
    public int getCoinsRemaining() { return coinsRemaining; }
    public void setCoinsRemaining(int coinsRemaining) { this.coinsRemaining = coinsRemaining; }
}
