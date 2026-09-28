package com.sostudio.apptemplate.di

import com.sostudio.apptemplate.data.remote.RetrofitClient
import com.sostudio.apptemplate.data.remote.ServiceManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * RetrofitClient/ServiceManager are Koin singles (constructor-injected) rather than plain
 * singleton objects, so repos get them via DI instead of a global object lookup — keeps them
 * mockable in tests.
 */
val networkModule = module {
    single { RetrofitClient(androidContext(), get(), get()) }
    single { ServiceManager(get()) }
}
