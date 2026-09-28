package com.sostudio.apptemplate.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import cafe.adriel.voyager.navigator.Navigator

/**
 * Hides the bottom nav bar whenever the active tab has pushed a screen on top of its root.
 * [com.sostudio.apptemplate.nav.screen.MainTabScreen] provides this as the single source of
 * truth for its Scaffold's bottomBar visibility; each tab's own nested Navigator updates it via
 * [trackBottomBarVisibility] since the bottom bar lives outside that Navigator's composition and
 * can't otherwise observe its stack depth.
 */
val LocalBottomBarVisible: ProvidableCompositionLocal<MutableState<Boolean>> =
    staticCompositionLocalOf { error("LocalBottomBarVisible not provided — must be set by MainTabScreen") }

/**
 * Publishes whether this Navigator is at its root (a single item) to [LocalBottomBarVisible].
 * Call once from each tab's own nested `Navigator { ... }` content — re-evaluated on every
 * recomposition, so it stays correct across pushes/pops and across switching back to a tab that
 * was left mid-stack.
 */
@Composable
fun Navigator.trackBottomBarVisibility() {
    val bottomBarVisible = LocalBottomBarVisible.current
    // `size` must be read here, in the composable body, not inside the SideEffect lambda below —
    // it's a derivedStateOf (see Voyager's SnapshotStateStack), and a State read only subscribes
    // this composable for recomposition when it happens during composition.
    val isAtRoot = size <= 1
    SideEffect { bottomBarVisible.value = isAtRoot }
}
