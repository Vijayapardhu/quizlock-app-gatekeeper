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
 * Login Activity - Apple-style authentication
 * Handles user login with email/password
 */
public class LoginActivity extends AppCompatActivity {
    
    private EditText editEmail, editPassword;
    private Button buttonLogin, buttonSignUp;
    private TextView textForgotPassword;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        initViews();
        setupClickListeners();
    }
    
    private void initViews() {
        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);
        buttonLogin = findViewById(R.id.button_login);
        buttonSignUp = findViewById(R.id.button_sign_up);
        textForgotPassword = findViewById(R.id.text_forgot_password);
    }
    
    private void setupClickListeners() {
        buttonLogin.setOnClickListener(v -> performLogin());
        buttonSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });
        textForgotPassword.setOnClickListener(v -> {
            Toast.makeText(this, "Forgot Password - Coming Soon", Toast.LENGTH_SHORT).show();
            // TODO: Implement forgot password functionality
        });
    }
    
    private void performLogin() {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        
        if (TextUtils.isEmpty(email)) {
            editEmail.setError("Email is required");
            return;
        }
        
        if (TextUtils.isEmpty(password)) {
            editPassword.setError("Password is required");
            return;
        }
        
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Please enter a valid email");
            return;
        }
        
        if (password.length() < 6) {
            editPassword.setError("Password must be at least 6 characters");
            return;
        }
        
        // TODO: Implement actual authentication
        // For now, just simulate login
        simulateLogin(email, password);
    }
    
    private void simulateLogin(String email, String password) {
        // Show loading
        buttonLogin.setText("Logging in...");
        buttonLogin.setEnabled(false);
        
        // Simulate network delay
        new android.os.Handler().postDelayed(() -> {
            // TODO: Replace with actual authentication
            if (email.equals("demo@example.com") && password.equals("password123")) {
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                
                // Save login state
                getSharedPreferences("auth", MODE_PRIVATE)
                    .edit()
                    .putBoolean("is_logged_in", true)
                    .putString("user_email", email)
                    .apply();
                
                // Go to main activity
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                buttonLogin.setText("Login");
                buttonLogin.setEnabled(true);
            }
        }, 1500);
    }
}
