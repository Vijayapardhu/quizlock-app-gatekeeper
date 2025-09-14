package com.smartappgatekeeper.config;

/**
 * Supabase configuration constants
 * Replace these with your actual Supabase project credentials
 */
public class SupabaseConfig {
    
    // Supabase Project URL - Using values from AppConfig
    public static final String SUPABASE_URL = AppConfig.SUPABASE_URL;
    
    // Supabase Anon Key - Using values from AppConfig
    public static final String SUPABASE_ANON_KEY = AppConfig.SUPABASE_ANON_KEY;
    
    // Supabase Service Role Key (for admin operations) - Using values from AppConfig
    public static final String SUPABASE_SERVICE_ROLE_KEY = AppConfig.SUPABASE_SERVICE_ROLE_KEY;
    
    // Database table names
    public static final String TABLE_USER_PROFILES = "user_profiles";
    public static final String TABLE_TARGET_APPS = "target_apps";
    public static final String TABLE_QUESTIONS = "questions";
    public static final String TABLE_USAGE_EVENTS = "usage_events";
    public static final String TABLE_STREAKS = "streaks";
    public static final String TABLE_APP_SETTINGS = "app_settings";
    
    // Storage bucket names
    public static final String BUCKET_AVATARS = "avatars";
    public static final String BUCKET_IMAGES = "images";
    
    // Real-time channels
    public static final String CHANNEL_USAGE_EVENTS = "usage_events";
    public static final String CHANNEL_QUIZ_RESULTS = "quiz_results";
    public static final String CHANNEL_NOTIFICATIONS = "notifications";
    
    // Authentication settings
    public static final boolean ENABLE_EMAIL_AUTH = true;
    public static final boolean ENABLE_GOOGLE_AUTH = true;
    public static final boolean ENABLE_ANONYMOUS_AUTH = false;
    
    // Database settings
    public static final int MAX_RETRY_ATTEMPTS = 3;
    public static final long CONNECTION_TIMEOUT_MS = 30000; // 30 seconds
    public static final long READ_TIMEOUT_MS = 60000; // 60 seconds
    
    // Cache settings
    public static final long CACHE_DURATION_MS = 300000; // 5 minutes
    public static final int MAX_CACHE_SIZE = 100;
    
    // Sync settings
    public static final long SYNC_INTERVAL_MS = 300000; // 5 minutes
    public static final boolean ENABLE_OFFLINE_SYNC = true;
    public static final boolean ENABLE_REAL_TIME_SYNC = true;
}
