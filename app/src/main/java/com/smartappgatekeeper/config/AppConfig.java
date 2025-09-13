package com.smartappgatekeeper.config;

public class AppConfig {
    
    // Subabase Configuration
    public static final String SUPABASE_URL = "https://ginxtmvwwbrccxwbuhsm.supabase.co";
    public static final String SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imdpbnh0bXZ3d2JyY2N4d2J1aHNtIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTc3NzIxOTAsImV4cCI6MjA3MzM0ODE5MH0._Cc71y0FwNW3LXfKq1DwySnjef83YNThkszEO5a_Zgs";
    public static final String SUPABASE_SERVICE_ROLE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imdpbnh0bXZ3d2JyY2N4d2J1aHNtIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc1Nzc3MjE5MCwiZXhwIjoyMDczMzQ4MTkwfQ.T7LK_3chPvuo0PMrrX9Qcdbl0EQhdjlNNz-lTcaMyYg";
    
    // Gemini AI Configuration
    public static final String GEMINI_API_KEY = "AIzaSyDb2ywvd19DWZYm8csOcdrc0ewhye7-s_I";
    public static final String GEMINI_MODEL_NAME = "gemini-2.0-flash";
    
    // App Configuration
    public static final String APP_NAME = "Smart App Gatekeeper";
    public static final String APP_VERSION = "1.0.0";
    
    // Default Values
    public static final int DEFAULT_QUESTIONS_PER_UNLOCK = 1;
    public static final int DEFAULT_TIME_PER_QUESTION = 30;
    public static final int DEFAULT_WRONG_ANSWER_DELAY = 5;
    public static final int DEFAULT_DAILY_LIMIT_SECONDS = 7200; // 2 hours
    public static final int DEFAULT_PER_UNLOCK_SECONDS = 1800; // 30 minutes
    
    // Coin System
    public static final int COINS_PER_QUIZ_COMPLETION = 10;
    public static final int COINS_PER_MODULE_COMPLETION = 25;
    public static final int COINS_PER_COURSE_COMPLETION = 100;
    public static final int COINS_PER_STREAK_DAY = 5;
    
    // Learning Platform URLs
    public static final String COURSERA_URL = "https://coursera.org";
    public static final String UDEMY_URL = "https://udemy.com";
    public static final String KHAN_ACADEMY_URL = "https://khanacademy.org";
    public static final String EDX_URL = "https://edx.org";
    
    // API Endpoints
    public static final String API_BASE_URL = "https://api.smartappgatekeeper.com";
    public static final String API_VERSION = "v1";
    
    // Database Configuration
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "smart_app_gatekeeper.db";
    
    // Notification Configuration
    public static final String NOTIFICATION_CHANNEL_ID = "learning_reminders";
    public static final String NOTIFICATION_CHANNEL_NAME = "Learning Reminders";
    public static final String NOTIFICATION_CHANNEL_DESCRIPTION = "Notifications for learning reminders and progress updates";
    
    // Privacy Configuration
    public static final boolean DEFAULT_ANALYTICS_ENABLED = true;
    public static final boolean DEFAULT_CRASH_REPORTS_ENABLED = true;
    public static final boolean DEFAULT_ADVERTISING_ENABLED = false;
    
    // Social Features
    public static final int MAX_FRIENDS = 100;
    public static final int LEADERBOARD_SIZE = 50;
    public static final int WEEKLY_REPORT_RETENTION_DAYS = 365;
    
    // Quiz Configuration
    public static final int MAX_QUESTIONS_PER_QUIZ = 10;
    public static final int MIN_QUESTIONS_PER_QUIZ = 1;
    public static final int QUIZ_TIMEOUT_SECONDS = 300; // 5 minutes
    public static final int QUESTION_TIMEOUT_SECONDS = 60; // 1 minute
    
    // Course Configuration
    public static final int MAX_COURSES_PER_PLATFORM = 1000;
    public static final int MAX_MODULES_PER_COURSE = 100;
    public static final int MAX_TOPICS_PER_USER = 20;
    
    // File Upload Configuration
    public static final long MAX_FILE_SIZE_BYTES = 10 * 1024 * 1024; // 10MB
    public static final String[] ALLOWED_FILE_TYPES = {".jpg", ".jpeg", ".png", ".gif", ".pdf", ".doc", ".docx"};
    
    // Cache Configuration
    public static final int CACHE_SIZE_MB = 50;
    public static final int CACHE_EXPIRY_HOURS = 24;
    
    // Rate Limiting
    public static final int MAX_API_REQUESTS_PER_MINUTE = 60;
    public static final int MAX_QUIZ_ATTEMPTS_PER_HOUR = 10;
    public static final int MAX_COURSE_PURCHASES_PER_DAY = 5;
}
