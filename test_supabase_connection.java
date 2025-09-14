import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Simple Supabase Connection Test
 * Run this to test the Supabase connection
 */
public class test_supabase_connection {
    
    // Supabase Configuration
    private static final String SUPABASE_URL = "https://ginxtmvwwbrccxwbuhsm.supabase.co";
    private static final String SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imdpbnh0bXZ3d2JyY2N4d2J1aHNtIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTc3NzIxOTAsImV4cCI6MjA3MzM0ODE5MH0._Cc71y0FwNW3LXfKq1DwySnjef83YNThkszEO5a_Zgs";
    
    public static void main(String[] args) {
        System.out.println("🔗 Testing Supabase Connection...");
        System.out.println("URL: " + SUPABASE_URL);
        System.out.println("Anon Key: " + SUPABASE_ANON_KEY.substring(0, 20) + "...");
        System.out.println();
        
        // Test 1: Basic HTTP connection
        testBasicConnection();
        
        // Test 2: Authentication endpoint
        testAuthEndpoint();
        
        // Test 3: Database query
        testDatabaseQuery();
    }
    
    private static void testBasicConnection() {
        System.out.println("📡 Testing basic HTTP connection...");
        try {
            URL url = new URL(SUPABASE_URL + "/rest/v1");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            
            int responseCode = connection.getResponseCode();
            
            if (responseCode == 200) {
                System.out.println("✅ Basic HTTP connection successful (200)");
            } else {
                System.out.println("⚠️ HTTP connection returned: " + responseCode);
            }
            
            connection.disconnect();
        } catch (Exception e) {
            System.out.println("❌ Basic connection failed: " + e.getMessage());
        }
        System.out.println();
    }
    
    private static void testAuthEndpoint() {
        System.out.println("🔐 Testing authentication endpoint...");
        try {
            URL url = new URL(SUPABASE_URL + "/auth/v1/settings");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("apikey", SUPABASE_ANON_KEY);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            
            int responseCode = connection.getResponseCode();
            
            if (responseCode == 200) {
                System.out.println("✅ Authentication endpoint accessible (200)");
            } else {
                System.out.println("⚠️ Authentication endpoint returned: " + responseCode);
            }
            
            connection.disconnect();
        } catch (Exception e) {
            System.out.println("❌ Authentication test failed: " + e.getMessage());
        }
        System.out.println();
    }
    
    private static void testDatabaseQuery() {
        System.out.println("🗄️ Testing database query...");
        try {
            URL url = new URL(SUPABASE_URL + "/rest/v1/user_profiles?select=*&limit=1");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("apikey", SUPABASE_ANON_KEY);
            connection.setRequestProperty("Authorization", "Bearer " + SUPABASE_ANON_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            
            int responseCode = connection.getResponseCode();
            
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                
                System.out.println("✅ Database query successful (200)");
                System.out.println("Response: " + response.toString().substring(0, Math.min(100, response.length())) + "...");
            } else if (responseCode == 401) {
                System.out.println("⚠️ Database query unauthorized (401) - This is expected if RLS is enabled");
            } else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                StringBuilder error = new StringBuilder();
                String line;
                
                while ((line = reader.readLine()) != null) {
                    error.append(line);
                }
                reader.close();
                
                System.out.println("❌ Database query failed (" + responseCode + ")");
                System.out.println("Error: " + error.toString());
            }
            
            connection.disconnect();
        } catch (Exception e) {
            System.out.println("❌ Database test failed: " + e.getMessage());
        }
        System.out.println();
    }
}
