package com.example.nottinder

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.collection.intListOf
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ProfileState(
    val self: User = User("Nick", "", intListOf(R.drawable.pika1))
)

class ProfileViewModel : ViewModel() {
    private var _state: MutableStateFlow<ProfileState> = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    fun updateBio(newBio: String) {
        _state.update { currentState ->
            currentState.copy(
                self = currentState.self.copy(biography = newBio)
            )
        }
    }


}