package com.example.nottinder

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MatchMakingState(
    val currentCandidate: User = User(0, "", "", emptyList()),
    val photoUri: Uri = "".toUri()
)

@HiltViewModel
class MatchMakingViewModel @Inject constructor(
    private val userDataSource: UserDataSource
) : ViewModel() {

    private var candidates = mutableListOf<User>()

    private var currentImageIndex = 0

    private var _state: MutableStateFlow<MatchMakingState> = MutableStateFlow(MatchMakingState())
    var state: StateFlow<MatchMakingState> = _state.asStateFlow()


    init {
        viewModelScope.launch {
            userDataSource.getUsers().collect { users ->
                // reset the candidate list
                currentImageIndex = 0
                candidates = users.toMutableList()
                _state.update { currentState ->
                    currentState.copy(
                        currentCandidate = users.firstOrNull() ?: userDataSource.nullCandidate,
                        photoUri = users.firstOrNull()?.pictureUris[0] ?: "".toUri()
                    )
                }
            }
        }
    }

    fun nextCandidate() {
        candidates = candidates.drop(1).toMutableList()

        currentImageIndex = 0

        _state.update { currentState ->
            val nextCandidate = candidates.firstOrNull() ?: userDataSource.nullCandidate
            currentState.copy(
                currentCandidate = nextCandidate,
                photoUri = nextCandidate.pictureUris.firstOrNull() ?: "".toUri()
            )
        }
    }

    fun changeImage(leftTap: Boolean) {
        val direction = if (leftTap) -1 else 1
        if (leftTap) {
            if (currentImageIndex + direction < 0) {
                return
            }
            currentImageIndex--
        } else {
            if (currentImageIndex + direction >= state.value.currentCandidate.pictureUris.size) {
                return
            }
            currentImageIndex++
        }

        _state.update { currentState ->
            currentState.copy(
                photoUri = state.value.currentCandidate.pictureUris[currentImageIndex]
            )
        }
    }
/*
    fun updateSelf(myself: User) {
        candidates.remove(self)
        self = myself
        candidates.add(0, myself)
        currentImageIndex = 0

        _state.update { currentState ->
            val nextCandidate = if (candidates.isNotEmpty()) candidates.first() else userDataSource.nullCandidate
            currentState.copy(
                currentCandidate = nextCandidate,
                photoUri = if (nextCandidate.pictureUris.isEmpty()) "".toUri() else nextCandidate.pictureUris[currentImageIndex]
            )
        }
    }*/
}