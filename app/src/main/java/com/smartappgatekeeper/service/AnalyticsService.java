package com.smartappgatekeeper.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.smartappgatekeeper.repository.AppRepository;
import com.smartappgatekeeper.database.entities.UsageEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service for analytics and data collection
 * Tracks user behavior, app usage patterns, and learning progress
 */
public class AnalyticsService extends Service {
    
    private static final String TAG = "AnalyticsService";
    private AppRepository repository;
    
    @Override
    public void onCreate() {
        super.onCreate();
        repository = AppRepository.getInstance(getApplication());
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case "TRACK_EVENT":
                        String eventType = intent.getStringExtra("event_type");
                        String appName = intent.getStringExtra("app_name");
                        String details = intent.getStringExtra("details");
                        trackEvent(eventType, appName, details);
                        break;
                    case "GENERATE_DAILY_REPORT":
                        generateDailyReport();
                        break;
                    case "ANALYZE_USAGE_PATTERNS":
                        analyzeUsagePatterns();
                        break;
                    case "CLEANUP_OLD_DATA":
                        cleanupOldData();
                        break;
                }
            }
        }
        return START_STICKY;
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    /**
     * Track a usage event
     */
    private void trackEvent(String eventType, String appName, String details) {
        CompletableFuture.runAsync(() -> {
            try {
                UsageEvent event = new UsageEvent();
                event.setEventType(eventType);
                event.setAppName(appName);
                event.setTimestamp(System.currentTimeMillis());
                event.setDetails(details);
                
                repository.insertUsageEvent(event);
                Log.d(TAG, "Event tracked: " + eventType + " for " + appName);
                
            } catch (Exception e) {
                Log.e(TAG, "Failed to track event: " + e.getMessage());
            }
        });
    }
    
    /**
     * Generate daily analytics report
     */
    private void generateDailyReport() {
        CompletableFuture.runAsync(() -> {
            try {
                // Get today's events
                long todayStart = getTodayStart();
                long todayEnd = getTodayEnd();
                
                repository.getAllUsageEvents().observeForever(events -> {
                    if (events != null) {
                        List<UsageEvent> todayEvents = filterEventsByTimeRange(events, todayStart, todayEnd);
                        
                        // Calculate daily metrics
                        int totalQuizzes = countEventsByType(todayEvents, "quiz_completed");
                        int totalSessions = countEventsByType(todayEvents, "session_completed");
                        int totalTimeSaved = calculateTimeSaved(todayEvents);
                        int streakDays = calculateCurrentStreak(events);
                        
                        // Log analytics
                        Log.i(TAG, "Daily Report - Quizzes: " + totalQuizzes + 
                              ", Sessions: " + totalSessions + 
                              ", Time Saved: " + totalTimeSaved + " minutes" +
                              ", Streak: " + streakDays + " days");
                        
                        // Track daily summary event
                        trackEvent("daily_summary", "Smart App Gatekeeper", 
                                 "Quizzes: " + totalQuizzes + ", Time Saved: " + totalTimeSaved + " min");
                    }
                });
                
            } catch (Exception e) {
                Log.e(TAG, "Failed to generate daily report: " + e.getMessage());
            }
        });
    }
    
    /**
     * Analyze usage patterns
     */
    private void analyzeUsagePatterns() {
        CompletableFuture.runAsync(() -> {
            try {
                repository.getAllUsageEvents().observeForever(events -> {
                    if (events != null) {
                        // Analyze peak usage hours
                        int[] hourlyUsage = new int[24];
                        for (UsageEvent event : events) {
                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(event.getTimestampLong());
                            int hour = cal.get(Calendar.HOUR_OF_DAY);
                            hourlyUsage[hour]++;
                        }
                        
                        // Find peak hours
                        int peakHour = 0;
                        int maxUsage = 0;
                        for (int i = 0; i < 24; i++) {
                            if (hourlyUsage[i] > maxUsage) {
                                maxUsage = hourlyUsage[i];
                                peakHour = i;
                            }
                        }
                        
                        Log.i(TAG, "Peak usage hour: " + peakHour + ":00 (" + maxUsage + " events)");
                        
                        // Analyze app usage patterns
                        analyzeAppUsagePatterns(events);
                        
                        // Analyze learning progress
                        analyzeLearningProgress(events);
                    }
                });
                
            } catch (Exception e) {
                Log.e(TAG, "Failed to analyze usage patterns: " + e.getMessage());
            }
        });
    }
    
    /**
     * Analyze app usage patterns
     */
    private void analyzeAppUsagePatterns(List<UsageEvent> events) {
        // Count events by app
        java.util.Map<String, Integer> appUsageCount = new java.util.HashMap<>();
        for (UsageEvent event : events) {
            String appName = event.getAppName();
            if (appName != null) {
                appUsageCount.put(appName, appUsageCount.getOrDefault(appName, 0) + 1);
            }
        }
        
        // Find most used apps
        String mostUsedApp = "";
        int maxCount = 0;
        for (java.util.Map.Entry<String, Integer> entry : appUsageCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostUsedApp = entry.getKey();
            }
        }
        
        Log.i(TAG, "Most used app: " + mostUsedApp + " (" + maxCount + " events)");
    }
    
    /**
     * Analyze learning progress
     */
    private void analyzeLearningProgress(List<UsageEvent> events) {
        int totalQuizzes = countEventsByType(events, "quiz_completed");
        int totalCorrectAnswers = countEventsByType(events, "correct_answer");
        int totalTimeSaved = calculateTimeSaved(events);
        
        double accuracy = totalQuizzes > 0 ? (double) totalCorrectAnswers / totalQuizzes * 100 : 0;
        
        Log.i(TAG, "Learning Progress - Quizzes: " + totalQuizzes + 
              ", Accuracy: " + String.format("%.1f", accuracy) + "%" +
              ", Time Saved: " + totalTimeSaved + " minutes");
    }
    
    /**
     * Cleanup old data
     */
    private void cleanupOldData() {
        CompletableFuture.runAsync(() -> {
            try {
                // Delete events older than 30 days
                long thirtyDaysAgo = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000);
                
                repository.getAllUsageEvents().observeForever(events -> {
                    if (events != null) {
                        int deletedCount = 0;
                        for (UsageEvent event : events) {
                            if (event.getTimestampLong() < thirtyDaysAgo) {
                                repository.deleteUsageEvent(event);
                                deletedCount++;
                            }
                        }
                        Log.i(TAG, "Cleaned up " + deletedCount + " old events");
                    }
                });
                
            } catch (Exception e) {
                Log.e(TAG, "Failed to cleanup old data: " + e.getMessage());
            }
        });
    }
    
    /**
     * Filter events by time range
     */
    private List<UsageEvent> filterEventsByTimeRange(List<UsageEvent> events, long startTime, long endTime) {
        List<UsageEvent> filtered = new java.util.ArrayList<>();
        for (UsageEvent event : events) {
            if (event.getTimestampLong() >= startTime && event.getTimestampLong() <= endTime) {
                filtered.add(event);
            }
        }
        return filtered;
    }
    
    /**
     * Count events by type
     */
    private int countEventsByType(List<UsageEvent> events, String eventType) {
        int count = 0;
        for (UsageEvent event : events) {
            if (eventType.equals(event.getEventType())) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Calculate total time saved
     */
    private int calculateTimeSaved(List<UsageEvent> events) {
        int timeSaved = 0;
        for (UsageEvent event : events) {
            if ("quiz_completed".equals(event.getEventType())) {
                timeSaved += 2; // 2 minutes per quiz
            }
        }
        return timeSaved;
    }
    
    /**
     * Calculate current streak
     */
    private int calculateCurrentStreak(List<UsageEvent> events) {
        // Simplified streak calculation
        // In a real app, this would be more sophisticated
        int streak = 0;
        long currentTime = System.currentTimeMillis();
        long dayInMillis = 24 * 60 * 60 * 1000;
        
        for (int i = 0; i < 30; i++) { // Check last 30 days
            long dayStart = currentTime - (i * dayInMillis);
            long dayEnd = dayStart + dayInMillis;
            
            boolean hasQuizToday = false;
            for (UsageEvent event : events) {
                if (event.getTimestampLong() >= dayStart && event.getTimestampLong() <= dayEnd &&
                    "quiz_completed".equals(event.getEventType())) {
                    hasQuizToday = true;
                    break;
                }
            }
            
            if (hasQuizToday) {
                streak++;
            } else {
                break;
            }
        }
        
        return streak;
    }
    
    /**
     * Get today's start time
     */
    private long getTodayStart() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
    
    /**
     * Get today's end time
     */
    private long getTodayEnd() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTimeInMillis();
    }
}
