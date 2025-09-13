package com.smartappgatekeeper.network.models;

import com.google.gson.annotations.SerializedName;

public class SupportTicketRequest {
    @SerializedName("subject")
    private String subject;
    
    @SerializedName("description")
    private String description;
    
    @SerializedName("category")
    private String category;
    
    @SerializedName("priority")
    private String priority;

    // Constructors
    public SupportTicketRequest() {}

    public SupportTicketRequest(String subject, String description, String category, String priority) {
        this.subject = subject;
        this.description = description;
        this.category = category;
        this.priority = priority;
    }

    // Getters and Setters
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
}
