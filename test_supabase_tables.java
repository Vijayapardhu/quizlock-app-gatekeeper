import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Test Supabase Tables
 * Check what tables exist in the database
 */
public class test_supabase_tables {
    
    private static final String SUPABASE_URL = "https://ginxtmvwwbrccxwbuhsm.supabase.co";
    private static final String SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imdpbnh0bXZ3d2JyY2N4d2J1aHNtIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTc3NzIxOTAsImV4cCI6MjA3MzM0ODE5MH0._Cc71y0FwNW3LXfKq1DwySnjef83YNThkszEO5a_Zgs";
    
    public static void main(String[] args) {
        System.out.println("🔍 Checking Supabase Tables...");
        System.out.println();
        
        // Test different table names
        String[] tablesToTest = {
            "user_profiles",
            "user_courses", 
            "target_apps",
            "questions",
            "usage_events",
            "streaks",
            "app_settings",
            "quiz_results"
        };
        
        for (String tableName : tablesToTest) {
            testTable(tableName);
        }
        
        // Test schema information
        testSchemaInfo();
    }
    
    private static void testTable(String tableName) {
        System.out.println("📋 Testing table: " + tableName);
        try {
            URL url = new URL(SUPABASE_URL + "/rest/v1/" + tableName + "?select=*&limit=1");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("apikey", SUPABASE_ANON_KEY);
            connection.setRequestProperty("Authorization", "Bearer " + SUPABASE_ANON_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            
            int responseCode = connection.getResponseCode();
            
            if (responseCode == 200) {
                System.out.println("✅ Table '" + tableName + "' exists and accessible");
            } else if (responseCode == 401) {
                System.out.println("⚠️ Table '" + tableName + "' exists but unauthorized (401)");
            } else if (responseCode == 404) {
                System.out.println("❌ Table '" + tableName + "' does not exist (404)");
            } else {
                System.out.println("⚠️ Table '" + tableName + "' returned: " + responseCode);
            }
            
            connection.disconnect();
        } catch (Exception e) {
            System.out.println("❌ Error testing table '" + tableName + "': " + e.getMessage());
        }
        System.out.println();
    }
    
    private static void testSchemaInfo() {
        System.out.println("📊 Testing schema information...");
        try {
            // Try to get schema information
            URL url = new URL(SUPABASE_URL + "/rest/v1/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("apikey", SUPABASE_ANON_KEY);
            connection.setRequestProperty("Authorization", "Bearer " + SUPABASE_ANON_KEY);
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
                
                System.out.println("✅ Schema info accessible");
                System.out.println("Response: " + response.toString().substring(0, Math.min(200, response.length())) + "...");
            } else {
                System.out.println("⚠️ Schema info returned: " + responseCode);
            }
            
            connection.disconnect();
        } catch (Exception e) {
            System.out.println("❌ Error getting schema info: " + e.getMessage());
        }
        System.out.println();
    }
}
