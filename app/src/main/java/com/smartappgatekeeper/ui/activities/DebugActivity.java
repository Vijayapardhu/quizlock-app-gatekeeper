package com.smartappgatekeeper.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.smartappgatekeeper.R;
import com.smartappgatekeeper.utils.DatabaseUtils;

/**
 * Debug activity for troubleshooting database issues
 */
public class DebugActivity extends AppCompatActivity {
    
    private TextView textStatus;
    private Button buttonClearData;
    private Button buttonResetDatabase;
    private Button buttonBackToMain;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        
        initializeViews();
        setupUI();
        updateStatus();
    }
    
    private void initializeViews() {
        textStatus = findViewById(R.id.text_status);
        buttonClearData = findViewById(R.id.button_clear_data);
        buttonResetDatabase = findViewById(R.id.button_reset_database);
        buttonBackToMain = findViewById(R.id.button_back_to_main);
    }
    
    private void setupUI() {
        buttonClearData.setOnClickListener(v -> {
            clearAllData();
        });
        
        buttonResetDatabase.setOnClickListener(v -> {
            resetDatabase();
        });
        
        buttonBackToMain.setOnClickListener(v -> {
            goBackToMain();
        });
    }
    
    private void clearAllData() {
        DatabaseUtils.clearAllAppData(this);
        Toast.makeText(this, "✅ All app data cleared!", Toast.LENGTH_SHORT).show();
        updateStatus();
    }
    
    private void resetDatabase() {
        DatabaseUtils.resetDatabaseIfNeeded(this);
        Toast.makeText(this, "🔄 Database reset completed!", Toast.LENGTH_SHORT).show();
        updateStatus();
    }
    
    private void goBackToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    
    private void updateStatus() {
        boolean needsReset = DatabaseUtils.needsDatabaseReset(this);
        String status = "Database Status:\n\n" +
                       "Version Check: " + (needsReset ? "❌ Needs Reset" : "✅ Up to Date") + "\n" +
                       "Last Reset: " + getLastResetTime() + "\n\n" +
                       "Actions Available:\n" +
                       "• Clear All Data - Removes all app data\n" +
                       "• Reset Database - Fixes schema issues\n" +
                       "• Back to Main - Return to app";
        
        textStatus.setText(status);
    }
    
    private String getLastResetTime() {
        // Simple timestamp for debugging
        return "Just now";
    }
}
