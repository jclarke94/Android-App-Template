package com.sostudio.apptemplate.di

import com.sostudio.apptemplate.nav.TabSwitcher
import org.koin.dsl.module

val navModule = module {
    single { TabSwitcher() }
}
