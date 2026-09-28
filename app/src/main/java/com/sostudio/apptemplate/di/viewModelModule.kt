package com.sostudio.apptemplate.di

import com.sostudio.apptemplate.ui.ViewModels.ActivityViewModel
import com.sostudio.apptemplate.ui.ViewModels.Auth.BannedViewModel
import com.sostudio.apptemplate.ui.ViewModels.Auth.LoginViewModel
import com.sostudio.apptemplate.ui.ViewModels.Auth.PasswordResetViewModel
import com.sostudio.apptemplate.ui.ViewModels.Auth.RegisterProfileImageViewModel
import com.sostudio.apptemplate.ui.ViewModels.Auth.RegisterViewModel
import com.sostudio.apptemplate.ui.ViewModels.Auth.SplashViewModel
import com.sostudio.apptemplate.ui.ViewModels.Auth.WelcomeViewModel
import com.sostudio.apptemplate.ui.ViewModels.HomeViewModel
import com.sostudio.apptemplate.ui.ViewModels.Settings.SettingsViewModel
import org.koin.dsl.module

/** Add your own feature ViewModels here as you build them out. */
val viewModelModule = module {
    factory { SplashViewModel(get()) }
    factory { WelcomeViewModel(get()) }
    factory { LoginViewModel(get(), get()) }
    factory { RegisterViewModel(get(), get()) }
    factory { RegisterProfileImageViewModel(get(), get()) }
    factory { PasswordResetViewModel(get()) }
    factory { BannedViewModel(get(), get(), get()) }
    factory { HomeViewModel(get()) }
    factory { ActivityViewModel() }
    factory { SettingsViewModel(get(), get()) }
}
