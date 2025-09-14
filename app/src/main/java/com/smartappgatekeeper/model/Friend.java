package com.smartappgatekeeper.model;

/**
 * Friend model class
 * Represents a friend in the app
 */
public class Friend {
    private String id;
    private String name;
    private String email;
    private String level;
    private int coins;
    private boolean isAdded;
    
    public Friend(String id, String name, String email, String level, int coins, boolean isAdded) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.level = level;
        this.coins = coins;
        this.isAdded = isAdded;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getLevel() {
        return level;
    }
    
    public void setLevel(String level) {
        this.level = level;
    }
    
    public int getCoins() {
        return coins;
    }
    
    public void setCoins(int coins) {
        this.coins = coins;
    }
    
    public boolean isAdded() {
        return isAdded;
    }
    
    public void setAdded(boolean added) {
        isAdded = added;
    }
}
