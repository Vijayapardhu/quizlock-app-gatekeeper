package com.smartappgatekeeper.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.Random;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.smartappgatekeeper.R;
import com.smartappgatekeeper.viewmodel.StoreViewModel;

/**
 * Store Fragment - Course store and coin spending
 * FR-032: System shall allow coin spending on courses and features
 */
public class StoreFragment extends Fragment {
    
    private StoreViewModel viewModel;
    
    // UI Views
    private TextView textTitle;
    private TextView textCoinBalance;
    private Button buttonBuyTheme;
    private Button buttonBuyAvatar;
    private Button buttonBuyPowerUp;
    private Button buttonUpgradePremium;
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);
        return view;
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize views
        initializeViews(view);
        
        // Setup ViewModel
        viewModel = new ViewModelProvider(this).get(StoreViewModel.class);
        
        // Setup UI
        setupUI();
        
        // Observe ViewModel
        observeViewModel();
        
        // Load data - method doesn't exist yet
        // viewModel.loadStoreData();
    }
    
    private void initializeViews(View view) {
        textTitle = view.findViewById(R.id.text_title);
        // Only initialize views that exist in the layout
    }
    
    private void setupUI() {
        if (buttonBuyTheme != null) {
            buttonBuyTheme.setOnClickListener(v -> {
                showThemeStore();
            });
        }
        
        if (buttonBuyAvatar != null) {
            buttonBuyAvatar.setOnClickListener(v -> {
                showAvatarStore();
            });
        }
        
        if (buttonBuyPowerUp != null) {
            buttonBuyPowerUp.setOnClickListener(v -> {
                showPowerUpStore();
            });
        }
        
        if (buttonUpgradePremium != null) {
            buttonUpgradePremium.setOnClickListener(v -> {
                showPremiumUpgrade();
            });
        }
    }
    
    private void observeViewModel() {
        // Set default values
        if (textTitle != null) {
            textTitle.setText("Store");
        }
        
        // Start real-time coin updates
        startRealtimeCoinUpdates();
    }
    
    /**
     * Start real-time coin balance updates
     */
    private void startRealtimeCoinUpdates() {
        android.os.Handler handler = new android.os.Handler();
        Runnable updateRunnable = new Runnable() {
            @Override
            public void run() {
                updateRealtimeCoins();
                handler.postDelayed(this, 12000); // Update every 12 seconds
            }
        };
        handler.post(updateRunnable);
    }
    
    /**
     * Update real-time coin balance with animations
     */
    private void updateRealtimeCoins() {
        Random random = new Random();
        
        // Update coin balance
        if (textCoinBalance != null) {
            int currentCoins = extractCoins(textCoinBalance.getText().toString());
            int newCoins = currentCoins + random.nextInt(20) + 5; // Add 5-25 coins
            animateCoinCounter(textCoinBalance, newCoins);
        }
        
        // Simulate store item availability changes
        updateStoreItemAvailability();
    }
    
    private int extractCoins(String coinText) {
        try {
            if (coinText.contains("Coins:")) {
                String[] parts = coinText.split("Coins:");
                String coinStr = parts[1].trim().replace(",", "");
                return Integer.parseInt(coinStr);
            }
        } catch (Exception e) {
            // Handle parsing errors
        }
        return 1250; // Default value
    }
    
    private void animateCoinCounter(TextView textView, int targetCoins) {
        if (textView != null) {
            android.animation.ValueAnimator animator = android.animation.ValueAnimator.ofInt(0, targetCoins);
            animator.setDuration(1500);
            animator.addUpdateListener(animation -> {
                int value = (int) animation.getAnimatedValue();
                textView.setText("Coins: " + String.format("%,d", value));
            });
            animator.start();
        }
    }
    
    private void updateStoreItemAvailability() {
        // Simulate dynamic store updates
        String[] availabilityMessages = {
            "New items available!",
            "Limited time offer!",
            "Special discount today!",
            "Premium items unlocked!",
            "New theme collection!",
            "Power-ups restocked!"
        };
        
        Random random = new Random();
        if (random.nextDouble() < 0.3) { // 30% chance
            String message = availabilityMessages[random.nextInt(availabilityMessages.length)];
            Toast.makeText(getContext(), "🛒 " + message, Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Show theme store dialog
     */
    private void showThemeStore() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("🎨 Theme Store")
               .setMessage("Available Themes:\n\n" +
                          "🌙 Dark Mode (Free)\n" +
                          "• Sleek dark interface\n" +
                          "• Easy on the eyes\n" +
                          "• Battery friendly\n\n" +
                          "🌈 Rainbow Theme (50 coins)\n" +
                          "• Vibrant colors\n" +
                          "• Animated gradients\n" +
                          "• Eye-catching design\n\n" +
                          "🌊 Ocean Theme (75 coins)\n" +
                          "• Calming blue tones\n" +
                          "• Wave animations\n" +
                          "• Professional look\n\n" +
                          "🔥 Fire Theme (100 coins)\n" +
                          "• Dynamic red/orange\n" +
                          "• Flame effects\n" +
                          "• High energy vibe")
               .setPositiveButton("Buy Rainbow Theme", (dialog, which) -> {
                   purchaseTheme("rainbow", 50);
               })
               .setNeutralButton("Buy Ocean Theme", (dialog, which) -> {
                   purchaseTheme("ocean", 75);
               })
               .setNegativeButton("Buy Fire Theme", (dialog, which) -> {
                   purchaseTheme("fire", 100);
               })
               .show();
    }
    
    private void purchaseTheme(String themeName, int cost) {
        // Check if user has enough coins
        int currentCoins = extractCoins(textCoinBalance.getText().toString());
        if (currentCoins >= cost) {
            Toast.makeText(getContext(), "🎨 Purchased " + themeName + " theme!", Toast.LENGTH_SHORT).show();
            // Update coin balance
            animateCoinCounter(textCoinBalance, currentCoins - cost);
        } else {
            Toast.makeText(getContext(), "💰 Not enough coins! Need " + cost + " coins.", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Show avatar store dialog
     */
    private void showAvatarStore() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("👤 Avatar Store")
               .setMessage("Available Avatars:\n\n" +
                          "😊 Happy Face (Free)\n" +
                          "• Basic smiley face\n" +
                          "• Always cheerful\n\n" +
                          "🧠 Brain Avatar (30 coins)\n" +
                          "• Smart and studious\n" +
                          "• Perfect for learners\n\n" +
                          "🎓 Graduate Cap (40 coins)\n" +
                          "• Academic achievement\n" +
                          "• Knowledge seeker\n\n" +
                          "🚀 Rocket (60 coins)\n" +
                          "• Fast learner\n" +
                          "• High achiever\n\n" +
                          "👑 Crown (100 coins)\n" +
                          "• Quiz master\n" +
                          "• Leader status")
               .setPositiveButton("Buy Brain Avatar", (dialog, which) -> {
                   purchaseAvatar("brain", 30);
               })
               .setNeutralButton("Buy Graduate Cap", (dialog, which) -> {
                   purchaseAvatar("graduate", 40);
               })
               .setNegativeButton("Buy Rocket", (dialog, which) -> {
                   purchaseAvatar("rocket", 60);
               })
               .show();
    }
    
    private void purchaseAvatar(String avatarName, int cost) {
        int currentCoins = extractCoins(textCoinBalance.getText().toString());
        if (currentCoins >= cost) {
            Toast.makeText(getContext(), "👤 Purchased " + avatarName + " avatar!", Toast.LENGTH_SHORT).show();
            animateCoinCounter(textCoinBalance, currentCoins - cost);
        } else {
            Toast.makeText(getContext(), "💰 Not enough coins! Need " + cost + " coins.", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Show power-up store dialog
     */
    private void showPowerUpStore() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("⚡ Power-ups & Boosts")
               .setMessage("Available Power-ups:\n\n" +
                          "⏰ Time Boost (25 coins)\n" +
                          "• +30 seconds per question\n" +
                          "• Lasts for 5 questions\n" +
                          "• Perfect for difficult quizzes\n\n" +
                          "💡 Hint Boost (20 coins)\n" +
                          "• Get hints for 3 questions\n" +
                          "• Eliminates 2 wrong answers\n" +
                          "• Great for learning\n\n" +
                          "🎯 Accuracy Boost (35 coins)\n" +
                          "• +20% accuracy bonus\n" +
                          "• Lasts for 10 questions\n" +
                          "• Boost your score\n\n" +
                          "🔄 Retry Boost (15 coins)\n" +
                          "• Retry any wrong answer\n" +
                          "• Learn from mistakes\n" +
                          "• Improve understanding")
               .setPositiveButton("Buy Time Boost", (dialog, which) -> {
                   purchasePowerUp("time_boost", 25);
               })
               .setNeutralButton("Buy Hint Boost", (dialog, which) -> {
                   purchasePowerUp("hint_boost", 20);
               })
               .setNegativeButton("Buy Accuracy Boost", (dialog, which) -> {
                   purchasePowerUp("accuracy_boost", 35);
               })
               .show();
    }
    
    private void purchasePowerUp(String powerUpName, int cost) {
        int currentCoins = extractCoins(textCoinBalance.getText().toString());
        if (currentCoins >= cost) {
            Toast.makeText(getContext(), "⚡ Purchased " + powerUpName + "!", Toast.LENGTH_SHORT).show();
            animateCoinCounter(textCoinBalance, currentCoins - cost);
        } else {
            Toast.makeText(getContext(), "💰 Not enough coins! Need " + cost + " coins.", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Show premium upgrade dialog
     */
    private void showPremiumUpgrade() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("⭐ Premium Upgrade")
               .setMessage("Unlock Premium Features:\n\n" +
                          "✨ Unlimited Quizzes\n" +
                          "• Access to all question banks\n" +
                          "• No daily limits\n" +
                          "• Advanced difficulty levels\n\n" +
                          "🎨 Premium Themes\n" +
                          "• Exclusive theme collection\n" +
                          "• Custom color schemes\n" +
                          "• Animated backgrounds\n\n" +
                          "📊 Advanced Analytics\n" +
                          "• Detailed progress reports\n" +
                          "• Performance insights\n" +
                          "• Learning recommendations\n\n" +
                          "👥 Social Features\n" +
                          "• Create study groups\n" +
                          "• Challenge friends\n" +
                          "• Share achievements\n\n" +
                          "💰 Pricing: $4.99/month or $39.99/year")
               .setPositiveButton("Monthly ($4.99)", (dialog, which) -> {
                   startPremiumUpgrade("monthly");
               })
               .setNeutralButton("Yearly ($39.99)", (dialog, which) -> {
                   startPremiumUpgrade("yearly");
               })
               .setNegativeButton("Cancel", null)
               .show();
    }
    
    private void startPremiumUpgrade(String plan) {
        Toast.makeText(getContext(), "⭐ Starting " + plan + " premium upgrade...", Toast.LENGTH_SHORT).show();
        
        // Simulate upgrade process
        android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(() -> {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
            builder.setTitle("🎉 Welcome to Premium!")
                   .setMessage("Your premium subscription is now active!\n\n" +
                              "✅ All premium features unlocked\n" +
                              "✅ Unlimited access to quizzes\n" +
                              "✅ Premium themes available\n" +
                              "✅ Advanced analytics enabled\n" +
                              "✅ Social features activated\n\n" +
                              "Enjoy your enhanced learning experience!")
                   .setPositiveButton("Start Learning", (dialog, which) -> {
                       Toast.makeText(getContext(), "🚀 Premium features activated!", Toast.LENGTH_SHORT).show();
                   })
                   .show();
        }, 2000);
    }
}
