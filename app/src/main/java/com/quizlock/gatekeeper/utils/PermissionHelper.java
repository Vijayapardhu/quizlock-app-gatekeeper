package com.quizlock.gatekeeper.utils;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityManager;

import java.util.List;

/**
 * Helper class for checking and managing app permissions
 */
public class PermissionHelper {
    
    /**
     * Check if accessibility service is enabled for our app
     */
    public static boolean hasAccessibilityPermission(Context context) {
        AccessibilityManager accessibilityManager = 
            (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        
        if (accessibilityManager == null) {
            return false;
        }
        
        List<AccessibilityServiceInfo> enabledServices = 
            accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);
        
        String packageName = context.getPackageName();
        String serviceName = packageName + "/.service.AppMonitoringService";
        
        for (AccessibilityServiceInfo service : enabledServices) {
            if (serviceName.equals(service.getId())) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Check if usage stats permission is granted
     */
    public static boolean hasUsageStatsPermission(Context context) {
        try {
            AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(), context.getPackageName());
            return mode == AppOpsManager.MODE_ALLOWED;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if overlay permission is granted (for showing quiz over other apps)
     */
    public static boolean hasOverlayPermission(Context context) {
        return Settings.canDrawOverlays(context);
    }
    
    /**
     * Check if notification permission is granted (Android 13+)
     */
    public static boolean hasNotificationPermission(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            return context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) 
                == PackageManager.PERMISSION_GRANTED;
        }
        return true; // Not required for older versions
    }
    
    /**
     * Get list of required permissions that are missing
     */
    public static String[] getMissingPermissions(Context context) {
        java.util.List<String> missing = new java.util.ArrayList<>();
        
        if (!hasAccessibilityPermission(context)) {
            missing.add("Accessibility Service");
        }
        
        if (!hasUsageStatsPermission(context)) {
            missing.add("Usage Access");
        }
        
        if (!hasOverlayPermission(context)) {
            missing.add("Display over other apps");
        }
        
        if (!hasNotificationPermission(context)) {
            missing.add("Notifications");
        }
        
        return missing.toArray(new String[0]);
    }
    
    /**
     * Check if all required permissions are granted
     */
    public static boolean hasAllRequiredPermissions(Context context) {
        return hasAccessibilityPermission(context) && 
               hasUsageStatsPermission(context) && 
               hasOverlayPermission(context) && 
               hasNotificationPermission(context);
    }
}