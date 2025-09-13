package com.vijayapardhu.quizlock.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppUtils {
    
    public static String getAppName(Context context, String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(packageName, 0);
            return pm.getApplicationLabel(appInfo).toString();
        } catch (PackageManager.NameNotFoundException e) {
            return packageName;
        }
    }
    
    public static Drawable getAppIcon(Context context, String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            return pm.getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }
    
    public static List<InstalledApp> getInstalledApps(Context context) {
        List<InstalledApp> apps = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> installedApps = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        
        for (ApplicationInfo appInfo : installedApps) {
            // Filter out system apps unless they're commonly restricted
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 || 
                isPopularApp(appInfo.packageName)) {
                
                String appName = pm.getApplicationLabel(appInfo).toString();
                apps.add(new InstalledApp(appInfo.packageName, appName));
            }
        }
        
        // Sort alphabetically
        apps.sort((a, b) -> a.appName.compareToIgnoreCase(b.appName));
        
        return apps;
    }
    
    private static boolean isPopularApp(String packageName) {
        for (String popularApp : Constants.POPULAR_APPS) {
            if (popularApp.equals(packageName)) {
                return true;
            }
        }
        return false;
    }
    
    public static String formatDuration(int minutes) {
        if (minutes < 60) {
            return minutes + " min";
        } else {
            int hours = minutes / 60;
            int remainingMinutes = minutes % 60;
            if (remainingMinutes == 0) {
                return hours + " hr";
            } else {
                return hours + " hr " + remainingMinutes + " min";
            }
        }
    }
    
    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault());
        return sdf.format(new Date());
    }
    
    public static String formatDateTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIME_FORMAT, Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
    
    public static boolean isNewDay(long lastTimestamp) {
        String lastDate = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault())
            .format(new Date(lastTimestamp));
        String currentDate = getCurrentDate();
        return !lastDate.equals(currentDate);
    }
    
    public static class InstalledApp {
        public String packageName;
        public String appName;
        
        public InstalledApp(String packageName, String appName) {
            this.packageName = packageName;
            this.appName = appName;
        }
    }
}