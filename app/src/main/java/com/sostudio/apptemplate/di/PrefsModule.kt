package com.sostudio.apptemplate.di

import com.sostudio.apptemplate.data.prefs.PrefsHelper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val prefsModule = module {
    single { PrefsHelper(androidContext()) }
}
