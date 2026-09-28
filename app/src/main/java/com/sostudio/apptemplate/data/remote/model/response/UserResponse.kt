package com.sostudio.apptemplate.data.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Mirrors the API's User model's fillable/appended fields (password/remember_token stay
 *  hidden server-side). */
@Serializable
data class UserResponse(
    val id: Int,
    val name: String,
    val email: String,
    val username: String? = null,
    val avatar: String? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("date_of_birth") val dateOfBirth: String? = null,
    @SerialName("email_verified_at") val emailVerifiedAt: String? = null,
    @SerialName("banned_at") val bannedAt: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null
) {
    companion object {
        /** For @Preview use only, never real data. */
        fun dummy(id: Int = 1): UserResponse = UserResponse(
            id = id,
            name = "Dummy User $id",
            email = "dummyuser$id@example.com",
            username = "dummyuser$id",
            avatarUrl = "https://randomuser.me/api/portraits/lego/$id.jpg",
            emailVerifiedAt = "2025-09-03T12:34:56Z",
            createdAt = "2025-09-03T12:34:56Z",
            updatedAt = "2025-09-03T12:34:56Z"
        )
    }
}

/** Shape returned by auth/register and auth/login: {user, token}. */
@Serializable
data class AuthData(
    val user: UserResponse,
    val token: String
)
