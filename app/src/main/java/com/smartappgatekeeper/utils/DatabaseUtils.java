package com.smartappgatekeeper.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.smartappgatekeeper.database.AppDatabase;

/**
 * Database utility class for managing database operations
 */
public class DatabaseUtils {
    
    private static final String TAG = "DatabaseUtils";
    private static final String PREFS_NAME = "database_prefs";
    private static final String KEY_DATABASE_VERSION = "database_version";
    
    /**
     * Clear all app data including database and preferences
     * This is useful when there are schema conflicts
     */
    public static void clearAllAppData(Context context) {
        try {
            // Clear database
            AppDatabase.closeDatabase();
            context.deleteDatabase("smart_app_gatekeeper_database");
            
            // Clear shared preferences
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            prefs.edit().clear().apply();
            
            // Clear all app preferences
            context.getSharedPreferences("app_settings", Context.MODE_PRIVATE).edit().clear().apply();
            context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE).edit().clear().apply();
            
            Log.d(TAG, "All app data cleared successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error clearing app data: " + e.getMessage());
        }
    }
    
    /**
     * Check if database needs to be reset
     */
    public static boolean needsDatabaseReset(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int storedVersion = prefs.getInt(KEY_DATABASE_VERSION, 0);
        int currentVersion = 2; // Current database version
        
        return storedVersion != currentVersion;
    }
    
    /**
     * Update database version in preferences
     */
    public static void updateDatabaseVersion(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(KEY_DATABASE_VERSION, 2).apply();
    }
    
    /**
     * Reset database if needed
     */
    public static void resetDatabaseIfNeeded(Context context) {
        if (needsDatabaseReset(context)) {
            Log.d(TAG, "Database version mismatch detected, clearing data...");
            clearAllAppData(context);
            updateDatabaseVersion(context);
        }
    }
}
