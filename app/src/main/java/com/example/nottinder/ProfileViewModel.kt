package com.example.nottinder

import android.net.Uri
import androidx.collection.intListOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class ProfileState(
    val self: User = User("Nick", "", intListOf(R.drawable.pika1), emptyList<Uri>())
)

@HiltViewModel
class ProfileViewModel @Inject constructor(val photoDataSource: PhotoDataSource) : ViewModel() {
    private var _state: MutableStateFlow<ProfileState> = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    fun updateBio(newBio: String) {
        _state.update { currentState ->
            currentState.copy(
                self = currentState.self.copy(biography = newBio)
            )
        }
    }

    fun addPhoto(uri: Uri) {
        _state.update { currentState ->
            currentState.copy(
                self = currentState.self.copy(pictureUris = currentState.self.pictureUris + uri)
            )
        }
    }

}