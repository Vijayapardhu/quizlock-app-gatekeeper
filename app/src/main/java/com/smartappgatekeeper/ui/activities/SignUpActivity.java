package com.smartappgatekeeper.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.smartappgatekeeper.R;

/**
 * Sign Up Activity - Apple-style user registration
 * Handles new user registration with validation
 */
public class SignUpActivity extends AppCompatActivity {
    
    private EditText editFullName, editEmail, editPassword, editConfirmPassword;
    private Button buttonSignUp, buttonLogin;
    private TextView textTerms;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        
        initViews();
        setupClickListeners();
    }
    
    private void initViews() {
        editFullName = findViewById(R.id.edit_full_name);
        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);
        editConfirmPassword = findViewById(R.id.edit_confirm_password);
        buttonSignUp = findViewById(R.id.button_sign_up);
        buttonLogin = findViewById(R.id.button_login);
        textTerms = findViewById(R.id.text_terms);
    }
    
    private void setupClickListeners() {
        buttonSignUp.setOnClickListener(v -> performSignUp());
        buttonLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
        textTerms.setOnClickListener(v -> {
            Toast.makeText(this, "Terms & Conditions - Coming Soon", Toast.LENGTH_SHORT).show();
            // TODO: Implement terms and conditions
        });
    }
    
    private void performSignUp() {
        String fullName = editFullName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String confirmPassword = editConfirmPassword.getText().toString().trim();
        
        // Validation
        if (TextUtils.isEmpty(fullName)) {
            editFullName.setError("Full name is required");
            return;
        }
        
        if (TextUtils.isEmpty(email)) {
            editEmail.setError("Email is required");
            return;
        }
        
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Please enter a valid email");
            return;
        }
        
        if (TextUtils.isEmpty(password)) {
            editPassword.setError("Password is required");
            return;
        }
        
        if (password.length() < 6) {
            editPassword.setError("Password must be at least 6 characters");
            return;
        }
        
        if (TextUtils.isEmpty(confirmPassword)) {
            editConfirmPassword.setError("Please confirm your password");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            editConfirmPassword.setError("Passwords do not match");
            return;
        }
        
        // TODO: Implement actual registration
        simulateSignUp(fullName, email, password);
    }
    
    private void simulateSignUp(String fullName, String email, String password) {
        // Show loading
        buttonSignUp.setText("Creating Account...");
        buttonSignUp.setEnabled(false);
        
        // Simulate network delay
        new android.os.Handler().postDelayed(() -> {
            // TODO: Replace with actual registration
            Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();
            
            // Save login state
            getSharedPreferences("auth", MODE_PRIVATE)
                .edit()
                .putBoolean("is_logged_in", true)
                .putString("user_email", email)
                .putString("user_name", fullName)
                .apply();
            
            // Go to main activity
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }, 2000);
    }
}
