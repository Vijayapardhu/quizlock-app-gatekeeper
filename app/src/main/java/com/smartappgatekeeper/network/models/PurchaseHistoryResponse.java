package com.smartappgatekeeper.network.models;

import com.google.gson.annotations.SerializedName;

public class PurchaseHistoryResponse {
    @SerializedName("id")
    private String id;
    
    @SerializedName("item_name")
    private String itemName;
    
    @SerializedName("price")
    private int price;
    
    @SerializedName("purchase_date")
    private String purchaseDate;
    
    @SerializedName("status")
    private String status;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
    
    public String getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(String purchaseDate) { this.purchaseDate = purchaseDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
