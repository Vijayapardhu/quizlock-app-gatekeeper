package com.smartappgatekeeper.network.models;

import com.google.gson.annotations.SerializedName;

public class SupportTicketResponse {
    @SerializedName("success")
    private boolean success;
    
    @SerializedName("message")
    private String message;
    
    @SerializedName("ticket_id")
    private String ticketId;
    
    @SerializedName("status")
    private String status;

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getTicketId() { return ticketId; }
    public void setTicketId(String ticketId) { this.ticketId = ticketId; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
