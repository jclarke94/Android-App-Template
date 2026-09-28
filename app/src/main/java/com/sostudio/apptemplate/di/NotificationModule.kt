package com.sostudio.apptemplate.di

import com.sostudio.apptemplate.service.NotificationTokenManager
import org.koin.dsl.module

val notificationModule = module {
    single { NotificationTokenManager(get(), get()) }
}
