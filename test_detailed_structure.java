import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Test Detailed Table Structure
 * Get detailed information about table columns
 */
public class test_detailed_structure {
    
    private static final String SUPABASE_URL = "https://ginxtmvwwbrccxwbuhsm.supabase.co";
    private static final String SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imdpbnh0bXZ3d2JyY2N4d2J1aHNtIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTc3NzIxOTAsImV4cCI6MjA3MzM0ODE5MH0._Cc71y0FwNW3LXfKq1DwySnjef83YNThkszEO5a_Zgs";
    
    public static void main(String[] args) {
        System.out.println("🔍 Getting Detailed Table Structure...");
        System.out.println();
        
        // Test existing tables
        String[] existingTables = {
            "user_courses",
            "target_apps", 
            "questions",
            "usage_events"
        };
        
        for (String tableName : existingTables) {
            getTableInfo(tableName);
        }
    }
    
    private static void getTableInfo(String tableName) {
        System.out.println("📋 Getting info for table: " + tableName);
        try {
            // Try to get table info from Supabase
            URL url = new URL(SUPABASE_URL + "/rest/v1/" + tableName + "?select=*&limit=0");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("apikey", SUPABASE_ANON_KEY);
            connection.setRequestProperty("Authorization", "Bearer " + SUPABASE_ANON_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Prefer", "return=minimal");
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
                
                System.out.println("✅ Table '" + tableName + "' structure:");
                System.out.println("Response: " + response.toString());
                
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
}
