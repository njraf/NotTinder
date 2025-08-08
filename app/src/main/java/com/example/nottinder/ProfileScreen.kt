package com.example.nottinder

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ProfileScreen(viewModel: ProfileViewModel, topPage: Route, onBottomNavigate: (Route) -> Unit, onProfileUpdated: (self: User) -> Unit) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomBar(topPage, onBottomNavigate)
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            PhotoSelector(onProfileUpdated)

            var bio by rememberSaveable { mutableStateOf("") }
            TextField(value = bio, onValueChange = { bio = it })
            Button(onClick = {
                viewModel.updateBio(bio)
                //bio = ""
                onProfileUpdated(state.self)
            }) { Text("Submit") }
        }
    }
}

@Composable
fun PhotoSelector(onProfileUpdated: (self: User) -> Unit) {

}

