import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Test Table Structure
 * Check what columns exist in the existing tables
 */
public class test_table_structure {
    
    private static final String SUPABASE_URL = "https://ginxtmvwwbrccxwbuhsm.supabase.co";
    private static final String SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imdpbnh0bXZ3d2JyY2N4d2J1aHNtIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTc3NzIxOTAsImV4cCI6MjA3MzM0ODE5MH0._Cc71y0FwNW3LXfKq1DwySnjef83YNThkszEO5a_Zgs";
    
    public static void main(String[] args) {
        System.out.println("🔍 Checking Table Structure...");
        System.out.println();
        
        // Test existing tables
        String[] existingTables = {
            "user_courses",
            "target_apps", 
            "questions",
            "usage_events"
        };
        
        for (String tableName : existingTables) {
            checkTableStructure(tableName);
        }
    }
    
    private static void checkTableStructure(String tableName) {
        System.out.println("📋 Checking structure of table: " + tableName);
        try {
            // Try to get table structure by selecting all columns
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
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                
                System.out.println("✅ Table '" + tableName + "' accessible");
                System.out.println("Sample data: " + response.toString());
                
                // Try to get specific columns
                testSpecificColumns(tableName);
                
            } else if (responseCode == 401) {
                System.out.println("⚠️ Table '" + tableName + "' exists but unauthorized (401)");
            } else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                StringBuilder error = new StringBuilder();
                String line;
                
                while ((line = reader.readLine()) != null) {
                    error.append(line);
                }
                reader.close();
                
                System.out.println("❌ Table '" + tableName + "' error (" + responseCode + ")");
                System.out.println("Error: " + error.toString());
            }
            
            connection.disconnect();
        } catch (Exception e) {
            System.out.println("❌ Error checking table '" + tableName + "': " + e.getMessage());
        }
        System.out.println();
    }
    
    private static void testSpecificColumns(String tableName) {
        String[] commonColumns = {"id", "user_id", "email", "name", "created_at", "updated_at"};
        
        for (String column : commonColumns) {
            try {
                URL url = new URL(SUPABASE_URL + "/rest/v1/" + tableName + "?select=" + column + "&limit=1");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("apikey", SUPABASE_ANON_KEY);
                connection.setRequestProperty("Authorization", "Bearer " + SUPABASE_ANON_KEY);
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                
                int responseCode = connection.getResponseCode();
                
                if (responseCode == 200) {
                    System.out.println("  ✅ Column '" + column + "' exists");
                } else if (responseCode == 400) {
                    System.out.println("  ❌ Column '" + column + "' does not exist");
                } else {
                    System.out.println("  ⚠️ Column '" + column + "' status: " + responseCode);
                }
                
                connection.disconnect();
            } catch (Exception e) {
                System.out.println("  ❌ Error testing column '" + column + "': " + e.getMessage());
            }
        }
    }
}
