package com.sostudio.apptemplate.nav.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.sostudio.apptemplate.R
import com.sostudio.apptemplate.nav.BaseScreen
import com.sostudio.apptemplate.nav.trackBottomBarVisibility
import com.sostudio.apptemplate.ui.ViewModels.ActivityViewModel
import com.sostudio.apptemplate.ui.components.ScreenOverlay
import com.sostudio.apptemplate.ui.content.ActivityContent

/** Second example tab — hosts its own nested [Navigator] like [HomeTab], even though nothing
 *  pushes further screens onto it yet. */
object ActivityTab : Tab {
    override val options: TabOptions
        @Composable get() = TabOptions(index = 1u, title = stringResource(R.string.activity_title))

    @Composable
    override fun Content() {
        Navigator(ActivityScreen()) { navigator ->
            navigator.trackBottomBarVisibility()
            CurrentScreen()
        }
    }
}

class ActivityScreen : BaseScreen() {

    @Composable
    override fun Content() {
        val viewModel: ActivityViewModel = koinScreenModel()

        ScreenOverlay(viewModel) {
            ActivityContent()
        }
    }
}
