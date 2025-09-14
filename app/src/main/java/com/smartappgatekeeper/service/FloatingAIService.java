package com.smartappgatekeeper.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.smartappgatekeeper.R;
import com.smartappgatekeeper.ui.activities.QuizActivity;

/**
 * Floating AI service that shows a floating AI button for quick quiz access
 */
public class FloatingAIService extends Service {
    private static final String TAG = "FloatingAIService";
    
    private WindowManager windowManager;
    private View floatingView;
    private ImageView aiButton;
    private boolean isFloatingVisible = false;
    
    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        createFloatingView();
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if ("SHOW_FLOATING_AI".equals(action)) {
                showFloatingAI();
            } else if ("HIDE_FLOATING_AI".equals(action)) {
                hideFloatingAI();
            } else if ("TOGGLE_FLOATING_AI".equals(action)) {
                toggleFloatingAI();
            }
        }
        return START_STICKY;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        hideFloatingAI();
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    /**
     * Create the floating AI view
     */
    private void createFloatingView() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        floatingView = inflater.inflate(R.layout.floating_ai_button, null);
        
        aiButton = floatingView.findViewById(R.id.floating_ai_button);
        if (aiButton != null) {
            aiButton.setOnClickListener(v -> {
                startQuiz();
            });
            
            // Add long press for settings
            aiButton.setOnLongClickListener(v -> {
                showAIMenu();
                return true;
            });
        }
    }
    
    /**
     * Show the floating AI button
     */
    private void showFloatingAI() {
        if (isFloatingVisible || floatingView == null) {
            return;
        }
        
        try {
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O 
                    ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                    : WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT
            );
            
            params.gravity = Gravity.TOP | Gravity.END;
            params.x = 20;
            params.y = 100;
            
            windowManager.addView(floatingView, params);
            isFloatingVisible = true;
            
            Log.d(TAG, "Floating AI button shown");
            
        } catch (Exception e) {
            Log.e(TAG, "Error showing floating AI", e);
        }
    }
    
    /**
     * Hide the floating AI button
     */
    private void hideFloatingAI() {
        if (!isFloatingVisible || floatingView == null) {
            return;
        }
        
        try {
            windowManager.removeView(floatingView);
            isFloatingVisible = false;
            
            Log.d(TAG, "Floating AI button hidden");
            
        } catch (Exception e) {
            Log.e(TAG, "Error hiding floating AI", e);
        }
    }
    
    /**
     * Toggle floating AI visibility
     */
    private void toggleFloatingAI() {
        if (isFloatingVisible) {
            hideFloatingAI();
        } else {
            showFloatingAI();
        }
    }
    
    /**
     * Start quiz when AI button is clicked
     */
    private void startQuiz() {
        try {
            Intent quizIntent = new Intent(this, QuizActivity.class);
            quizIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            quizIntent.putExtra("ai_generated", true);
            startActivity(quizIntent);
            
            // Animate button click
            animateButtonClick();
            
        } catch (Exception e) {
            Log.e(TAG, "Error starting quiz", e);
            Toast.makeText(this, "Error starting quiz", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Show AI menu on long press
     */
    private void showAIMenu() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("🤖 AI Quiz Options")
               .setItems(new String[]{
                   "Quick Quiz",
                   "Course-Based Quiz", 
                   "AI Recommendations",
                   "Hide Floating AI"
               }, (dialog, which) -> {
                   switch (which) {
                       case 0:
                           startQuickQuiz();
                           break;
                       case 1:
                           startCourseQuiz();
                           break;
                       case 2:
                           showAIRecommendations();
                           break;
                       case 3:
                           hideFloatingAI();
                           break;
                   }
               })
               .show();
    }
    
    /**
     * Start quick quiz
     */
    private void startQuickQuiz() {
        Intent quizIntent = new Intent(this, QuizActivity.class);
        quizIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        quizIntent.putExtra("quiz_type", "quick");
        startActivity(quizIntent);
    }
    
    /**
     * Start course-based quiz
     */
    private void startCourseQuiz() {
        Intent quizIntent = new Intent(this, QuizActivity.class);
        quizIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        quizIntent.putExtra("quiz_type", "course");
        startActivity(quizIntent);
    }
    
    /**
     * Show AI recommendations
     */
    private void showAIRecommendations() {
        Toast.makeText(this, "AI Recommendations coming soon!", Toast.LENGTH_SHORT).show();
    }
    
    /**
     * Animate button click
     */
    private void animateButtonClick() {
        if (aiButton != null) {
            aiButton.animate()
                   .scaleX(0.8f)
                   .scaleY(0.8f)
                   .setDuration(100)
                   .withEndAction(() -> {
                       aiButton.animate()
                               .scaleX(1.0f)
                               .scaleY(1.0f)
                               .setDuration(100)
                               .start();
                   })
                   .start();
        }
    }
    
    /**
     * Check if floating AI is visible
     */
    public boolean isFloatingVisible() {
        return isFloatingVisible;
    }
}
