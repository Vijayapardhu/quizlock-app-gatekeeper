-- Test Database Connection and Schema
-- Run this script to verify the database is working correctly

-- Test 1: Check if all required tables exist
SELECT 
    table_name,
    CASE 
        WHEN table_name IN ('user_profiles', 'target_apps', 'questions', 'usage_events', 'streaks', 'app_settings', 'quiz_results') 
        THEN '✓ EXISTS' 
        ELSE '✗ MISSING' 
    END as status
FROM information_schema.tables 
WHERE table_schema = 'public' 
AND table_name IN ('user_profiles', 'target_apps', 'questions', 'usage_events', 'streaks', 'app_settings', 'quiz_results')
ORDER BY table_name;

-- Test 2: Check usage_events table structure
SELECT 
    column_name,
    data_type,
    is_nullable,
    column_default
FROM information_schema.columns 
WHERE table_name = 'usage_events' 
AND table_schema = 'public'
ORDER BY ordinal_position;

-- Test 3: Check if the correct column name exists
SELECT 
    CASE 
        WHEN EXISTS (
            SELECT 1 FROM information_schema.columns 
            WHERE table_name = 'usage_events' AND column_name = 'package_name'
        ) THEN '✓ package_name column exists'
        ELSE '✗ package_name column missing'
    END as package_name_status,
    CASE 
        WHEN EXISTS (
            SELECT 1 FROM information_schema.columns 
            WHERE table_name = 'usage_events' AND column_name = 'app_package'
        ) THEN '⚠️ app_package column exists (should be renamed)'
        ELSE '✓ app_package column does not exist'
    END as app_package_status;

-- Test 4: Check indexes
SELECT 
    indexname,
    tablename,
    indexdef
FROM pg_indexes 
WHERE tablename = 'usage_events'
ORDER BY indexname;

-- Test 5: Check RLS policies
SELECT 
    schemaname,
    tablename,
    policyname,
    permissive,
    roles,
    cmd,
    qual
FROM pg_policies 
WHERE tablename = 'usage_events'
ORDER BY policyname;

-- Test 6: Test the get_user_stats function
SELECT get_user_stats('00000000-0000-0000-0000-000000000000'::UUID) as test_stats;
