package com.example.nottinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.nottinder.ui.theme.NotTinderTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {
    @Serializable
    data object Login : Route

    @Serializable
    data object Candidates : Route

    @Serializable
    data object Profile : Route

    @Serializable
    data object Settings : Route
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotTinderTheme {
                val matchMakingViewModel: MatchMakingViewModel = hiltViewModel()
                val profileViewModel: ProfileViewModel = hiltViewModel()

                val loginBackstack = rememberNavBackStack(Route.Login)
                val candidatesBackstack = rememberNavBackStack(Route.Candidates)
                val profileBackstack = rememberNavBackStack(Route.Profile)
                val settingsBackstack = rememberNavBackStack(Route.Settings)

                val allBackstacks = listOf(
                    loginBackstack,
                    candidatesBackstack,
                    profileBackstack,
                    settingsBackstack
                )

                var backstackKey: Route by remember { mutableStateOf(Route.Login) }
                val currentBackStack = when (backstackKey) {
                    Route.Login -> loginBackstack
                    Route.Candidates -> candidatesBackstack
                    Route.Profile -> profileBackstack
                    Route.Settings -> settingsBackstack
                    else -> loginBackstack
                }

                val changeBackstack: (Route) -> Unit = { route ->
                    if (route != currentBackStack.first()) {
                        backstackKey = route
                    }
                }

                NavDisplay(
                    backStack = currentBackStack,
                    onBack = { currentBackStack.removeLastOrNull() }) { route ->
                    when (route) {
                        is Route.Login -> NavEntry(route) {
                            LoginScreen(
                                onLoginVerified = { backstackKey = Route.Candidates },
                                onCreateProfileClicked = { currentBackStack.add(Route.Profile) })
                        }

                        is Route.Candidates -> NavEntry(route) {
                            MatchMakingScreen(matchMakingViewModel, backstackKey, changeBackstack)
                        }

                        is Route.Profile -> NavEntry(route) {
                            ProfileScreen(
                                profileViewModel,
                                backstackKey,
                                changeBackstack,
                                onProfileSaved = {
                                    if (currentBackStack.first() == Route.Login) {
                                        currentBackStack.removeRange(1, currentBackStack.size)
                                        backstackKey = Route.Candidates
                                    }
                                })
                        }

                        is Route.Settings -> NavEntry(route) {
                            SettingsScreen(
                                backstackKey,
                                changeBackstack,
                                onLogout = {
                                    allBackstacks.forEach { it.removeRange(1, it.size) }
                                    backstackKey = Route.Login
                                })
                        }

                        else -> NavEntry(route) { Text("Unknown Page") }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomBar(currentBackstackTopPage: Route, onClick: (Route) -> Unit) {
    BottomAppBar(actions = {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            IconButton(onClick = { onClick(Route.Candidates) }) {
                Icon(
                    if (currentBackstackTopPage is Route.Candidates) Icons.Filled.AccountBox else Icons.Outlined.AccountBox,
                    contentDescription = ""
                )
            }
            IconButton(onClick = { onClick(Route.Profile) }) {
                Icon(
                    if (currentBackstackTopPage is Route.Profile) Icons.Filled.AccountCircle else Icons.Outlined.AccountCircle,
                    contentDescription = ""
                )
            }
            IconButton(onClick = { onClick(Route.Settings) }) {
                Icon(
                    if (currentBackstackTopPage is Route.Settings) Icons.Filled.Settings else Icons.Outlined.Settings,
                    contentDescription = ""
                )
            }
        }
    })
}

@Preview
@Composable
fun PreviewBottomBar() {
    var topPage by remember { mutableStateOf<Route>(Route.Candidates) }
    BottomBar(topPage, { route -> topPage = route })
}