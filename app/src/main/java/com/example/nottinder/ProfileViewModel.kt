package com.example.nottinder

import android.net.Uri
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
    val self: User = User(-1, "", "", emptyList<Uri>()),
    val temporaryPhotoUris: List<Uri> = emptyList<Uri>(),
    val creatingAccount: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userDataSource: UserDataSource
) : ViewModel() {
    private var _state: MutableStateFlow<ProfileState> = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            userDataSource.getSelf().collect { self ->
                _state.update { currentState ->
                    currentState.copy(
                        self = self ?: userDataSource.nullCandidate,
                        temporaryPhotoUris = self?.pictureUris ?: emptyList()
                    )
                }
            }
        }
    }

    fun addTemporaryPhoto(uri: Uri) {
        _state.update { currentState ->
            currentState.copy(
                temporaryPhotoUris = currentState.temporaryPhotoUris + uri
            )
        }
    }

    fun removeTemporaryPhoto(uri: Uri) {
        _state.update { currentState ->
            currentState.copy(
                temporaryPhotoUris = currentState.temporaryPhotoUris - uri
            )
        }
    }

    fun updateUser(user: User) {
        userDataSource.updateSelf(user)
    }
}