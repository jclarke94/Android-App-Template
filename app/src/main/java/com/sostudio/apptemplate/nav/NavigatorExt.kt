package com.sostudio.apptemplate.nav

import cafe.adriel.voyager.navigator.Navigator

/**
 * The topmost ancestor Navigator. A single `.parent` hop from inside a tab's nested Navigator
 * only reaches [cafe.adriel.voyager.navigator.tab.TabNavigator]'s own internal Navigator (which
 * holds Tab items, not the app's root Screen) — pushing a plain Screen there crashes
 * `TabNavigator.current`'s cast to Tab. Walking all the way up avoids hardcoding a fixed number
 * of hops, so it stays correct regardless of how deep the calling screen is nested. Used to push
 * SettingsScreen from inside a tab, and to replace the whole app with WelcomeScreen on logout.
 */
fun Navigator.rootNavigator(): Navigator = generateSequence(this) { it.parent }.last()
