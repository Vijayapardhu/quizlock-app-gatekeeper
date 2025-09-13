package com.quizlock.gatekeeper.ui.appselection;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.quizlock.gatekeeper.R;

/**
 * App selection activity - placeholder implementation
 */
public class AppSelectionActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // For now, just show a simple layout
        setContentView(android.R.layout.simple_list_item_1);
        setTitle("Select Apps to Block");
        
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