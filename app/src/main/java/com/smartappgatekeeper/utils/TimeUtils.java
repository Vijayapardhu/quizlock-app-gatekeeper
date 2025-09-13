package com.smartappgatekeeper.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for time-related operations
 * Provides methods for formatting, calculating, and manipulating time
 */
public class TimeUtils {
    
    /**
     * Format time in milliseconds to readable string
     */
    public static String formatTime(long timeInMillis) {
        long hours = TimeUnit.MILLISECONDS.toHours(timeInMillis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeInMillis) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeInMillis) % 60;
        
        if (hours > 0) {
            return String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds);
        }
    }
    
    /**
     * Format time in seconds to readable string
     */
    public static String formatTimeSeconds(int timeInSeconds) {
        int hours = timeInSeconds / 3600;
        int minutes = (timeInSeconds % 3600) / 60;
        int seconds = timeInSeconds % 60;
        
        if (hours > 0) {
            return String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds);
        }
    }
    
    /**
     * Format duration in minutes to readable string
     */
    public static String formatDuration(int minutes) {
        if (minutes < 60) {
            return minutes + " min";
        } else {
            int hours = minutes / 60;
            int remainingMinutes = minutes % 60;
            if (remainingMinutes == 0) {
                return hours + "h";
            } else {
                return hours + "h " + remainingMinutes + "m";
            }
        }
    }
    
    /**
     * Format date to readable string
     */
    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.getDefault());
        return formatter.format(date);
    }
    
    /**
     * Format date to short string (MMM dd, yyyy)
     */
    public static String formatDateShort(Date date) {
        return formatDate(date, Constants.DATE_FORMAT_SHORT);
    }
    
    /**
     * Format date to long string (EEEE, MMMM dd, yyyy)
     */
    public static String formatDateLong(Date date) {
        return formatDate(date, Constants.DATE_FORMAT_LONG);
    }
    
    /**
     * Format date and time
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, Constants.DATETIME_FORMAT);
    }
    
    /**
     * Get start of today
     */
    public static long getTodayStart() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
    
    /**
     * Get end of today
     */
    public static long getTodayEnd() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTimeInMillis();
    }
    
    /**
     * Get start of week (Monday)
     */
    public static long getWeekStart() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
    
    /**
     * Get end of week (Sunday)
     */
    public static long getWeekEnd() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTimeInMillis();
    }
    
    /**
     * Get start of month
     */
    public static long getMonthStart() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
    
    /**
     * Get end of month
     */
    public static long getMonthEnd() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTimeInMillis();
    }
    
    /**
     * Check if two dates are on the same day
     */
    public static boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
    
    /**
     * Check if a date is today
     */
    public static boolean isToday(Date date) {
        return isSameDay(date, new Date());
    }
    
    /**
     * Check if a date is yesterday
     */
    public static boolean isYesterday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return isSameDay(date, cal.getTime());
    }
    
    /**
     * Get days between two dates
     */
    public static int getDaysBetween(Date startDate, Date endDate) {
        long diffInMillis = endDate.getTime() - startDate.getTime();
        return (int) TimeUnit.MILLISECONDS.toDays(diffInMillis);
    }
    
    /**
     * Get hours between two dates
     */
    public static int getHoursBetween(Date startDate, Date endDate) {
        long diffInMillis = endDate.getTime() - startDate.getTime();
        return (int) TimeUnit.MILLISECONDS.toHours(diffInMillis);
    }
    
    /**
     * Get minutes between two dates
     */
    public static int getMinutesBetween(Date startDate, Date endDate) {
        long diffInMillis = endDate.getTime() - startDate.getTime();
        return (int) TimeUnit.MILLISECONDS.toMinutes(diffInMillis);
    }
    
    /**
     * Add days to a date
     */
    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }
    
    /**
     * Add hours to a date
     */
    public static Date addHours(Date date, int hours) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, hours);
        return cal.getTime();
    }
    
    /**
     * Add minutes to a date
     */
    public static Date addMinutes(Date date, int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minutes);
        return cal.getTime();
    }
    
    /**
     * Get relative time string (e.g., "2 hours ago", "3 days ago")
     */
    public static String getRelativeTimeString(Date date) {
        long now = System.currentTimeMillis();
        long time = date.getTime();
        long diff = now - time;
        
        if (diff < 60000) { // Less than 1 minute
            return "Just now";
        } else if (diff < 3600000) { // Less than 1 hour
            int minutes = (int) (diff / 60000);
            return minutes + " minute" + (minutes == 1 ? "" : "s") + " ago";
        } else if (diff < 86400000) { // Less than 1 day
            int hours = (int) (diff / 3600000);
            return hours + " hour" + (hours == 1 ? "" : "s") + " ago";
        } else if (diff < 604800000) { // Less than 1 week
            int days = (int) (diff / 86400000);
            return days + " day" + (days == 1 ? "" : "s") + " ago";
        } else if (diff < 2592000000L) { // Less than 1 month
            int weeks = (int) (diff / 604800000);
            return weeks + " week" + (weeks == 1 ? "" : "s") + " ago";
        } else if (diff < 31536000000L) { // Less than 1 year
            int months = (int) (diff / 2592000000L);
            return months + " month" + (months == 1 ? "" : "s") + " ago";
        } else {
            int years = (int) (diff / 31536000000L);
            return years + " year" + (years == 1 ? "" : "s") + " ago";
        }
    }
    
    /**
     * Get time until next day
     */
    public static long getTimeUntilNextDay() {
        long now = System.currentTimeMillis();
        long tomorrow = getTodayStart() + 86400000; // Add 24 hours
        return tomorrow - now;
    }
    
    /**
     * Get time until next week
     */
    public static long getTimeUntilNextWeek() {
        long now = System.currentTimeMillis();
        long nextWeek = getWeekStart() + 604800000; // Add 7 days
        return nextWeek - now;
    }
    
    /**
     * Get time until next month
     */
    public static long getTimeUntilNextMonth() {
        long now = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis() - now;
    }
    
    /**
     * Check if it's a new day since last check
     */
    public static boolean isNewDay(long lastCheckTime) {
        return System.currentTimeMillis() - lastCheckTime >= 86400000; // 24 hours
    }
    
    /**
     * Check if it's a new week since last check
     */
    public static boolean isNewWeek(long lastCheckTime) {
        return System.currentTimeMillis() - lastCheckTime >= 604800000; // 7 days
    }
    
    /**
     * Check if it's a new month since last check
     */
    public static boolean isNewMonth(long lastCheckTime) {
        Calendar cal = Calendar.getInstance();
        Calendar lastCheck = Calendar.getInstance();
        lastCheck.setTimeInMillis(lastCheckTime);
        
        return cal.get(Calendar.YEAR) != lastCheck.get(Calendar.YEAR) ||
               cal.get(Calendar.MONTH) != lastCheck.get(Calendar.MONTH);
    }
}
