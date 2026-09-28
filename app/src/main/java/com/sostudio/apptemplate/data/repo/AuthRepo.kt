package com.sostudio.apptemplate.data.repo

import com.sostudio.apptemplate.data.db.dao.UserDao
import com.sostudio.apptemplate.data.db.entity.toEntity
import com.sostudio.apptemplate.data.db.entity.toResponse
import com.sostudio.apptemplate.data.remote.ServiceManager
import com.sostudio.apptemplate.data.remote.api.AuthApi
import com.sostudio.apptemplate.data.remote.model.body.DeleteAccountBody
import com.sostudio.apptemplate.data.remote.model.body.DeveloperFeedbackBody
import com.sostudio.apptemplate.data.remote.model.body.DeviceTokenBody
import com.sostudio.apptemplate.data.remote.model.body.ForgotPasswordBody
import com.sostudio.apptemplate.data.remote.model.body.LoginBody
import com.sostudio.apptemplate.data.remote.model.body.NotificationSettingsBody
import com.sostudio.apptemplate.data.remote.model.body.RegisterBody
import com.sostudio.apptemplate.data.remote.model.body.UpdateUserBody
import com.sostudio.apptemplate.data.remote.model.response.APIResult
import com.sostudio.apptemplate.data.remote.model.response.AuthData
import com.sostudio.apptemplate.data.remote.model.response.AvatarUploadResponse
import com.sostudio.apptemplate.data.remote.model.response.NotificationSettingsResponse
import com.sostudio.apptemplate.data.remote.model.response.UserResponse
import com.sostudio.apptemplate.data.repo.base.BaseRepo
import com.sostudio.apptemplate.util.toMultipartPart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * `getUser()`/`updateUser()` write-through cache to [userDao], matching iOS's
 * `UserStorageService` write triggers. `uploadAvatar()` alone isn't wired to the cache —
 * `AvatarUploadResponse` doesn't carry the full user shape needed to build a `UserEntity` —
 * a save flow that only changed the avatar should re-fetch via `getUser()` afterward.
 */
class AuthRepo(
    override val serviceManager: ServiceManager,
    private val userDao: UserDao
) : BaseRepo() {
    private val authService by lazyService<AuthApi>()

    suspend fun register(name: String, email: String, password: String, ageConfirmed: Boolean, inviteToken: String? = null): APIResult<AuthData> =
        doAPICall("register") {
            authService.api.register(RegisterBody(name, email, password, ageConfirmed, inviteToken))
        }

    suspend fun login(login: String, password: String): APIResult<AuthData> =
        doAPICall("login") {
            authService.api.login(LoginBody(login, password))
        }

    suspend fun getUser(): APIResult<UserResponse> =
        doAPICall("getUser") { authService.api.getUser() }
            .also { if (it is APIResult.Success) userDao.upsert(it.data.toEntity()) }

    /** Cache-first paint, matching iOS's `UserStorageService` synchronous-read-at-init pattern. */
    suspend fun getCachedUser(): UserResponse? = userDao.getCurrentUser()?.toResponse()

    /** Live avatar URL for a top bar's account icon — updates as soon as [getUser]/[updateUser] refresh the cache. */
    val cachedAvatarUrl: Flow<String?> = userDao.observeCurrentUser().map { it?.avatarUrl }

    suspend fun updateUser(
        name: String? = null,
        email: String? = null,
        password: String? = null,
        username: String? = null
    ): APIResult<UserResponse> =
        doAPICall("updateUser") {
            authService.api.updateUser(UpdateUserBody(name, email, password, username))
        }.also { if (it is APIResult.Success) userDao.upsert(it.data.toEntity()) }

    suspend fun deleteAccount(password: String): APIResult<Unit> =
        doAPICallWithEmptyResponse("deleteAccount") { authService.api.deleteAccount(DeleteAccountBody(password)) }

    suspend fun forgotPassword(email: String): APIResult<Unit> =
        doAPICallWithEmptyResponse("forgotPassword") { authService.api.forgotPassword(ForgotPasswordBody(email)) }

    suspend fun resendEmailVerification(): APIResult<Unit> =
        doAPICallWithEmptyResponse("resendEmailVerification") { authService.api.resendEmailVerification() }

    suspend fun uploadAvatar(imageBytes: ByteArray): APIResult<AvatarUploadResponse> =
        doAPICall("uploadAvatar") {
            authService.api.uploadAvatar(imageBytes.toMultipartPart("avatar", "avatar.jpg", "image/jpeg"))
        }

    suspend fun deviceToken(token: String): APIResult<Unit> =
        doAPICallWithEmptyResponse("deviceToken") { authService.api.deviceToken(DeviceTokenBody(token)) }

    suspend fun developerFeedback(message: String): APIResult<Unit> =
        doAPICallWithEmptyResponse("developerFeedback") { authService.api.developerFeedback(DeveloperFeedbackBody(message)) }

    suspend fun getNotificationSettings(): APIResult<NotificationSettingsResponse> =
        doAPICall("getNotificationSettings") { authService.api.getNotificationSettings() }

    suspend fun updateNotificationSettings(
        connectionsEnabled: Boolean,
        messagesEnabled: Boolean,
        generalEnabled: Boolean
    ): APIResult<NotificationSettingsResponse> =
        doAPICall("updateNotificationSettings") {
            authService.api.updateNotificationSettings(
                NotificationSettingsBody(connectionsEnabled, messagesEnabled, generalEnabled)
            )
        }
}
