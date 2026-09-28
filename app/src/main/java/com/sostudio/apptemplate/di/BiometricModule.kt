package com.sostudio.apptemplate.di

import com.sostudio.apptemplate.service.BiometricLockManager
import org.koin.dsl.module

val biometricModule = module {
    single { BiometricLockManager(get(), get()) }
}
