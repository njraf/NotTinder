package com.example.nottinder

import android.net.Uri
import androidx.collection.intListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileState(
    val self: User = User(0, "Nick", "", emptyList<Uri>())
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userDataSource: UserDataSource
) : ViewModel() {
    private var _state: MutableStateFlow<ProfileState> = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            userDataSource.users.collect { users ->
                _state.update { currentState ->
                    val self: User = users.find { it.id == currentState.self.id } ?: return@collect
                    currentState.copy(
                        self = self
                    )
                }
            }
        }
    }

    fun updateBio(newBio: String) {
        userDataSource.updateBio(state.value.self.id, newBio)
    }

    fun addPhoto(uri: Uri) {
        userDataSource.updatePhotos(state.value.self.id, state.value.self.pictureUris + uri)
    }

}