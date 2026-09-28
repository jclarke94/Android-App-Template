package com.sostudio.apptemplate.nav.screen

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.sostudio.apptemplate.nav.BaseScreen
import com.sostudio.apptemplate.nav.LocalBottomBarVisible
import com.sostudio.apptemplate.ui.components.text.BoldCaptionText
import com.sostudio.apptemplate.ui.theme.AppTemplateTheme

/**
 * Root of the logged-in app: an outer [BaseScreen] hosting an inner [TabNavigator] for the
 * example tabs (mirrors iOS's `AppTab`: `.home`/`.activity`). Being a Screen itself (not just a
 * raw Composable) means Settings can be pushed onto the *outer* Navigator on top of this whole
 * tab shell, mirroring iOS's fullScreenCover-over-any-tab pattern (see
 * [com.sostudio.apptemplate.nav.rootNavigator]).
 *
 * The bottom bar hides whenever the active tab has pushed a screen on top of its root — mirrors
 * iOS's `NavigationContainerView.isAtRoot` check.
 */
class MainTabScreen : BaseScreen() {

    @Composable
    override fun Content() {
        val bottomBarVisible = remember { mutableStateOf(true) }

        CompositionLocalProvider(LocalBottomBarVisible provides bottomBarVisible) {
            TabNavigator(HomeTab) {
                val tabNavigator = LocalTabNavigator.current

                Scaffold(
                    bottomBar = {
                        AnimatedVisibility(
                            visible = bottomBarVisible.value,
                            enter = slideInVertically(initialOffsetY = { it }),
                            exit = slideOutVertically(targetOffsetY = { it })
                        ) {
                            MainBottomBar(selectedTab = tabNavigator.current, onTabSelected = { tabNavigator.current = it })
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        tabNavigator.current.Content()
                    }
                }
            }
        }
    }
}

// Selected = filled icon + primary color, unselected = outlined icon + secondary color — mirrors
// iOS's BottomNavBarView exactly. No pill indicator on iOS either, so it's turned off here via a
// transparent indicatorColor rather than Material3's default selected-state background.
@Composable
private fun MainBottomBar(selectedTab: Tab, onTabSelected: (Tab) -> Unit) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.background) {
        listOf(HomeTab, ActivityTab).forEach { tab ->
            val selected = selectedTab == tab
            val (filledIcon, outlinedIcon) = tabIcons(tab)
            val tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary

            NavigationBarItem(
                selected = selected,
                onClick = { onTabSelected(tab) },
                icon = { Icon(if (selected) filledIcon else outlinedIcon, contentDescription = tab.options.title, tint = tint) },
                label = { BoldCaptionText(tab.options.title, color = tint) },
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
            )
        }
    }
}

private fun tabIcons(tab: Tab): Pair<ImageVector, ImageVector> = when (tab) {
    HomeTab -> Icons.Filled.Home to Icons.Outlined.Home
    ActivityTab -> Icons.Filled.Notifications to Icons.Outlined.Notifications
    else -> Icons.Filled.Home to Icons.Outlined.Home
}

@Preview(name = "Light", showBackground = true)
@Composable
private fun MainBottomBarLightPreview() = MainBottomBarPreview()

@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun MainBottomBarDarkPreview() = MainBottomBarPreview()

@Composable
private fun MainBottomBarPreview() {
    AppTemplateTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            MainBottomBar(selectedTab = HomeTab, onTabSelected = {})
        }
    }
}
