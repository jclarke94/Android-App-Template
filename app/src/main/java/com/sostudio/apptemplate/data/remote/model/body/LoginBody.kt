package com.sostudio.apptemplate.data.remote.model.body

import kotlinx.serialization.Serializable

/** `login` accepts either an email or a username. */
@Serializable
data class LoginBody(
    val login: String,
    val password: String
)
