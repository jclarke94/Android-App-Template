package com.sostudio.apptemplate.data.remote

import kotlin.reflect.KClass

/**
 * Koin single. Caches one [BaseService] wrapper per API interface type, all sharing the single
 * [RetrofitClient] instance injected here.
 */
class ServiceManager(@PublishedApi internal val retrofitClient: RetrofitClient) {
    @PublishedApi
    internal val services = mutableMapOf<KClass<*>, BaseService<*>>()

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : BaseService.API> getService(): BaseService<T> {
        return (services[T::class] ?: synchronized(services) {
            services[T::class] ?: BaseService(T::class, retrofitClient).also {
                services[T::class] = it
            }
        }) as BaseService<T>
    }
}
