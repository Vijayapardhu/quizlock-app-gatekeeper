package com.vijayapardhu.quizlock.utils;

public class Constants {
    
    // Preferences Keys
    public static final String PREF_ONBOARDING_COMPLETED = "onboarding_completed";
    public static final String PREF_ACCESSIBILITY_ENABLED = "accessibility_enabled";
    public static final String PREF_SELECTED_TOPICS = "selected_topics";
    public static final String PREF_CURRENT_STREAK = "current_streak";
    public static final String PREF_BEST_STREAK = "best_streak";
    public static final String PREF_GEMINI_API_KEY = "gemini_api_key";
    public static final String PREF_NOTIFICATIONS_ENABLED = "notifications_enabled";
    public static final String PREF_DAILY_GOAL = "daily_goal";
    
    // Quiz Topics
    public static final String TOPIC_MATH = "Math";
    public static final String TOPIC_PROGRAMMING = "Programming";
    public static final String TOPIC_GENERAL_KNOWLEDGE = "General Knowledge";
    public static final String TOPIC_VOCABULARY = "Vocabulary";
    public static final String TOPIC_FITNESS = "Fitness";
    public static final String TOPIC_SCIENCE = "Science";
    public static final String TOPIC_HISTORY = "History";
    public static final String TOPIC_GEOGRAPHY = "Geography";
    
    // Quiz Difficulty Levels
    public static final String DIFFICULTY_EASY = "Easy";
    public static final String DIFFICULTY_MEDIUM = "Medium";
    public static final String DIFFICULTY_HARD = "Hard";
    
    // Session Constants
    public static final int DEFAULT_SESSION_MINUTES = 30;
    public static final int COOLDOWN_MINUTES = 5;
    public static final int MAX_DAILY_LIMIT_MINUTES = 480; // 8 hours
    
    // Notification IDs
    public static final int NOTIFICATION_ID_SESSION_ACTIVE = 1001;
    public static final int NOTIFICATION_ID_SESSION_ENDED = 1002;
    public static final int NOTIFICATION_ID_STREAK_REMINDER = 1003;
    
    // Intent Actions
    public static final String ACTION_QUIZ_INTERCEPT = "com.vijayapardhu.quizlock.QUIZ_INTERCEPT";
    public static final String ACTION_SESSION_END = "com.vijayapardhu.quizlock.SESSION_END";
    public static final String ACTION_EMERGENCY_UNLOCK = "com.vijayapardhu.quizlock.EMERGENCY_UNLOCK";
    
    // Extra Keys
    public static final String EXTRA_PACKAGE_NAME = "package_name";
    public static final String EXTRA_APP_NAME = "app_name";
    public static final String EXTRA_SESSION_ID = "session_id";
    
    // Popular Apps to Restrict (default suggestions)
    public static final String[] POPULAR_APPS = {
        "com.instagram.android",
        "com.facebook.katana",
        "com.snapchat.android",
        "com.zhiliaoapp.musically", // TikTok
        "com.twitter.android",
        "com.youtube.android",
        "com.netflix.mediaclient",
        "com.spotify.music",
        "com.reddit.frontpage",
        "com.discord",
        "com.whatsapp",
        "com.telegram.messenger",
        "com.pubg.imobile", // PUBG Mobile
        "com.supercell.clashofclans",
        "com.king.candycrushsaga",
        "com.amazon.mShop.android.shopping"
    };
    
    // Default Quiz Questions Cache Size
    public static final int QUIZ_CACHE_SIZE = 50;
    
    // API Timeouts
    public static final int NETWORK_TIMEOUT_SECONDS = 30;
    
    // Date Format
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
}