package com.smartappgatekeeper.utils;

/**
 * Constants used throughout the application
 * Centralized location for all app constants
 */
public class Constants {
    
    // Database
    public static final String DATABASE_NAME = "smart_app_gatekeeper_database";
    public static final int DATABASE_VERSION = 1;
    
    // SharedPreferences
    public static final String PREF_NAME = "smart_app_gatekeeper_prefs";
    public static final String PREF_FIRST_LAUNCH = "first_launch";
    public static final String PREF_ONBOARDING_COMPLETE = "onboarding_complete";
    public static final String PREF_USER_ID = "user_id";
    public static final String PREF_CURRENT_STREAK = "current_streak";
    public static final String PREF_TOTAL_COINS = "total_coins";
    public static final String PREF_LAST_QUIZ_DATE = "last_quiz_date";
    public static final String PREF_NOTIFICATION_ENABLED = "notification_enabled";
    public static final String PREF_SOUND_ENABLED = "sound_enabled";
    public static final String PREF_VIBRATION_ENABLED = "vibration_enabled";
    public static final String PREF_DARK_MODE = "dark_mode";
    public static final String PREF_ANALYTICS_ENABLED = "analytics_enabled";
    
    // Quiz
    public static final int QUIZ_TIME_LIMIT = 300; // 5 minutes in seconds
    public static final int QUIZ_QUESTIONS_COUNT = 10;
    public static final int QUIZ_POINTS_PER_CORRECT = 10;
    public static final int QUIZ_TIME_SAVED_PER_CORRECT = 2; // minutes
    public static final int QUIZ_COINS_PER_CORRECT = 1;
    public static final int QUIZ_PERFECT_SCORE_BONUS = 5;
    public static final int QUIZ_HIGH_ACCURACY_BONUS = 3;
    
    // Streaks
    public static final int STREAK_SAVER_COST = 50; // coins
    public static final int MAX_STREAK_SAVER_USES = 3; // per day
    public static final long STREAK_RESET_TIME = 24 * 60 * 60 * 1000; // 24 hours in milliseconds
    
    // Notifications
    public static final String NOTIFICATION_CHANNEL_ID = "smart_app_gatekeeper_channel";
    public static final String NOTIFICATION_CHANNEL_NAME = "Smart App Gatekeeper Notifications";
    public static final int NOTIFICATION_QUIZ_REMINDER_ID = 1;
    public static final int NOTIFICATION_STREAK_REMINDER_ID = 2;
    public static final int NOTIFICATION_ACHIEVEMENT_ID = 3;
    public static final int NOTIFICATION_DAILY_REMINDER_ID = 4;
    
    // Service Actions
    public static final String ACTION_QUIZ_REMINDER = "QUIZ_REMINDER";
    public static final String ACTION_STREAK_REMINDER = "STREAK_REMINDER";
    public static final String ACTION_ACHIEVEMENT = "ACHIEVEMENT";
    public static final String ACTION_DAILY_REMINDER = "DAILY_REMINDER";
    public static final String ACTION_TRACK_EVENT = "TRACK_EVENT";
    public static final String ACTION_GENERATE_QUIZ = "GENERATE_QUIZ";
    public static final String ACTION_SUBMIT_ANSWER = "SUBMIT_ANSWER";
    public static final String ACTION_COMPLETE_QUIZ = "COMPLETE_QUIZ";
    public static final String ACTION_GET_RANDOM_QUESTION = "GET_RANDOM_QUESTION";
    public static final String ACTION_GENERATE_DAILY_REPORT = "GENERATE_DAILY_REPORT";
    public static final String ACTION_ANALYZE_USAGE_PATTERNS = "ANALYZE_USAGE_PATTERNS";
    public static final String ACTION_CLEANUP_OLD_DATA = "CLEANUP_OLD_DATA";
    
    // Event Types
    public static final String EVENT_QUIZ_STARTED = "quiz_started";
    public static final String EVENT_QUIZ_COMPLETED = "quiz_completed";
    public static final String EVENT_ANSWER_SUBMITTED = "answer_submitted";
    public static final String EVENT_CORRECT_ANSWER = "correct_answer";
    public static final String EVENT_INCORRECT_ANSWER = "incorrect_answer";
    public static final String EVENT_SESSION_COMPLETED = "session_completed";
    public static final String EVENT_APP_BLOCKED = "app_blocked";
    public static final String EVENT_APP_UNLOCKED = "app_unlocked";
    public static final String EVENT_STREAK_MAINTAINED = "streak_maintained";
    public static final String EVENT_STREAK_BROKEN = "streak_broken";
    public static final String EVENT_ACHIEVEMENT_UNLOCKED = "achievement_unlocked";
    public static final String EVENT_COINS_EARNED = "coins_earned";
    public static final String EVENT_DAILY_SUMMARY = "daily_summary";
    public static final String EVENT_QUESTION_REQUESTED = "question_requested";
    
    // App Categories
    public static final String CATEGORY_SOCIAL_MEDIA = "Social Media";
    public static final String CATEGORY_ENTERTAINMENT = "Entertainment";
    public static final String CATEGORY_GAMING = "Gaming";
    public static final String CATEGORY_MESSAGING = "Messaging";
    public static final String CATEGORY_BROWSER = "Browser";
    public static final String CATEGORY_NEWS = "News";
    public static final String CATEGORY_PRODUCTIVITY = "Productivity";
    public static final String CATEGORY_EDUCATION = "Education";
    
