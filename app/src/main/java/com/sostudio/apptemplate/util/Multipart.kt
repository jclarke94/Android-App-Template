package com.sostudio.apptemplate.util

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

fun ByteArray.toMultipartPart(partName: String, filename: String, mimeType: String): MultipartBody.Part =
    MultipartBody.Part.createFormData(partName, filename, toRequestBody(mimeType.toMediaType()))
