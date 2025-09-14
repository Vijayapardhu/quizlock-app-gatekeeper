import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

/**
 * Test Working Schema
 * Test with the actual table schema that exists
 */
public class test_working_schema {
    
    private static final String SUPABASE_URL = "https://ginxtmvwwbrccxwbuhsm.supabase.co";
    private static final String SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imdpbnh0bXZ3d2JyY2N4d2J1aHNtIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTc3NzIxOTAsImV4cCI6MjA3MzM0ODE5MH0._Cc71y0FwNW3LXfKq1DwySnjef83YNThkszEO5a_Zgs";
    
    public static void main(String[] args) {
        System.out.println("🔍 Testing with Working Schema...");
        System.out.println();
        
        // Test with actual schema
        testUserCoursesWithCorrectSchema();
        testTargetAppsWithCorrectSchema();
        testQuestionsWithCorrectSchema();
        testUsageEventsWithCorrectSchema();
    }
    
    private static void testUserCoursesWithCorrectSchema() {
        System.out.println("📋 Testing user_courses with correct schema...");
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
            
            // Use UUID for user_id
            String userId = UUID.randomUUID().toString();
            String jsonData = "{\"user_id\": \"" + userId + "\"}";
            
            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonData.getBytes());
            }
            
            int responseCode = connection.getResponseCode();
            
            if (responseCode == 201) {
                System.out.println("✅ Insert successful - user_courses schema confirmed");
                System.out.println("  - user_id: UUID (required)");
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
            System.out.println("❌ Error testing user_courses: " + e.getMessage());
        }
        System.out.println();
    }
    
    private static void testTargetAppsWithCorrectSchema() {
        System.out.println("📋 Testing target_apps with correct schema...");
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
            
            // Try with minimal data - just user_id as UUID
            String userId = UUID.randomUUID().toString();
            String jsonData = "{\"user_id\": \"" + userId + "\"}";
            
            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonData.getBytes());
            }
            
            int responseCode = connection.getResponseCode();
            
            if (responseCode == 201) {
                System.out.println("✅ Insert successful - target_apps schema confirmed");
                System.out.println("  - user_id: UUID (required)");
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
            System.out.println("❌ Error testing target_apps: " + e.getMessage());
        }
        System.out.println();
    }
    
    private static void testQuestionsWithCorrectSchema() {
        System.out.println("📋 Testing questions with correct schema...");
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
            
            // Include question_id as required field
            String questionId = UUID.randomUUID().toString();
            String jsonData = "{\"question_id\": \"" + questionId + "\", \"question_text\": \"Test question?\", \"correct_answer\": \"Test answer\"}";
            
            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonData.getBytes());
            }
            
            int responseCode = connection.getResponseCode();
            
            if (responseCode == 201) {
                System.out.println("✅ Insert successful - questions schema confirmed");
                System.out.println("  - question_id: UUID (required)");
                System.out.println("  - question_text: String");
                System.out.println("  - correct_answer: String");
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
            System.out.println("❌ Error testing questions: " + e.getMessage());
        }
        System.out.println();
    }
    
    private static void testUsageEventsWithCorrectSchema() {
        System.out.println("📋 Testing usage_events with correct schema...");
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
            
            // Try with minimal data - just user_id as UUID
            String userId = UUID.randomUUID().toString();
            String jsonData = "{\"user_id\": \"" + userId + "\"}";
            
            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonData.getBytes());
            }
            
            int responseCode = connection.getResponseCode();
            
            if (responseCode == 201) {
                System.out.println("✅ Insert successful - usage_events schema confirmed");
                System.out.println("  - user_id: UUID (required)");
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
            System.out.println("❌ Error testing usage_events: " + e.getMessage());
        }
        System.out.println();
    }
}
