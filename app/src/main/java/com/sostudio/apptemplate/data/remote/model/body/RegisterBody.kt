package com.sostudio.apptemplate.data.remote.model.body

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * age_confirmed is a self-attested 13+ checkbox, not a stored birthdate — the server should
 * record an age_confirmed_at timestamp when true. Username is generated server-side, not sent
 * by the client.
 */
@Serializable
data class RegisterBody(
    val name: String,
    val email: String,
    val password: String,
    @SerialName("age_confirmed") val ageConfirmed: Boolean,
    // Optional — consumed server-side to token-match a pending invite regardless of
    // email-verification state, since possession of the token is itself the proof of intent.
    @SerialName("invite_token") val inviteToken: String? = null
)
