import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Test Insert Data
 * Try to insert data to see what columns are expected
 */
public class test_insert_data {
    
    private static final String SUPABASE_URL = "https://ginxtmvwwbrccxwbuhsm.supabase.co";
    private static final String SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imdpbnh0bXZ3d2JyY2N4d2J1aHNtIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTc3NzIxOTAsImV4cCI6MjA3MzM0ODE5MH0._Cc71y0FwNW3LXfKq1DwySnjef83YNThkszEO5a_Zgs";
    
    public static void main(String[] args) {
        System.out.println("🔍 Testing Insert Data to Understand Table Structure...");
        System.out.println();
        
        // Test inserting data to understand table structure
        testInsertToUserCourses();
        testInsertToTargetApps();
        testInsertToQuestions();
        testInsertToUsageEvents();
    }
    
    private static void testInsertToUserCourses() {
        System.out.println("📋 Testing insert to user_courses...");
        try {
            URL url = new URL(SUPABASE_URL + "/rest/v1/user_courses");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("apikey", SUPABASE_ANON_KEY);
            connection.setRequestProperty("Authorization", "Bearer " + SUPABASE_ANON_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Prefer", "return=minimal");
            connection.setDoOutput(true);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            
            // Try minimal data
            String jsonData = "{\"user_id\": \"test-user-123\"}";
            
            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonData.getBytes());
            }
            
            int responseCode = connection.getResponseCode();
            
            if (responseCode == 201) {
                System.out.println("✅ Insert successful - user_courses accepts user_id");
            } else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                StringBuilder error = new StringBuilder();
                String line;
                
                while ((line = reader.readLine()) != null) {
                    error.append(line);
                }
                reader.close();
                
                System.out.println("❌ Insert failed (" + responseCode + ")");
                System.out.println("Error: " + error.toString());
            }
            
            connection.disconnect();
        } catch (Exception e) {
            System.out.println("❌ Error testing user_courses insert: " + e.getMessage());
        }
        System.out.println();
    }
    
    private static void testInsertToTargetApps() {
        System.out.println("📋 Testing insert to target_apps...");
        try {
            URL url = new URL(SUPABASE_URL + "/rest/v1/target_apps");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("apikey", SUPABASE_ANON_KEY);
            connection.setRequestProperty("Authorization", "Bearer " + SUPABASE_ANON_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Prefer", "return=minimal");
            connection.setDoOutput(true);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            
            // Try minimal data
            String jsonData = "{\"user_id\": \"test-user-123\", \"app_name\": \"Test App\"}";
            
            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonData.getBytes());
            }
            
            int responseCode = connection.getResponseCode();
            
            if (responseCode == 201) {
                System.out.println("✅ Insert successful - target_apps accepts user_id and app_name");
            } else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                StringBuilder error = new StringBuilder();
                String line;
                
                while ((line = reader.readLine()) != null) {
                    error.append(line);
                }
                reader.close();
                
                System.out.println("❌ Insert failed (" + responseCode + ")");
                System.out.println("Error: " + error.toString());
            }
            
            connection.disconnect();
        } catch (Exception e) {
            System.out.println("❌ Error testing target_apps insert: " + e.getMessage());
        }
        System.out.println();
    }
    
    private static void testInsertToQuestions() {
        System.out.println("📋 Testing insert to questions...");
        try {
            URL url = new URL(SUPABASE_URL + "/rest/v1/questions");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("apikey", SUPABASE_ANON_KEY);
            connection.setRequestProperty("Authorization", "Bearer " + SUPABASE_ANON_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Prefer", "return=minimal");
            connection.setDoOutput(true);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            
            // Try minimal data
            String jsonData = "{\"question_text\": \"Test question?\", \"correct_answer\": \"Test answer\"}";
            
            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonData.getBytes());
            }
            
            int responseCode = connection.getResponseCode();
            
            if (responseCode == 201) {
                System.out.println("✅ Insert successful - questions accepts question_text and correct_answer");
            } else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                StringBuilder error = new StringBuilder();
                String line;
                
                while ((line = reader.readLine()) != null) {
                    error.append(line);
                }
                reader.close();
                
                System.out.println("❌ Insert failed (" + responseCode + ")");
                System.out.println("Error: " + error.toString());
            }
            
            connection.disconnect();
        } catch (Exception e) {
            System.out.println("❌ Error testing questions insert: " + e.getMessage());
        }
        System.out.println();
    }
    
    private static void testInsertToUsageEvents() {
        System.out.println("📋 Testing insert to usage_events...");
        try {
            URL url = new URL(SUPABASE_URL + "/rest/v1/usage_events");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("apikey", SUPABASE_ANON_KEY);
            connection.setRequestProperty("Authorization", "Bearer " + SUPABASE_ANON_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Prefer", "return=minimal");
            connection.setDoOutput(true);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            
            // Try minimal data
            String jsonData = "{\"user_id\": \"test-user-123\", \"event_type\": \"app_opened\"}";
            
            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonData.getBytes());
            }
            
            int responseCode = connection.getResponseCode();
            
            if (responseCode == 201) {
                System.out.println("✅ Insert successful - usage_events accepts user_id and event_type");
            } else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                StringBuilder error = new StringBuilder();
                String line;
                
                while ((line = reader.readLine()) != null) {
                    error.append(line);
                }
                reader.close();
                
                System.out.println("❌ Insert failed (" + responseCode + ")");
                System.out.println("Error: " + error.toString());
            }
            
            connection.disconnect();
        } catch (Exception e) {
            System.out.println("❌ Error testing usage_events insert: " + e.getMessage());
        }
        System.out.println();
    }
}
