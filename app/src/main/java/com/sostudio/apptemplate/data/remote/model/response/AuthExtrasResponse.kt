package com.sostudio.apptemplate.data.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AvatarUploadResponse(
    @SerialName("avatar_url") val avatarUrl: String
)

@Serializable
data class NotificationSettingsResponse(
    val id: Int,
    @SerialName("user_id") val userId: Int,
    @SerialName("general_enabled") val generalEnabled: Boolean,
    @SerialName("connections_enabled") val connectionsEnabled: Boolean,
    @SerialName("messages_enabled") val messagesEnabled: Boolean,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null
)
