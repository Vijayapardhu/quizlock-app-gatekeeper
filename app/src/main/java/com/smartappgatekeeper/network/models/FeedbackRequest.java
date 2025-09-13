package com.smartappgatekeeper.network.models;

import com.google.gson.annotations.SerializedName;

public class FeedbackRequest {
    @SerializedName("rating")
    private int rating;
    
    @SerializedName("comment")
    private String comment;
    
    @SerializedName("category")
    private String category;

    // Constructors
    public FeedbackRequest() {}

    public FeedbackRequest(int rating, String comment, String category) {
        this.rating = rating;
        this.comment = comment;
        this.category = category;
    }

    // Getters and Setters
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