    // Quiz Difficulties
    public static final int DIFFICULTY_EASY = 1;
    public static final int DIFFICULTY_MEDIUM = 2;
    public static final int DIFFICULTY_HARD = 3;
    
    // Session Durations (in minutes)
    public static final int SESSION_5_MIN = 5;
    public static final int SESSION_10_MIN = 10;
    public static final int SESSION_15_MIN = 15;
    public static final int SESSION_30_MIN = 30;
    public static final int SESSION_60_MIN = 60;
    
    // Languages
    public static final String LANGUAGE_ENGLISH = "en";
    public static final String LANGUAGE_SPANISH = "es";
    public static final String LANGUAGE_FRENCH = "fr";
    public static final String LANGUAGE_GERMAN = "de";
    public static final String LANGUAGE_ITALIAN = "it";
    public static final String LANGUAGE_PORTUGUESE = "pt";
    public static final String LANGUAGE_CHINESE = "zh";
    public static final String LANGUAGE_JAPANESE = "ja";
    public static final String LANGUAGE_KOREAN = "ko";
    
    // Goals
    public static final String GOAL_REDUCE_SCREEN_TIME = "Reduce screen time";
    public static final String GOAL_IMPROVE_FOCUS = "Improve focus";
    public static final String GOAL_LEARN_SKILLS = "Learn new skills";
    public static final String GOAL_BREAK_ADDICTION = "Break social media addiction";
    public static final String GOAL_INCREASE_PRODUCTIVITY = "Increase productivity";
    public static final String GOAL_WORK_LIFE_BALANCE = "Better work-life balance";
    
    // Store Categories
    public static final String STORE_THEME = "theme";
    public static final String STORE_AVATAR = "avatar";
    public static final String STORE_POWERUP = "powerup";
    public static final String STORE_PREMIUM = "premium";
    
    // Achievement Categories
    public static final String ACHIEVEMENT_QUIZ = "quiz";
    public static final String ACHIEVEMENT_STREAK = "streak";
    public static final String ACHIEVEMENT_QUESTIONS = "questions";
    public static final String ACHIEVEMENT_TIME = "time";
    public static final String ACHIEVEMENT_FOCUS = "focus";
    
    // API
    public static final String API_BASE_URL = "https://api.smartappgatekeeper.com/";
    public static final String API_VERSION = "v1";
    public static final int API_TIMEOUT = 30; // seconds
    public static final int API_RETRY_COUNT = 3;
    
    // File Paths
    public static final String EXPORT_PATH = "/SmartAppGatekeeper/Exports/";
    public static final String LOG_PATH = "/SmartAppGatekeeper/Logs/";
    public static final String BACKUP_PATH = "/SmartAppGatekeeper/Backups/";
    
    // Time Formats
    public static final String TIME_FORMAT_12H = "h:mm a";
    public static final String TIME_FORMAT_24H = "HH:mm";
    public static final String DATE_FORMAT_SHORT = "MMM dd, yyyy";
    public static final String DATE_FORMAT_LONG = "EEEE, MMMM dd, yyyy";
    public static final String DATETIME_FORMAT = "MMM dd, yyyy h:mm a";
    
    // Animation Durations
    public static final int ANIMATION_DURATION_SHORT = 200;
    public static final int ANIMATION_DURATION_MEDIUM = 300;
    public static final int ANIMATION_DURATION_LONG = 500;
    
    // UI
    public static final int MAX_RECENT_ACTIVITY_ITEMS = 10;
    public static final int MAX_TOP_APPS_COUNT = 5;
    public static final int MAX_WEEKLY_PROGRESS_DAYS = 7;
    public static final int MAX_ACHIEVEMENTS_DISPLAY = 10;
    public static final int MAX_STORE_ITEMS_PER_PAGE = 20;
    
    // Validation
    public static final int MIN_USERNAME_LENGTH = 2;
    public static final int MAX_USERNAME_LENGTH = 50;
    public static final int MIN_AGE = 13;
    public static final int MAX_AGE = 120;
    public static final int MIN_QUIZ_QUESTIONS = 5;
    public static final int MAX_QUIZ_QUESTIONS = 50;
    
    // Error Messages
    public static final String ERROR_NETWORK = "Network error. Please check your connection.";
    public static final String ERROR_DATABASE = "Database error. Please try again.";
    public static final String ERROR_INVALID_INPUT = "Invalid input. Please check your data.";
    public static final String ERROR_PERMISSION_DENIED = "Permission denied. Please grant required permissions.";
    public static final String ERROR_SERVICE_UNAVAILABLE = "Service temporarily unavailable. Please try again later.";
    public static final String ERROR_INSUFFICIENT_COINS = "Insufficient coins. Complete more quizzes to earn coins.";
    public static final String ERROR_QUIZ_NOT_AVAILABLE = "Quiz not available. Please try again later.";
    public static final String ERROR_ACHIEVEMENT_ALREADY_UNLOCKED = "Achievement already unlocked.";
    
    // Success Messages
    public static final String SUCCESS_QUIZ_COMPLETED = "Quiz completed successfully!";
    public static final String SUCCESS_ACHIEVEMENT_UNLOCKED = "Achievement unlocked!";
    public static final String SUCCESS_COINS_EARNED = "Coins earned!";
    public static final String SUCCESS_STREAK_MAINTAINED = "Streak maintained!";
    public static final String SUCCESS_SETTINGS_SAVED = "Settings saved successfully!";
    public static final String SUCCESS_DATA_EXPORTED = "Data exported successfully!";
    public static final String SUCCESS_DATA_IMPORTED = "Data imported successfully!";
}
