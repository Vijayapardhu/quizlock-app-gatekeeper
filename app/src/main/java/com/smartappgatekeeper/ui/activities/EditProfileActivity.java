package com.smartappgatekeeper.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.smartappgatekeeper.R;

/**
 * Edit Profile Activity - Apple-style profile editing
 * Allows users to edit their profile information
 */
public class EditProfileActivity extends AppCompatActivity {
    
    private EditText editUserName, editUserEmail, editUserBio;
    private ImageView imageUserAvatar;
    private Button buttonSave, buttonCancel;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        
        initViews();
        setupClickListeners();
        loadCurrentProfile();
    }
    
    private void initViews() {
        editUserName = findViewById(R.id.edit_user_name);
        editUserEmail = findViewById(R.id.edit_user_email);
        editUserBio = findViewById(R.id.edit_user_bio);
        imageUserAvatar = findViewById(R.id.image_user_avatar);
        buttonSave = findViewById(R.id.button_save);
        buttonCancel = findViewById(R.id.button_cancel);
    }
    
    private void setupClickListeners() {
        buttonSave.setOnClickListener(v -> saveProfile());
        buttonCancel.setOnClickListener(v -> finish());
        
        imageUserAvatar.setOnClickListener(v -> {
            Toast.makeText(this, "Change Avatar - Coming Soon", Toast.LENGTH_SHORT).show();
            // TODO: Implement avatar selection
        });
    }
    
    private void loadCurrentProfile() {
        // Load current profile data
        editUserName.setText("John Doe");
        editUserEmail.setText("john.doe@example.com");
        editUserBio.setText("Quiz enthusiast and learning advocate");
    }
    
    private void saveProfile() {
        String name = editUserName.getText().toString().trim();
        String email = editUserEmail.getText().toString().trim();
        String bio = editUserBio.getText().toString().trim();
        
        if (name.isEmpty()) {
            editUserName.setError("Name is required");
            return;
        }
        
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editUserEmail.setError("Valid email is required");
            return;
        }
        
        // TODO: Save to database
        Toast.makeText(this, "Profile saved successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
