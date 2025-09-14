-- Supabase Migration Fix - Corrected Version
-- Run this script to fix the column naming issues

-- First, let's check if the usage_events table exists and what columns it has
-- If the table doesn't exist, create it
CREATE TABLE IF NOT EXISTS usage_events (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES user_profiles(id) ON DELETE CASCADE,
    package_name VARCHAR(255) NOT NULL,  -- Correct column name
    event_type VARCHAR(50) NOT NULL,
    timestamp TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    duration_seconds INTEGER DEFAULT 0,
    quiz_attempted BOOLEAN DEFAULT false,
    quiz_passed BOOLEAN DEFAULT false,
    quiz_question_id UUID REFERENCES questions(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- If the table exists but has the wrong column name, rename it
DO $$ 
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'usage_events' AND column_name = 'app_package'
    ) THEN
        ALTER TABLE usage_events RENAME COLUMN app_package TO package_name;
    END IF;
END $$;

-- If the table exists but is missing the timestamp column, add it
DO $$ 
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'usage_events' AND column_name = 'timestamp'
    ) THEN
        ALTER TABLE usage_events ADD COLUMN timestamp TIMESTAMP WITH TIME ZONE DEFAULT NOW();
    END IF;
END $$;

-- If the table exists but is missing other columns, add them
DO $$ 
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'usage_events' AND column_name = 'duration_seconds'
    ) THEN
        ALTER TABLE usage_events ADD COLUMN duration_seconds INTEGER DEFAULT 0;
    END IF;
END $$;

DO $$ 
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'usage_events' AND column_name = 'quiz_attempted'
    ) THEN
        ALTER TABLE usage_events ADD COLUMN quiz_attempted BOOLEAN DEFAULT false;
    END IF;
END $$;

DO $$ 
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'usage_events' AND column_name = 'quiz_passed'
    ) THEN
        ALTER TABLE usage_events ADD COLUMN quiz_passed BOOLEAN DEFAULT false;
    END IF;
END $$;

DO $$ 
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'usage_events' AND column_name = 'quiz_question_id'
    ) THEN
        ALTER TABLE usage_events ADD COLUMN quiz_question_id UUID REFERENCES questions(id);
    END IF;
END $$;

DO $$ 
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'usage_events' AND column_name = 'created_at'
    ) THEN
        ALTER TABLE usage_events ADD COLUMN created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW();
    END IF;
END $$;

-- Drop the incorrect index if it exists
DROP INDEX IF EXISTS idx_usage_events_app_package;

-- Create correct indexes if they don't exist
CREATE INDEX IF NOT EXISTS idx_usage_events_user_id ON usage_events(user_id);
CREATE INDEX IF NOT EXISTS idx_usage_events_timestamp ON usage_events(timestamp);
CREATE INDEX IF NOT EXISTS idx_usage_events_package_name ON usage_events(package_name);  -- Correct column name

-- Enable RLS if not already enabled
ALTER TABLE usage_events ENABLE ROW LEVEL SECURITY;

-- Create RLS policies if they don't exist
DO $$ 
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_policies 
        WHERE tablename = 'usage_events' AND policyname = 'Users can view own usage events'
    ) THEN
        CREATE POLICY "Users can view own usage events" ON usage_events
            FOR SELECT USING (auth.uid() = user_id);
    END IF;
END $$;

DO $$ 
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_policies 
        WHERE tablename = 'usage_events' AND policyname = 'Users can insert own usage events'
    ) THEN
        CREATE POLICY "Users can insert own usage events" ON usage_events
            FOR INSERT WITH CHECK (auth.uid() = user_id);
    END IF;
END $$;

-- Update the get_user_stats function to use the correct column name
CREATE OR REPLACE FUNCTION get_user_stats(user_uuid UUID)
RETURNS JSON AS $$
DECLARE
    result JSON;
BEGIN
    SELECT json_build_object(
        'total_quiz_attempts', COALESCE(quiz_stats.total_attempts, 0),
        'correct_answers', COALESCE(quiz_stats.correct_answers, 0),
        'current_streak', COALESCE(streak_stats.current_streak, 0),
        'longest_streak', COALESCE(streak_stats.longest_streak, 0),
        'total_app_usage_time', COALESCE(usage_stats.total_seconds, 0),
        'apps_restricted', COALESCE(app_stats.restricted_count, 0)
    ) INTO result
    FROM (
        SELECT 
            COUNT(*) as total_attempts,
            COUNT(*) FILTER (WHERE is_correct = true) as correct_answers
        FROM quiz_results 
        WHERE user_id = user_uuid
    ) quiz_stats
    CROSS JOIN (
        SELECT 
            COALESCE(MAX(current_streak), 0) as current_streak,
            COALESCE(MAX(longest_streak), 0) as longest_streak
        FROM streaks 
        WHERE user_id = user_uuid AND streak_type = 'daily'
    ) streak_stats
    CROSS JOIN (
        SELECT 
            COALESCE(SUM(duration_seconds), 0) as total_seconds
        FROM usage_events 
        WHERE user_id = user_uuid AND event_type = 'session_ended'
    ) usage_stats
    CROSS JOIN (
        SELECT 
            COUNT(*) as restricted_count
        FROM target_apps 
        WHERE user_id = user_uuid AND is_enabled = true
    ) app_stats;
    
    RETURN result;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

-- Grant necessary permissions
GRANT USAGE ON SCHEMA public TO anon, authenticated;
GRANT ALL ON ALL TABLES IN SCHEMA public TO anon, authenticated;
GRANT ALL ON ALL SEQUENCES IN SCHEMA public TO anon, authenticated;
GRANT EXECUTE ON FUNCTION get_user_stats(UUID) TO anon, authenticated;
