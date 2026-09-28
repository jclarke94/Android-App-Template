package com.sostudio.apptemplate.data.remote

import kotlin.reflect.KClass

class BaseService<C : BaseService.API>(
    private val kClass: KClass<C>,
    private val retrofitClient: RetrofitClient
) {
    /** Empty interface implemented by all API interfaces — acts as a base type constraint. */
    interface API

    @Volatile
    private var client: C? = null

    val api: C
        get() = client ?: synchronized(this) {
            client ?: retrofitClient.createService(kClass.java).also { client = it }
        }
}
