package com.smartappgatekeeper.network.models;

import com.google.gson.annotations.SerializedName;

public class PurchaseRequest {
    @SerializedName("item_id")
    private String itemId;
    
    @SerializedName("quantity")
    private int quantity;

    // Constructors
    public PurchaseRequest() {}

    public PurchaseRequest(String itemId, int quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
