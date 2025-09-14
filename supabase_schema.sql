-- Supabase Database Schema for Smart App Gatekeeper
-- Run this script in your Supabase SQL editor to create the required tables

-- Enable necessary extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create user_profiles table
CREATE TABLE IF NOT EXISTS user_profiles (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    age INTEGER,
    avatar_url TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Create target_apps table
CREATE TABLE IF NOT EXISTS target_apps (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES user_profiles(id) ON DELETE CASCADE,
    package_name VARCHAR(255) NOT NULL,
    app_name VARCHAR(255) NOT NULL,
    category VARCHAR(100),
    is_enabled BOOLEAN DEFAULT true,
    daily_limit_seconds INTEGER DEFAULT 7200, -- 2 hours
    per_unlock_seconds INTEGER DEFAULT 1800, -- 30 minutes
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(user_id, package_name)
);

-- Create questions table
CREATE TABLE IF NOT EXISTS questions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    question_text TEXT NOT NULL,
    option_a TEXT NOT NULL,
    option_b TEXT NOT NULL,
    option_c TEXT NOT NULL,
    option_d TEXT NOT NULL,
    correct_answer CHAR(1) NOT NULL CHECK (correct_answer IN ('A', 'B', 'C', 'D')),
    explanation TEXT,
    topic VARCHAR(100),
    difficulty VARCHAR(20) DEFAULT 'medium',
    source VARCHAR(50) DEFAULT 'manual',
    ai_model VARCHAR(100),
    time_limit_seconds INTEGER DEFAULT 30,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Create usage_events table
CREATE TABLE IF NOT EXISTS usage_events (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES user_profiles(id) ON DELETE CASCADE,
    package_name VARCHAR(255) NOT NULL,
    event_type VARCHAR(50) NOT NULL, -- 'app_opened', 'quiz_attempted', 'quiz_passed', 'session_ended'
    timestamp TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    duration_seconds INTEGER DEFAULT 0,
    quiz_attempted BOOLEAN DEFAULT false,
    quiz_passed BOOLEAN DEFAULT false,
    quiz_question_id UUID REFERENCES questions(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Create streaks table
CREATE TABLE IF NOT EXISTS streaks (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES user_profiles(id) ON DELETE CASCADE,
    streak_type VARCHAR(50) NOT NULL, -- 'daily', 'weekly', 'monthly'
    current_streak INTEGER DEFAULT 0,
    longest_streak INTEGER DEFAULT 0,
    last_activity_date DATE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(user_id, streak_type)
);

-- Create app_settings table
CREATE TABLE IF NOT EXISTS app_settings (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES user_profiles(id) ON DELETE CASCADE,
    setting_key VARCHAR(100) NOT NULL,
    setting_value TEXT,
    setting_type VARCHAR(20) DEFAULT 'string', -- 'string', 'integer', 'boolean', 'json'
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(user_id, setting_key)
);

-- Create quiz_results table
CREATE TABLE IF NOT EXISTS quiz_results (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES user_profiles(id) ON DELETE CASCADE,
    question_id UUID REFERENCES questions(id) ON DELETE CASCADE,
    selected_answer CHAR(1) CHECK (selected_answer IN ('A', 'B', 'C', 'D')),
    is_correct BOOLEAN NOT NULL,
    time_taken_seconds INTEGER,
    quiz_session_id UUID,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_target_apps_user_id ON target_apps(user_id);
CREATE INDEX IF NOT EXISTS idx_target_apps_package_name ON target_apps(package_name);
CREATE INDEX IF NOT EXISTS idx_usage_events_user_id ON usage_events(user_id);
CREATE INDEX IF NOT EXISTS idx_usage_events_timestamp ON usage_events(timestamp);
CREATE INDEX IF NOT EXISTS idx_usage_events_package_name ON usage_events(package_name);
CREATE INDEX IF NOT EXISTS idx_questions_topic ON questions(topic);
CREATE INDEX IF NOT EXISTS idx_questions_difficulty ON questions(difficulty);
CREATE INDEX IF NOT EXISTS idx_questions_is_active ON questions(is_active);
CREATE INDEX IF NOT EXISTS idx_streaks_user_id ON streaks(user_id);
CREATE INDEX IF NOT EXISTS idx_streaks_streak_type ON streaks(streak_type);
CREATE INDEX IF NOT EXISTS idx_quiz_results_user_id ON quiz_results(user_id);
CREATE INDEX IF NOT EXISTS idx_quiz_results_question_id ON quiz_results(question_id);
CREATE INDEX IF NOT EXISTS idx_quiz_results_created_at ON quiz_results(created_at);

-- Create updated_at trigger function
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create triggers for updated_at
CREATE TRIGGER update_user_profiles_updated_at BEFORE UPDATE ON user_profiles
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_target_apps_updated_at BEFORE UPDATE ON target_apps
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_questions_updated_at BEFORE UPDATE ON questions
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_streaks_updated_at BEFORE UPDATE ON streaks
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_app_settings_updated_at BEFORE UPDATE ON app_settings
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Enable Row Level Security (RLS)
ALTER TABLE user_profiles ENABLE ROW LEVEL SECURITY;
ALTER TABLE target_apps ENABLE ROW LEVEL SECURITY;
ALTER TABLE usage_events ENABLE ROW LEVEL SECURITY;
ALTER TABLE streaks ENABLE ROW LEVEL SECURITY;
ALTER TABLE app_settings ENABLE ROW LEVEL SECURITY;
ALTER TABLE quiz_results ENABLE ROW LEVEL SECURITY;

-- Create RLS policies
-- Users can only access their own data
CREATE POLICY "Users can view own profile" ON user_profiles
    FOR SELECT USING (auth.uid() = id);

CREATE POLICY "Users can update own profile" ON user_profiles
    FOR UPDATE USING (auth.uid() = id);

CREATE POLICY "Users can insert own profile" ON user_profiles
    FOR INSERT WITH CHECK (auth.uid() = id);

CREATE POLICY "Users can view own target apps" ON target_apps
    FOR SELECT USING (auth.uid() = user_id);

CREATE POLICY "Users can manage own target apps" ON target_apps
    FOR ALL USING (auth.uid() = user_id);

CREATE POLICY "Users can view own usage events" ON usage_events
    FOR SELECT USING (auth.uid() = user_id);

CREATE POLICY "Users can insert own usage events" ON usage_events
    FOR INSERT WITH CHECK (auth.uid() = user_id);

CREATE POLICY "Users can view own streaks" ON streaks
    FOR SELECT USING (auth.uid() = user_id);

CREATE POLICY "Users can manage own streaks" ON streaks
    FOR ALL USING (auth.uid() = user_id);

CREATE POLICY "Users can view own app settings" ON app_settings
    FOR SELECT USING (auth.uid() = user_id);

CREATE POLICY "Users can manage own app settings" ON app_settings
    FOR ALL USING (auth.uid() = user_id);

CREATE POLICY "Users can view own quiz results" ON quiz_results
    FOR SELECT USING (auth.uid() = user_id);

CREATE POLICY "Users can insert own quiz results" ON quiz_results
    FOR INSERT WITH CHECK (auth.uid() = user_id);

-- Questions table is public for reading (no RLS needed)
-- Users can read all questions but not modify them

-- Create a function to get user statistics
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
