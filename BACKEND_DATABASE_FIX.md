# Backend Database Fix Instructions

## Problem
The Android app is getting an error: `ERROR: 42703: column "app_package" does not exist`

This happens because there's a mismatch between the database schema and the code expectations.

## Root Cause
The database migration file was trying to create an index on `app_package` column, but the actual column name in the schema is `package_name`.

## Solution

### Step 1: Run the Corrected Migration Script

Execute the `supabase_migration_fix_corrected.sql` script in your Supabase SQL editor:

```sql
-- This script will:
-- 1. Create the usage_events table if it doesn't exist
-- 2. Rename app_package column to package_name if it exists
-- 3. Add missing columns if needed
-- 4. Create correct indexes
-- 5. Set up proper RLS policies
-- 6. Update the get_user_stats function
```

### Step 2: Verify the Fix

Run the `test_database_connection.sql` script to verify everything is working:

```sql
-- This will check:
-- 1. All required tables exist
-- 2. usage_events table has correct structure
-- 3. package_name column exists (not app_package)
-- 4. Indexes are correct
-- 5. RLS policies are set up
-- 6. Functions work properly
```

### Step 3: Test the Android App

After running the migration scripts:

1. **Build the app**: `.\gradlew assembleDebug`
2. **Install on device/emulator**: The app should now connect to Supabase without errors
3. **Test database operations**: Try logging usage events, syncing data, etc.

## Database Schema Summary

The corrected schema uses these column names:

### usage_events table:
- `id` (UUID, Primary Key)
- `user_id` (UUID, Foreign Key to user_profiles)
- `package_name` (VARCHAR(255)) ← **This is the correct column name**
- `event_type` (VARCHAR(50))
- `timestamp` (TIMESTAMP WITH TIME ZONE)
- `duration_seconds` (INTEGER)
- `quiz_attempted` (BOOLEAN)
- `quiz_passed` (BOOLEAN)
- `quiz_question_id` (UUID, Foreign Key to questions)
- `created_at` (TIMESTAMP WITH TIME ZONE)

### Key Indexes:
- `idx_usage_events_user_id` on `user_id`
- `idx_usage_events_timestamp` on `timestamp`
- `idx_usage_events_package_name` on `package_name` ← **Correct column name**

## Android Code Compatibility

The Android app is already correctly configured:

- ✅ `UsageEvent.java` entity uses `packageName` field
- ✅ `SupabaseDatabaseService.java` uses `package_name` in JSON
- ✅ All database operations use the correct column name

## Troubleshooting

If you still get errors after running the migration:

1. **Check column names**: Run the test script to verify `package_name` exists
2. **Check indexes**: Make sure the index is on `package_name`, not `app_package`
3. **Check RLS policies**: Ensure policies allow your user to access the data
4. **Check Supabase credentials**: Verify your `AppConfig.java` has correct Supabase URL and keys

## Files Modified

- `supabase_migration_fix_corrected.sql` - Corrected migration script
- `test_database_connection.sql` - Test script to verify the fix
- `BACKEND_DATABASE_FIX.md` - This documentation

## Next Steps

After fixing the database:

1. Test all app features that interact with Supabase
2. Verify data synchronization works
3. Check that analytics and reporting features work
4. Test user authentication and data access

The app should now work correctly with the Supabase backend! 🚀
