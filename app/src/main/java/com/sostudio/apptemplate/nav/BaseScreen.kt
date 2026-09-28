package com.sostudio.apptemplate.nav

import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey

abstract class BaseScreen : Screen {
    override val key = uniqueScreenKey
}
