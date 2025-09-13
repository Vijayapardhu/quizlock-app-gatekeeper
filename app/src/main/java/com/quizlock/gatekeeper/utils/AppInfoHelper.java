package com.quizlock.gatekeeper.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

/**
 * Helper class for app-related operations
 */
public class AppInfoHelper {
    
    /**
     * Get app name from package name
     */
    public static String getAppName(Context context, String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(packageName, 0);
            return pm.getApplicationLabel(appInfo).toString();
        } catch (PackageManager.NameNotFoundException e) {
            return packageName; // Return package name as fallback
        }
    }
    
    /**
     * Get app icon from package name
     */
    public static Drawable getAppIcon(Context context, String packageName) throws PackageManager.NameNotFoundException {
        PackageManager pm = context.getPackageManager();
        return pm.getApplicationIcon(packageName);
    }
    
    /**
     * Convert timestamp to "time ago" text
     */
    public static String getTimeAgoText(long timestamp) {
        long now = System.currentTimeMillis();
        long diff = now - timestamp;
        
        if (diff < 60 * 1000) { // Less than 1 minute
            return "just now";
        } else if (diff < 60 * 60 * 1000) { // Less than 1 hour
            long minutes = diff / (60 * 1000);
            return minutes + "m ago";
        } else if (diff < 24 * 60 * 60 * 1000) { // Less than 1 day
            long hours = diff / (60 * 60 * 1000);
            return hours + "h ago";
        } else if (diff < 7 * 24 * 60 * 60 * 1000) { // Less than 1 week
            long days = diff / (24 * 60 * 60 * 1000);
            return days + "d ago";
        } else {
            // More than a week, show actual date
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM dd", java.util.Locale.getDefault());
            return sdf.format(new java.util.Date(timestamp));
        }
    }
    
    /**
     * Format duration from milliseconds to readable text
     */
    public static String formatDuration(long durationMs) {
        if (durationMs < 1000) {
            return "< 1s";
        } else if (durationMs < 60 * 1000) {
            return (durationMs / 1000) + "s";
        } else if (durationMs < 60 * 60 * 1000) {
            long minutes = durationMs / (60 * 1000);
            long seconds = (durationMs % (60 * 1000)) / 1000;
            if (seconds == 0) {
                return minutes + "m";
            } else {
                return minutes + "m " + seconds + "s";
            }
        } else {
            long hours = durationMs / (60 * 60 * 1000);
            long minutes = (durationMs % (60 * 60 * 1000)) / (60 * 1000);
            if (minutes == 0) {
                return hours + "h";
            } else {
                return hours + "h " + minutes + "m";
            }
        }
    }
    
    /**
     * Check if an app is a system app
     */
    public static boolean isSystemApp(Context context, String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(packageName, 0);
            return (appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    
    /**
     * Get common app display names for known packages
     */
    public static String getCommonAppName(String packageName) {
        switch (packageName) {
            case "com.instagram.android":
                return "Instagram";
            case "com.google.android.youtube":
                return "YouTube";
            case "com.facebook.katana":
                return "Facebook";
            case "com.twitter.android":
                return "Twitter";
            case "com.snapchat.android":
                return "Snapchat";
            case "com.tiktokv.music":
                return "TikTok";
            case "com.netflix.mediaclient":
                return "Netflix";
            case "com.spotify.music":
                return "Spotify";
            case "com.whatsapp":
                return "WhatsApp";
            case "com.telegram.messenger":
                return "Telegram";
            default:
                return null;
        }
    }
}