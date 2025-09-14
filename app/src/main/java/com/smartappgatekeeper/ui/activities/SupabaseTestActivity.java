package com.smartappgatekeeper.ui.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.smartappgatekeeper.R;
import com.smartappgatekeeper.network.SupabaseClient;
import com.smartappgatekeeper.service.SupabaseAuthService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

/**
 * Supabase Connection Test Activity
 * Tests the Supabase connection and displays results
 */
public class SupabaseTestActivity extends AppCompatActivity {
    
    private static final String TAG = "SupabaseTest";
    private TextView textResults;
    private Button buttonTestConnection;
    private Button buttonTestAuth;
    private Button buttonTestDatabase;
    private Button buttonBack;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supabase_test);
        
        initViews();
        setupClickListeners();
    }
    
    private void initViews() {
        textResults = findViewById(R.id.text_results);
        buttonTestConnection = findViewById(R.id.button_test_connection);
        buttonTestAuth = findViewById(R.id.button_test_auth);
        buttonTestDatabase = findViewById(R.id.button_test_database);
        buttonBack = findViewById(R.id.button_back);
    }
    
    private void setupClickListeners() {
        buttonTestConnection.setOnClickListener(v -> testConnection());
        buttonTestAuth.setOnClickListener(v -> testAuthentication());
        buttonTestDatabase.setOnClickListener(v -> testDatabase());
        buttonBack.setOnClickListener(v -> finish());
    }
    
    /**
     * Test basic Supabase connection
     */
    private void testConnection() {
        textResults.setText("Testing Supabase connection...\n");
        
        SupabaseClient client = SupabaseClient.getInstance(this);
        
        if (!client.isInitialized()) {
            textResults.append("❌ Supabase client not initialized\n");
            return;
        }
        
        textResults.append("✅ Supabase client initialized\n");
        textResults.append("URL: " + client.getPostgrestUrl() + "\n");
        textResults.append("Anon Key: " + client.getAnonKey().substring(0, 20) + "...\n");
        
        // Test HTTP connection
        testHttpConnection();
    }
    
    /**
     * Test HTTP connection to Supabase
     */
    private void testHttpConnection() {
        CompletableFuture.runAsync(() -> {
            try {
                SupabaseClient client = SupabaseClient.getInstance(this);
                URL url = new URL(client.getPostgrestUrl() + "/user_profiles");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                
                connection.setRequestMethod("GET");
                connection.setRequestProperty("apikey", client.getAnonKey());
                connection.setRequestProperty("Authorization", "Bearer " + client.getAnonKey());
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                
                int responseCode = connection.getResponseCode();
                
                runOnUiThread(() -> {
                    if (responseCode == 200) {
                        textResults.append("✅ HTTP connection successful (200)\n");
                    } else if (responseCode == 401) {
                        textResults.append("⚠️ HTTP connection successful but unauthorized (401)\n");
                        textResults.append("This is expected if RLS is enabled\n");
                    } else {
                        textResults.append("❌ HTTP connection failed (" + responseCode + ")\n");
                    }
                });
                
                connection.disconnect();
                
            } catch (Exception e) {
                runOnUiThread(() -> {
                    textResults.append("❌ HTTP connection error: " + e.getMessage() + "\n");
                    Log.e(TAG, "HTTP connection test failed", e);
                });
            }
        });
    }
    
    /**
     * Test Supabase authentication
     */
    private void testAuthentication() {
        textResults.append("\nTesting Supabase authentication...\n");
        
        SupabaseAuthService authService = SupabaseAuthService.getInstance(this);
        
        // Test with demo credentials
        String email = "demo@example.com";
        String password = "password123";
        
        textResults.append("Attempting to sign in with: " + email + "\n");
        
        authService.signInUser(email, password)
            .thenAccept(result -> {
                runOnUiThread(() -> {
                    if (result.isSuccess()) {
                        textResults.append("✅ Authentication successful\n");
                        textResults.append("User ID: " + result.getUserId() + "\n");
                        textResults.append("Message: " + result.getMessage() + "\n");
                    } else {
                        textResults.append("❌ Authentication failed: " + result.getMessage() + "\n");
                    }
                });
            })
            .exceptionally(throwable -> {
                runOnUiThread(() -> {
                    textResults.append("❌ Authentication error: " + throwable.getMessage() + "\n");
                    Log.e(TAG, "Authentication test failed", throwable);
                });
                return null;
            });
    }
    
    /**
     * Test Supabase database operations
     */
    private void testDatabase() {
        textResults.append("\nTesting Supabase database...\n");
        
        // Test reading from user_profiles table
        CompletableFuture.runAsync(() -> {
            try {
                SupabaseClient client = SupabaseClient.getInstance(this);
                URL url = new URL(client.getPostgrestUrl() + "/user_profiles?select=*&limit=1");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                
                connection.setRequestMethod("GET");
                connection.setRequestProperty("apikey", client.getAnonKey());
                connection.setRequestProperty("Authorization", "Bearer " + client.getAnonKey());
                connection.setRequestProperty("Content-Type", "application/json");
                
                int responseCode = connection.getResponseCode();
                
                if (responseCode == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    
                    runOnUiThread(() -> {
                        textResults.append("✅ Database query successful\n");
                        textResults.append("Response: " + response.toString().substring(0, Math.min(100, response.length())) + "...\n");
                    });
                } else {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    StringBuilder error = new StringBuilder();
                    String line;
                    
                    while ((line = reader.readLine()) != null) {
                        error.append(line);
                    }
                    reader.close();
                    
                    runOnUiThread(() -> {
                        textResults.append("❌ Database query failed (" + responseCode + ")\n");
                        textResults.append("Error: " + error.toString() + "\n");
                    });
                }
                
                connection.disconnect();
                
            } catch (Exception e) {
                runOnUiThread(() -> {
                    textResults.append("❌ Database test error: " + e.getMessage() + "\n");
                    Log.e(TAG, "Database test failed", e);
                });
            }
        });
    }
}
