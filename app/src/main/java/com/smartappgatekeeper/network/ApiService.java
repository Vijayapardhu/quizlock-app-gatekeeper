package com.smartappgatekeeper.network;

import retrofit2.Call;
import retrofit2.http.*;
import java.util.List;
import com.smartappgatekeeper.network.models.*;

/**
 * API Service interface for Retrofit
 * Defines all API endpoints and request/response models
 */
public interface ApiService {
    
    // Authentication endpoints
    @POST("auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);
    
    @POST("auth/register")
    Call<AuthResponse> register(@Body RegisterRequest request);
    
    @POST("auth/refresh")
    Call<AuthResponse> refreshToken(@Body RefreshTokenRequest request);
    
    @POST("auth/logout")
    Call<Void> logout(@Header("Authorization") String token);
    
    // User endpoints
    @GET("users/profile")
    Call<UserProfileResponse> getUserProfile(@Header("Authorization") String token);
    
    @PUT("users/profile")
    Call<UserProfileResponse> updateUserProfile(@Header("Authorization") String token, @Body UserProfileRequest request);
    
    @GET("users/stats")
    Call<UserStatsResponse> getUserStats(@Header("Authorization") String token);
    
    // Quiz endpoints
    @GET("quiz/questions")
    Call<List<QuestionResponse>> getQuestions(@Header("Authorization") String token, 
                                            @Query("difficulty") int difficulty,
                                            @Query("count") int count);
    
    @POST("quiz/submit")
    Call<QuizResultResponse> submitQuiz(@Header("Authorization") String token, @Body QuizSubmissionRequest request);
    
    @GET("quiz/history")
    Call<List<QuizHistoryResponse>> getQuizHistory(@Header("Authorization") String token,
                                                  @Query("page") int page,
                                                  @Query("limit") int limit);
    
    // Analytics endpoints
    @GET("analytics/usage")
    Call<UsageAnalyticsResponse> getUsageAnalytics(@Header("Authorization") String token,
                                                  @Query("period") String period);
    
    @GET("analytics/progress")
    Call<ProgressAnalyticsResponse> getProgressAnalytics(@Header("Authorization") String token,
                                                        @Query("start_date") String startDate,
                                                        @Query("end_date") String endDate);
    
    // Achievements endpoints
    @GET("achievements")
    Call<List<AchievementResponse>> getAchievements(@Header("Authorization") String token);
    
    @POST("achievements/{id}/claim")
    Call<AchievementResponse> claimAchievement(@Header("Authorization") String token, @Path("id") String achievementId);
    
    // Store endpoints
    @GET("store/items")
    Call<List<StoreItemResponse>> getStoreItems(@Header("Authorization") String token);
    
    @POST("store/purchase")
    Call<PurchaseResponse> purchaseItem(@Header("Authorization") String token, @Body PurchaseRequest request);
    
    @GET("store/purchases")
    Call<List<PurchaseHistoryResponse>> getPurchaseHistory(@Header("Authorization") String token);
    
    // Settings endpoints
    @GET("settings")
    Call<SettingsResponse> getSettings(@Header("Authorization") String token);
    
    @PUT("settings")
    Call<SettingsResponse> updateSettings(@Header("Authorization") String token, @Body SettingsRequest request);
    
    // Sync endpoints
    @POST("sync/upload")
    Call<SyncResponse> uploadData(@Header("Authorization") String token, @Body SyncDataRequest request);
    
    @GET("sync/download")
    Call<SyncDataResponse> downloadData(@Header("Authorization") String token);
    
    // Feedback endpoints
    @POST("feedback")
    Call<Void> submitFeedback(@Header("Authorization") String token, @Body FeedbackRequest request);
    
    @POST("support/ticket")
    Call<SupportTicketResponse> createSupportTicket(@Header("Authorization") String token, @Body SupportTicketRequest request);
    
    // App version endpoints
    @GET("app/version")
    Call<AppVersionResponse> getAppVersion();
    
    @GET("app/features")
    Call<List<FeatureResponse>> getFeatures(@Header("Authorization") String token);
    
    // Notification endpoints
    @POST("notifications/register")
    Call<Void> registerNotificationToken(@Header("Authorization") String token, @Body NotificationTokenRequest request);
    
    @GET("notifications")
    Call<List<NotificationResponse>> getNotifications(@Header("Authorization") String token);
    
    @PUT("notifications/{id}/read")
    Call<Void> markNotificationAsRead(@Header("Authorization") String token, @Path("id") String notificationId);
}
