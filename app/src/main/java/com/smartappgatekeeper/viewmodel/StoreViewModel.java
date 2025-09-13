package com.smartappgatekeeper.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.smartappgatekeeper.repository.AppRepository;
import java.util.List;
import java.util.ArrayList;

/**
 * ViewModel for StoreFragment
 * Manages store items, purchases, and user coins
 */
public class StoreViewModel extends AndroidViewModel {
    
    private final AppRepository repository;
    private final MutableLiveData<List<StoreItem>> storeItems = new MutableLiveData<>();
    private final MutableLiveData<Integer> userCoins = new MutableLiveData<>();
    private final MutableLiveData<List<Purchase>> purchaseHistory = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    
    public StoreViewModel(Application application) {
        super(application);
        repository = AppRepository.getInstance(application);
        loadData();
    }
    
    /**
     * Load all store data
     */
    private void loadData() {
        loadStoreItems();
        loadUserCoins();
        loadPurchaseHistory();
    }
    
    /**
     * Load store items
     */
    private void loadStoreItems() {
        List<StoreItem> items = new ArrayList<>();
        
        // Themes
        items.add(new StoreItem("Dark Theme", "Unlock dark mode theme", 100, "theme", false));
        items.add(new StoreItem("Colorful Theme", "Unlock colorful theme", 150, "theme", false));
        items.add(new StoreItem("Minimal Theme", "Unlock minimal theme", 200, "theme", false));
        
        // Avatars
        items.add(new StoreItem("Wise Owl", "Unlock wise owl avatar", 50, "avatar", false));
        items.add(new StoreItem("Smart Fox", "Unlock smart fox avatar", 75, "avatar", false));
        items.add(new StoreItem("Focused Eagle", "Unlock focused eagle avatar", 100, "avatar", false));
        
        // Power-ups
        items.add(new StoreItem("Extra Time", "Get 5 extra minutes for quiz", 25, "powerup", false));
        items.add(new StoreItem("Hint Pack", "Get 3 hints for difficult questions", 30, "powerup", false));
        items.add(new StoreItem("Streak Saver", "Protect your streak for one day", 50, "powerup", false));
        
        // Premium features
        items.add(new StoreItem("Advanced Analytics", "Unlock detailed progress analytics", 500, "premium", false));
        items.add(new StoreItem("Custom Questions", "Add your own quiz questions", 300, "premium", false));
        items.add(new StoreItem("Priority Support", "Get priority customer support", 200, "premium", false));
        
        storeItems.setValue(items);
    }
    
    /**
     * Load user coins
     */
    private void loadUserCoins() {
        repository.getTotalCoins().observeForever(coins -> {
            if (coins != null) {
                userCoins.setValue(coins);
            } else {
                userCoins.setValue(0);
            }
        });
    }
    
    /**
     * Load purchase history
     */
    private void loadPurchaseHistory() {
        // Mock purchase history
        List<Purchase> purchases = new ArrayList<>();
        purchases.add(new Purchase("Dark Theme", 100, System.currentTimeMillis() - 86400000, true));
        purchases.add(new Purchase("Wise Owl", 50, System.currentTimeMillis() - 172800000, true));
        
        purchaseHistory.setValue(purchases);
    }
    
    /**
     * Purchase an item
     */
    public void purchaseItem(StoreItem item) {
        Integer currentCoins = userCoins.getValue();
        if (currentCoins != null && currentCoins >= item.getPrice()) {
            // Deduct coins
            int newCoins = currentCoins - item.getPrice();
            userCoins.setValue(newCoins);
            
            // Mark item as purchased
            item.setPurchased(true);
            
            // Add to purchase history
            List<Purchase> purchases = purchaseHistory.getValue();
            if (purchases != null) {
                purchases.add(new Purchase(item.getName(), item.getPrice(), System.currentTimeMillis(), true));
                purchaseHistory.setValue(purchases);
            }
            
            // Update store items
            List<StoreItem> items = storeItems.getValue();
            if (items != null) {
                for (StoreItem storeItem : items) {
                    if (storeItem.getName().equals(item.getName())) {
                        storeItem.setPurchased(true);
                        break;
                    }
                }
                storeItems.setValue(items);
            }
        } else {
            errorMessage.setValue("Not enough coins! You need " + item.getPrice() + " coins.");
        }
    }
    
    /**
     * Refresh store data
     */
    public void refreshData() {
        loadData();
    }
    
    // Getters
    public LiveData<List<StoreItem>> getStoreItems() {
        return storeItems;
    }
    
    public LiveData<Integer> getUserCoins() {
        return userCoins;
    }
    
    public LiveData<List<Purchase>> getPurchaseHistory() {
        return purchaseHistory;
    }
    
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    
    /**
     * Store item data class
     */
    public static class StoreItem {
        private String name;
        private String description;
        private int price;
        private String category;
        private boolean isPurchased;
        
        public StoreItem(String name, String description, int price, String category, boolean isPurchased) {
            this.name = name;
            this.description = description;
            this.price = price;
            this.category = category;
            this.isPurchased = isPurchased;
        }
        
        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public int getPrice() { return price; }
        public void setPrice(int price) { this.price = price; }
        
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        
        public boolean isPurchased() { return isPurchased; }
        public void setPurchased(boolean purchased) { isPurchased = purchased; }
    }
    
    /**
     * Purchase data class
     */
    public static class Purchase {
        private String itemName;
        private int price;
        private long purchaseTime;
        private boolean isSuccessful;
        
        public Purchase(String itemName, int price, long purchaseTime, boolean isSuccessful) {
            this.itemName = itemName;
            this.price = price;
            this.purchaseTime = purchaseTime;
            this.isSuccessful = isSuccessful;
        }
        
        // Getters and setters
        public String getItemName() { return itemName; }
        public void setItemName(String itemName) { this.itemName = itemName; }
        
        public int getPrice() { return price; }
        public void setPrice(int price) { this.price = price; }
        
        public long getPurchaseTime() { return purchaseTime; }
        public void setPurchaseTime(long purchaseTime) { this.purchaseTime = purchaseTime; }
        
        public boolean isSuccessful() { return isSuccessful; }
        public void setSuccessful(boolean successful) { isSuccessful = successful; }
    }
}
