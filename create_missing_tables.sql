-- Create Missing Tables for Smart App Gatekeeper
-- Run this in your Supabase SQL Editor

-- 1. Create user_profiles table
CREATE TABLE IF NOT EXISTS public.user_profiles (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id TEXT UNIQUE NOT NULL,
    email TEXT UNIQUE NOT NULL,
    name TEXT,
    avatar_url TEXT,
    coins INTEGER DEFAULT 0,
    level INTEGER DEFAULT 1,
    experience_points INTEGER DEFAULT 0,
    streak_days INTEGER DEFAULT 0,
    total_quizzes_completed INTEGER DEFAULT 0,
    average_quiz_score REAL DEFAULT 0.0,
    preferred_topics TEXT[],
    preferred_difficulty TEXT DEFAULT 'medium',
    daily_goal_minutes INTEGER DEFAULT 30,
    notification_enabled BOOLEAN DEFAULT true,
    dark_mode_enabled BOOLEAN DEFAULT false,
    auto_sync_enabled BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- 2. Create streaks table
CREATE TABLE IF NOT EXISTS public.streaks (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id TEXT NOT NULL,
    streak_type TEXT NOT NULL, -- 'daily', 'weekly', 'monthly'
    current_streak INTEGER DEFAULT 0,
    longest_streak INTEGER DEFAULT 0,
    last_activity_date DATE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(user_id, streak_type)
);

-- 3. Create app_settings table
CREATE TABLE IF NOT EXISTS public.app_settings (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id TEXT NOT NULL,
    setting_key TEXT NOT NULL,
    setting_value TEXT,
    setting_type TEXT DEFAULT 'string', -- 'string', 'boolean', 'integer', 'json'
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(user_id, setting_key)
);

-- 4. Create quiz_results table
CREATE TABLE IF NOT EXISTS public.quiz_results (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id TEXT NOT NULL,
    quiz_id TEXT,
    questions_answered INTEGER DEFAULT 0,
    correct_answers INTEGER DEFAULT 0,
    total_score REAL DEFAULT 0.0,
    time_taken_seconds INTEGER DEFAULT 0,
    difficulty_level TEXT DEFAULT 'medium',
    topics TEXT[],
    completed_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- 5. Create friends table
CREATE TABLE IF NOT EXISTS public.friends (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id TEXT NOT NULL,
    friend_id TEXT NOT NULL,
    status TEXT DEFAULT 'pending', -- 'pending', 'accepted', 'blocked'
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(user_id, friend_id)
);

-- 6. Create achievements table
CREATE TABLE IF NOT EXISTS public.achievements (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id TEXT NOT NULL,
    achievement_type TEXT NOT NULL,
    achievement_name TEXT NOT NULL,
    description TEXT,
    points_earned INTEGER DEFAULT 0,
    unlocked_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- 7. Create learning_paths table
CREATE TABLE IF NOT EXISTS public.learning_paths (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id TEXT NOT NULL,
    path_name TEXT NOT NULL,
    description TEXT,
    difficulty_level TEXT DEFAULT 'beginner',
    estimated_hours INTEGER DEFAULT 0,
    progress_percentage REAL DEFAULT 0.0,
    is_completed BOOLEAN DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- 8. Create learning_modules table
CREATE TABLE IF NOT EXISTS public.learning_modules (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    learning_path_id UUID REFERENCES public.learning_paths(id) ON DELETE CASCADE,
    module_name TEXT NOT NULL,
    description TEXT,
    order_index INTEGER DEFAULT 0,
    is_completed BOOLEAN DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_user_profiles_user_id ON public.user_profiles(user_id);
CREATE INDEX IF NOT EXISTS idx_user_profiles_email ON public.user_profiles(email);
CREATE INDEX IF NOT EXISTS idx_streaks_user_id ON public.streaks(user_id);
CREATE INDEX IF NOT EXISTS idx_app_settings_user_id ON public.app_settings(user_id);
CREATE INDEX IF NOT EXISTS idx_quiz_results_user_id ON public.quiz_results(user_id);
CREATE INDEX IF NOT EXISTS idx_friends_user_id ON public.friends(user_id);
CREATE INDEX IF NOT EXISTS idx_achievements_user_id ON public.achievements(user_id);
CREATE INDEX IF NOT EXISTS idx_learning_paths_user_id ON public.learning_paths(user_id);
CREATE INDEX IF NOT EXISTS idx_learning_modules_path_id ON public.learning_modules(learning_path_id);

-- Enable Row Level Security (RLS)
ALTER TABLE public.user_profiles ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.streaks ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.app_settings ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.quiz_results ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.friends ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.achievements ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.learning_paths ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.learning_modules ENABLE ROW LEVEL SECURITY;

-- Create RLS policies (basic policies for now)
-- Note: You may need to adjust these based on your authentication setup

-- User profiles policies
CREATE POLICY "Users can view their own profile" ON public.user_profiles
    FOR SELECT USING (true); -- Adjust based on your auth setup

CREATE POLICY "Users can insert their own profile" ON public.user_profiles
    FOR INSERT WITH CHECK (true); -- Adjust based on your auth setup

CREATE POLICY "Users can update their own profile" ON public.user_profiles
    FOR UPDATE USING (true); -- Adjust based on your auth setup

-- Streaks policies
CREATE POLICY "Users can view their own streaks" ON public.streaks
    FOR SELECT USING (true);

CREATE POLICY "Users can insert their own streaks" ON public.streaks
    FOR INSERT WITH CHECK (true);

CREATE POLICY "Users can update their own streaks" ON public.streaks
    FOR UPDATE USING (true);

-- App settings policies
CREATE POLICY "Users can view their own settings" ON public.app_settings
    FOR SELECT USING (true);

CREATE POLICY "Users can insert their own settings" ON public.app_settings
    FOR INSERT WITH CHECK (true);

CREATE POLICY "Users can update their own settings" ON public.app_settings
    FOR UPDATE USING (true);

-- Quiz results policies
CREATE POLICY "Users can view their own quiz results" ON public.quiz_results
    FOR SELECT USING (true);

CREATE POLICY "Users can insert their own quiz results" ON public.quiz_results
    FOR INSERT WITH CHECK (true);

-- Friends policies
CREATE POLICY "Users can view their own friends" ON public.friends
    FOR SELECT USING (true);

CREATE POLICY "Users can insert their own friends" ON public.friends
    FOR INSERT WITH CHECK (true);

CREATE POLICY "Users can update their own friends" ON public.friends
    FOR UPDATE USING (true);

-- Achievements policies
CREATE POLICY "Users can view their own achievements" ON public.achievements
    FOR SELECT USING (true);

CREATE POLICY "Users can insert their own achievements" ON public.achievements
    FOR INSERT WITH CHECK (true);

-- Learning paths policies
CREATE POLICY "Users can view their own learning paths" ON public.learning_paths
    FOR SELECT USING (true);

CREATE POLICY "Users can insert their own learning paths" ON public.learning_paths
    FOR INSERT WITH CHECK (true);

CREATE POLICY "Users can update their own learning paths" ON public.learning_paths
    FOR UPDATE USING (true);

-- Learning modules policies
CREATE POLICY "Users can view their own learning modules" ON public.learning_modules
    FOR SELECT USING (true);

CREATE POLICY "Users can insert their own learning modules" ON public.learning_modules
    FOR INSERT WITH CHECK (true);

CREATE POLICY "Users can update their own learning modules" ON public.learning_modules
    FOR UPDATE USING (true);

-- Insert some sample data
INSERT INTO public.user_profiles (user_id, email, name, coins, level, experience_points, streak_days, total_quizzes_completed, average_quiz_score, preferred_topics, preferred_difficulty, daily_goal_minutes, notification_enabled, dark_mode_enabled, auto_sync_enabled) 
VALUES 
('demo-user-1', 'demo@example.com', 'Demo User', 1000, 5, 2500, 7, 25, 85.5, ARRAY['programming', 'mathematics'], 'medium', 60, true, false, true),
('test-user-1', 'test@example.com', 'Test User', 500, 3, 1200, 3, 12, 78.0, ARRAY['science', 'technology'], 'easy', 30, true, true, true)
ON CONFLICT (user_id) DO NOTHING;

-- Insert sample streaks
INSERT INTO public.streaks (user_id, streak_type, current_streak, longest_streak, last_activity_date)
VALUES 
('demo-user-1', 'daily', 7, 15, CURRENT_DATE),
('test-user-1', 'daily', 3, 8, CURRENT_DATE)
ON CONFLICT (user_id, streak_type) DO NOTHING;

-- Insert sample achievements
INSERT INTO public.achievements (user_id, achievement_type, achievement_name, description, points_earned)
VALUES 
('demo-user-1', 'quiz', 'Quiz Master', 'Completed 25 quizzes', 250),
('demo-user-1', 'streak', 'Week Warrior', 'Maintained 7-day streak', 100),
('test-user-1', 'quiz', 'Quick Learner', 'Completed 10 quizzes', 100),
('test-user-1', 'streak', 'Getting Started', 'Maintained 3-day streak', 50)
ON CONFLICT DO NOTHING;

-- Insert sample learning paths
INSERT INTO public.learning_paths (user_id, path_name, description, difficulty_level, estimated_hours, progress_percentage, is_completed)
VALUES 
('demo-user-1', 'Python Programming', 'Complete Python programming course', 'intermediate', 40, 65.0, false),
('demo-user-1', 'Data Science', 'Data science fundamentals', 'advanced', 60, 25.0, false),
('test-user-1', 'Web Development', 'Learn web development basics', 'beginner', 30, 40.0, false)
ON CONFLICT DO NOTHING;

-- Insert sample learning modules
INSERT INTO public.learning_modules (learning_path_id, module_name, description, order_index, is_completed)
SELECT 
    lp.id,
    'Introduction to ' || lp.path_name,
    'Get started with ' || lp.path_name,
    1,
    CASE WHEN lp.progress_percentage > 0 THEN true ELSE false END
FROM public.learning_paths lp
ON CONFLICT DO NOTHING;

-- Insert sample quiz results
INSERT INTO public.quiz_results (user_id, quiz_id, questions_answered, correct_answers, total_score, time_taken_seconds, difficulty_level, topics)
VALUES 
('demo-user-1', 'quiz-001', 10, 8, 80.0, 300, 'medium', ARRAY['python', 'programming']),
('demo-user-1', 'quiz-002', 15, 12, 80.0, 450, 'hard', ARRAY['algorithms', 'data-structures']),
('test-user-1', 'quiz-003', 8, 6, 75.0, 240, 'easy', ARRAY['html', 'css']),
('test-user-1', 'quiz-004', 12, 9, 75.0, 360, 'medium', ARRAY['javascript', 'web-development'])
ON CONFLICT DO NOTHING;

-- Insert sample app settings
INSERT INTO public.app_settings (user_id, setting_key, setting_value, setting_type)
VALUES 
('demo-user-1', 'notifications_enabled', 'true', 'boolean'),
('demo-user-1', 'dark_mode', 'false', 'boolean'),
('demo-user-1', 'daily_reminder_time', '09:00', 'string'),
('test-user-1', 'notifications_enabled', 'true', 'boolean'),
('test-user-1', 'dark_mode', 'true', 'boolean'),
('test-user-1', 'daily_reminder_time', '18:00', 'string')
ON CONFLICT (user_id, setting_key) DO NOTHING;

-- Create a function to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create triggers for updated_at
CREATE TRIGGER update_user_profiles_updated_at BEFORE UPDATE ON public.user_profiles FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_streaks_updated_at BEFORE UPDATE ON public.streaks FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_app_settings_updated_at BEFORE UPDATE ON public.app_settings FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_learning_paths_updated_at BEFORE UPDATE ON public.learning_paths FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_learning_modules_updated_at BEFORE UPDATE ON public.learning_modules FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Grant necessary permissions
GRANT USAGE ON SCHEMA public TO anon, authenticated;
GRANT ALL ON ALL TABLES IN SCHEMA public TO anon, authenticated;
GRANT ALL ON ALL SEQUENCES IN SCHEMA public TO anon, authenticated;
