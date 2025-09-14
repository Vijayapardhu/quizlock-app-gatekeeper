# Supabase Setup Guide for Smart App Gatekeeper

This guide will help you set up Supabase for your Smart App Gatekeeper Android app.

## 🚀 Quick Start

### 1. Create Supabase Project

1. Go to [supabase.com](https://supabase.com)
2. Sign up or log in to your account
3. Click "New Project"
4. Choose your organization
5. Enter project details:
   - **Name**: `smart-app-gatekeeper`
   - **Database Password**: Generate a strong password
   - **Region**: Choose closest to your users
6. Click "Create new project"

### 2. Get Your Project Credentials

1. Go to **Settings** → **API**
2. Copy the following values:
   - **Project URL**: `https://your-project-id.supabase.co`
   - **Anon Key**: `eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`
   - **Service Role Key**: `eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`

### 3. Update App Configuration

Update `app/src/main/java/com/smartappgatekeeper/config/AppConfig.java`:

```java
// Replace with your actual Supabase credentials
public static final String SUPABASE_URL = "https://your-project-id.supabase.co";
public static final String SUPABASE_ANON_KEY = "your-anon-key-here";
public static final String SUPABASE_SERVICE_ROLE_KEY = "your-service-role-key-here";
```

## 🗄️ Database Setup

### 1. Run the Schema Script

1. Go to **SQL Editor** in your Supabase dashboard
2. Copy and paste the contents of `supabase_schema.sql`
3. Click **Run** to create all tables and indexes

### 2. Fix Migration Issues (if needed)

If you encounter column errors, run the migration fix:

1. Copy and paste the contents of `supabase_migration_fix.sql`
2. Click **Run** to fix any missing columns

## 🔐 Authentication Setup

### 1. Enable Email Authentication

1. Go to **Authentication** → **Settings**
2. Under **Auth Providers**, enable **Email**
3. Configure email templates if needed

### 2. Configure Row Level Security (RLS)

The schema automatically sets up RLS policies. Verify they're enabled:

1. Go to **Authentication** → **Policies**
2. Ensure all tables have RLS enabled
3. Check that policies are created for each table

## 📱 App Integration

### 1. Test Authentication

The app includes these Supabase services:

- **SupabaseAuthService**: User registration and login
- **SupabaseDatabaseService**: Data synchronization
- **SupabaseSyncService**: Background sync management

### 2. Enable Sync

To enable data synchronization:

```java
// Initialize Supabase services
SupabaseAuthService authService = SupabaseAuthService.getInstance(context);
SupabaseSyncService syncService = SupabaseSyncService.getInstance(context);

// Sign in user
authService.signInUser(email, password)
    .thenAccept(result -> {
        if (result.isSuccess()) {
            // User signed in, start syncing
            syncService.syncAllData();
        }
    });
```

## 🛠️ Troubleshooting

### Common Issues

#### 1. "Column does not exist" Error

**Solution**: Run the migration fix script:
```sql
-- Run supabase_migration_fix.sql in SQL Editor
```

#### 2. Authentication Failed

**Check**:
- Project URL is correct
- Anon key is correct
- Email authentication is enabled
- User exists in Supabase

#### 3. RLS Policy Errors

**Solution**: Ensure RLS policies are created:
```sql
-- Check if policies exist
SELECT * FROM pg_policies WHERE tablename = 'usage_events';
```

### Debug Mode

Enable debug logging in your app:

```java
// In your Application class or MainActivity
if (BuildConfig.DEBUG) {
    Log.d("Supabase", "URL: " + SupabaseConfig.SUPABASE_URL);
    Log.d("Supabase", "Anon Key: " + SupabaseConfig.SUPABASE_ANON_KEY);
}
```

## 📊 Database Schema

### Tables Created

1. **user_profiles**: User information and preferences
2. **target_apps**: Apps to be restricted
3. **questions**: Quiz questions and answers
4. **usage_events**: App usage and quiz attempt logs
5. **streaks**: Learning streak tracking
6. **app_settings**: User app configuration
7. **quiz_results**: Individual quiz attempt results

### Key Features

- **UUID Primary Keys**: All tables use UUID for better scalability
- **Timestamps**: Automatic created_at and updated_at tracking
- **RLS Security**: Row-level security for data isolation
- **Indexes**: Optimized for common queries
- **Foreign Keys**: Proper relationships between tables

## 🔄 Data Synchronization

### Sync Process

1. **Local First**: App works offline with local Room database
2. **Background Sync**: Data syncs to Supabase when online
3. **Conflict Resolution**: Last-write-wins strategy
4. **Real-time Updates**: Optional real-time subscriptions

### Sync Triggers

- User sign in/out
- App foreground/background
- Manual sync button
- Periodic background sync

## 🚀 Production Deployment

### 1. Environment Variables

For production, use environment variables:

```java
public static final String SUPABASE_URL = BuildConfig.SUPABASE_URL;
public static final String SUPABASE_ANON_KEY = BuildConfig.SUPABASE_ANON_KEY;
```

### 2. Security Considerations

- Never commit service role keys to version control
- Use different projects for development and production
- Enable additional security features in Supabase
- Monitor usage and set up alerts

### 3. Performance Optimization

- Enable connection pooling
- Set up proper indexes
- Monitor query performance
- Use Supabase Edge Functions for complex operations

## 📈 Monitoring

### Supabase Dashboard

Monitor your app's usage:

1. **Database**: Query performance and usage
2. **Authentication**: User sign-ups and logins
3. **API**: Request logs and errors
4. **Storage**: File uploads and usage

### App Analytics

The app includes built-in analytics:

- User engagement metrics
- Quiz performance tracking
- App usage patterns
- Learning progress

## 🆘 Support

### Resources

- [Supabase Documentation](https://supabase.com/docs)
- [Supabase Community](https://github.com/supabase/supabase/discussions)
- [Android Integration Guide](https://supabase.com/docs/guides/getting-started/tutorials/with-android)

### Common Commands

```sql
-- Check table structure
\d usage_events

-- View all policies
SELECT * FROM pg_policies;

-- Check RLS status
SELECT schemaname, tablename, rowsecurity 
FROM pg_tables 
WHERE tablename = 'usage_events';

-- Reset user data (development only)
DELETE FROM usage_events WHERE user_id = 'user-uuid-here';
```

---

**Note**: This setup provides a complete backend solution for your Smart App Gatekeeper app with authentication, database, and real-time capabilities. The app will work offline and sync data when online.
