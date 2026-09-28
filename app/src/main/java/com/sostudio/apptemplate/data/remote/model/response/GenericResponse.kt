package com.sostudio.apptemplate.data.remote.model.response

import kotlinx.serialization.Serializable

/** The API wraps every response as {success, data, error}. */
@Serializable
data class GenericResponse<T>(
    val success: Boolean? = false,
    val data: T? = null,
    val error: String? = null
)

/**
 * Covers both a manual controller error shape ({success, error}) and Laravel's automatic
 * validation-exception shape ({message, errors}), since a failed `$request->validate()` call
 * never reaches the controller's own json() response.
 */
@Serializable
data class ErrorBody(
    val success: Boolean? = null,
    val error: String? = null,
    val message: String? = null,
    val errors: Map<String, List<String>>? = null
)
