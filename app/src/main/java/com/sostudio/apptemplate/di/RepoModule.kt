package com.sostudio.apptemplate.di

import com.sostudio.apptemplate.data.repo.AuthRepo
import org.koin.dsl.module

/** Add your own feature repos here as you build them out. */
val repoModule = module {
    single { AuthRepo(get(), get()) }
}
