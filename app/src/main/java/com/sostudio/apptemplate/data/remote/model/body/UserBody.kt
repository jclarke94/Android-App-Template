package com.sostudio.apptemplate.data.remote.model.body

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserBody(
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
    val username: String? = null
)

@Serializable
data class ForgotPasswordBody(val email: String)

@Serializable
data class DeleteAccountBody(val password: String)

@Serializable
data class DeviceTokenBody(@SerialName("device_token") val deviceToken: String)

@Serializable
data class DeveloperFeedbackBody(val message: String)

@Serializable
data class NotificationSettingsBody(
    @SerialName("connections_enabled") val connectionsEnabled: Boolean,
    @SerialName("messages_enabled") val messagesEnabled: Boolean,
    @SerialName("general_enabled") val generalEnabled: Boolean
)
