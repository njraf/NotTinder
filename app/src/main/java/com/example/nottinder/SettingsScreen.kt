package com.example.nottinder

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingsScreen(topPage: Route, onBottomNavigate: (Route) -> Unit, onLogout: () -> Unit) {
    val viewModel: SettingsViewModel = hiltViewModel()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomBar(topPage, onBottomNavigate)
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text("Settings screen")
            Button(onClick = {
                viewModel.logOut()
                onLogout()
            }) {
                Text(text = "Log Out")
            }
        }
    }
}