package com.sostudio.apptemplate.data.remote.api

import com.sostudio.apptemplate.data.remote.BaseService
import com.sostudio.apptemplate.data.remote.model.body.DeleteAccountBody
import com.sostudio.apptemplate.data.remote.model.body.DeveloperFeedbackBody
import com.sostudio.apptemplate.data.remote.model.body.DeviceTokenBody
import com.sostudio.apptemplate.data.remote.model.body.ForgotPasswordBody
import com.sostudio.apptemplate.data.remote.model.body.LoginBody
import com.sostudio.apptemplate.data.remote.model.body.NotificationSettingsBody
import com.sostudio.apptemplate.data.remote.model.body.RegisterBody
import com.sostudio.apptemplate.data.remote.model.body.UpdateUserBody
import com.sostudio.apptemplate.data.remote.model.response.AuthData
import com.sostudio.apptemplate.data.remote.model.response.AvatarUploadResponse
import com.sostudio.apptemplate.data.remote.model.response.GenericResponse
import com.sostudio.apptemplate.data.remote.model.response.NotificationSettingsResponse
import com.sostudio.apptemplate.data.remote.model.response.UserResponse
import com.skydoves.sandwich.ApiResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part

/** Maps to an `auth` route group plus `notification-settings` on your backend. */
interface AuthApi : BaseService.API {
    @POST("api/auth/register")
    suspend fun register(@Body body: RegisterBody): ApiResponse<GenericResponse<AuthData>>

    @POST("api/auth/login")
    suspend fun login(@Body body: LoginBody): ApiResponse<GenericResponse<AuthData>>

    @GET("api/auth/user")
    suspend fun getUser(): ApiResponse<GenericResponse<UserResponse>>

    @POST("api/auth/updateProfile")
    suspend fun updateUser(@Body body: UpdateUserBody): ApiResponse<GenericResponse<UserResponse>>

    // Retrofit's @DELETE doesn't support @Body — deleting an account requires the current
    // password, so this needs @HTTP with hasBody explicitly set instead.
    @HTTP(method = "DELETE", path = "api/auth/account", hasBody = true)
    suspend fun deleteAccount(@Body body: DeleteAccountBody): ApiResponse<GenericResponse<String?>>

    @POST("api/auth/forgotPassword")
    suspend fun forgotPassword(@Body body: ForgotPasswordBody): ApiResponse<GenericResponse<String?>>

    @POST("api/auth/resendVerification")
    suspend fun resendEmailVerification(): ApiResponse<GenericResponse<String?>>

    @Multipart
    @POST("api/auth/avatar")
    suspend fun uploadAvatar(@Part avatar: MultipartBody.Part): ApiResponse<GenericResponse<AvatarUploadResponse>>

    @POST("api/auth/deviceToken")
    suspend fun deviceToken(@Body body: DeviceTokenBody): ApiResponse<GenericResponse<String?>>

    @POST("api/auth/feedback")
    suspend fun developerFeedback(@Body body: DeveloperFeedbackBody): ApiResponse<GenericResponse<String?>>

    @GET("api/notification-settings")
    suspend fun getNotificationSettings(): ApiResponse<GenericResponse<NotificationSettingsResponse>>

    @PATCH("api/notification-settings")
    suspend fun updateNotificationSettings(@Body body: NotificationSettingsBody): ApiResponse<GenericResponse<NotificationSettingsResponse>>
}
