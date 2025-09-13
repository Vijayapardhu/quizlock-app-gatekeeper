package com.smartappgatekeeper.network.models;

import com.google.gson.annotations.SerializedName;

public class AchievementResponse {
    @SerializedName("id")
    private String id;
    
    @SerializedName("name")
    private String name;
    
    @SerializedName("description")
    private String description;
    
    @SerializedName("icon")
    private String icon;
    
    @SerializedName("points")
    private int points;
    
    @SerializedName("is_claimed")
    private boolean isClaimed;
    
    @SerializedName("is_unlocked")
    private boolean isUnlocked;
    
    @SerializedName("unlocked_date")
    private String unlockedDate;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    
    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }
    
    public boolean isClaimed() { return isClaimed; }
    public void setClaimed(boolean claimed) { isClaimed = claimed; }
    
    public boolean isUnlocked() { return isUnlocked; }
    public void setUnlocked(boolean unlocked) { isUnlocked = unlocked; }
    
    public String getUnlockedDate() { return unlockedDate; }
    public void setUnlockedDate(String unlockedDate) { this.unlockedDate = unlockedDate; }
}
