package com.sostudio.apptemplate.nav

import cafe.adriel.voyager.navigator.tab.Tab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Requests a tab switch from outside [com.sostudio.apptemplate.nav.screen.MainTabScreen]'s own
 * composition. Needed because anything pushed onto the outer root Navigator as a sibling of
 * MainTabScreen — Settings, most notably — has no access to `LocalTabNavigator.current`, which
 * only exists inside `TabNavigator`'s own content scope. MainTabScreen observes [requestedTab]
 * and applies + clears it once it's back in composition (e.g. after Settings pops itself off the
 * root Navigator).
 */
class TabSwitcher {
    private val _requestedTab = MutableStateFlow<Tab?>(null)
    val requestedTab: StateFlow<Tab?> = _requestedTab.asStateFlow()

    fun request(tab: Tab) {
        _requestedTab.value = tab
    }

    fun consume() {
        _requestedTab.value = null
    }
}
