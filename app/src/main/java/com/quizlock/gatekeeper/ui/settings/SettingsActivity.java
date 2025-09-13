package com.quizlock.gatekeeper.ui.settings;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.quizlock.gatekeeper.R;

/**
 * Settings activity - placeholder implementation
 */
public class SettingsActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // For now, just show a simple layout
        setContentView(android.R.layout.simple_list_item_1);
        setTitle("Settings");
        
        // Add back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}