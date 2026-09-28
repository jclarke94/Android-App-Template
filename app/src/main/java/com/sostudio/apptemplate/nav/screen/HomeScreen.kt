package com.sostudio.apptemplate.nav.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.sostudio.apptemplate.R
import com.sostudio.apptemplate.nav.BaseScreen
import com.sostudio.apptemplate.nav.trackBottomBarVisibility
import com.sostudio.apptemplate.ui.ViewModels.HomeViewModel
import com.sostudio.apptemplate.ui.components.ScreenOverlay
import com.sostudio.apptemplate.ui.components.text.TitleText
import com.sostudio.apptemplate.ui.content.HomeContent

/** Hosts its own nested [Navigator] (mirroring iOS's per-tab `NavigationPath`) so future screens
 *  can be pushed on top of Home without touching the outer tab shell. */
object HomeTab : Tab {
    override val options: TabOptions
        @Composable get() = TabOptions(index = 0u, title = stringResource(R.string.home_title))

    @Composable
    override fun Content() {
        Navigator(HomeScreen()) { navigator ->
            navigator.trackBottomBarVisibility()
            CurrentScreen()
        }
    }
}

/** Smallest example of View -> ViewModel -> Content, alongside Settings — replace with your own
 *  screens. Pushes [DetailScreen] to demonstrate a pushed destination. */
class HomeScreen : BaseScreen() {

    @Composable
    override fun Content() {
        val viewModel: HomeViewModel = koinScreenModel()
        val navigator = LocalNavigator.currentOrThrow
        val user by viewModel.user.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) { viewModel.loadUser() }

        ScreenOverlay(viewModel) {
            HomeContent(user = user, onOpenDetail = { navigator.push(DetailScreen(id = 1)) })
        }
    }
}

/** Placeholder screen demonstrating a pushed destination. Replace with your own screens. */
class DetailScreen(private val id: Int) : BaseScreen() {
    @Composable
    override fun Content() {
        TitleText("Detail #$id")
    }
}
