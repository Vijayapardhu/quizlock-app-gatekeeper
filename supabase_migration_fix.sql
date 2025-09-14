-- Supabase Migration Fix
-- Run this script to fix the timestamp column issue

-- First, let's check if the usage_events table exists and what columns it has
-- If the table doesn't exist, create it
CREATE TABLE IF NOT EXISTS usage_events (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES user_profiles(id) ON DELETE CASCADE,
    package_name VARCHAR(255) NOT NULL,  -- Changed from app_package to package_name
    event_type VARCHAR(50) NOT NULL,
    timestamp TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    duration_seconds INTEGER DEFAULT 0,
    quiz_attempted BOOLEAN DEFAULT false,
    quiz_passed BOOLEAN DEFAULT false,
    quiz_question_id UUID REFERENCES questions(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

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

-- Create indexes if they don't exist
CREATE INDEX IF NOT EXISTS idx_usage_events_user_id ON usage_events(user_id);
CREATE INDEX IF NOT EXISTS idx_usage_events_timestamp ON usage_events(timestamp);
CREATE INDEX IF NOT EXISTS idx_usage_events_app_package ON usage_events(app_package);

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
