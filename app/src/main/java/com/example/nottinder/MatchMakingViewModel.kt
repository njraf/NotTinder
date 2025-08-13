package com.example.nottinder

import android.net.Uri
import androidx.collection.emptyIntList
import androidx.collection.intListOf
import androidx.compose.ui.text.input.KeyboardType.Companion.Uri
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
    //private val photoDataSource: PhotoDataSource,
    private val userDataSource: UserDataSource
) : ViewModel() {
    private val nullCandidate = User(-1, "", "", emptyList())
    private var self: User? = null

    private var candidates = mutableListOf<User>()
        /*mutableListOf<User>(
        User(
            1,
            "Link",
            "The hero of Hyrule and a tenacious swordsman.",
            intListOf(R.drawable.link1, R.drawable.link2, R.drawable.link3),
            emptyList()
        ),
        User(
            2,
            "Zelda",
            "The princess of Hyrule and a fierce combatant, loyal to her people.",
            intListOf(R.drawable.zelda1, R.drawable.zelda2, R.drawable.zelda3),
            emptyList()
        ),
        User(
            3,
            "Mario",
            "Wahoo! Wah! Wah! Yipeeeeeee!",
            intListOf(R.drawable.mario1, R.drawable.mario2, R.drawable.mario3, R.drawable.mario4),
            emptyList()
        ),
        User(
            4,
            "Pikachu",
            "Pika pi! Pikaaaachuuuuuuuuuu!!!!! Pika.",
            intListOf(R.drawable.pika1, R.drawable.pika2, R.drawable.pika3, R.drawable.pika4),
            emptyList()
        ),
    )*/

    private var currentImageIndex = 0

    private var _state: MutableStateFlow<MatchMakingState> = MutableStateFlow(MatchMakingState())
    var state: StateFlow<MatchMakingState> = _state.asStateFlow()


    init {

        // initialize UI
        /*_state.update { currentState ->
            val nextCandidate = candidates.firstOrNull() ?: nullCandidate
            currentState.copy(
                currentCandidate = nextCandidate,
                photoUri = if (nextCandidate.pictureUris.isEmpty()) "".toUri() else nextCandidate.pictureUris[currentImageIndex]
            )
        }*/

        viewModelScope.launch {
            userDataSource.users.collect { users ->
                val selfUser: User = users.find { it.id == (self?.id ?: 0) } ?: return@collect
                updateSelf(selfUser)
            }
        }
    }

    fun nextCandidate() {
        candidates = candidates.drop(1).toMutableList()

        currentImageIndex = 0

        _state.update { currentState ->
            val nextCandidate = if (candidates.isNotEmpty()) candidates.first() else nullCandidate
            currentState.copy(
                currentCandidate = nextCandidate,
                photoUri = if (nextCandidate.pictureUris.isEmpty()) "".toUri() else nextCandidate.pictureUris[currentImageIndex]
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

    fun updateSelf(myself: User) {
        candidates.remove(self)
        self = myself
        candidates.add(0, myself)
        currentImageIndex = 0

        _state.update { currentState ->
            val nextCandidate = if (candidates.isNotEmpty()) candidates.first() else nullCandidate
            currentState.copy(
                currentCandidate = nextCandidate,
                photoUri = if (nextCandidate.pictureUris.isEmpty()) "".toUri() else nextCandidate.pictureUris[currentImageIndex]
            )
        }
    }
}