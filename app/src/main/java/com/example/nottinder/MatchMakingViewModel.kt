package com.example.nottinder

import androidx.collection.emptyIntList
import androidx.collection.intListOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class MatchMakingState(
    val currentCandidate: User = User("", "", emptyIntList()),
    val imageID: Int = 0
)

@HiltViewModel
class MatchMakingViewModel @Inject constructor() : ViewModel() {
    private val nullCandidate = User("", "", emptyIntList())
    private var candidates = mutableListOf<User>(
        User(
            "Link",
            "The hero of Hyrule and a tenacious swordsman.",
            intListOf(R.drawable.link1, R.drawable.link2, R.drawable.link3)
        ),
        User(
            "Zelda",
            "The princess of Hyrule and a fierce combatant, loyal to her people.",
            intListOf(R.drawable.zelda1, R.drawable.zelda2, R.drawable.zelda3)
        ),
        User(
            "Mario",
            "Wahoo! Wah! Wah! Yipeeeeeee!",
            intListOf(R.drawable.mario1, R.drawable.mario2, R.drawable.mario3, R.drawable.mario4)
        ),
        User(
            "Pikachu",
            "Pika pi! Pikaaaachuuuuuuuuuu!!!!! Pika.",
            intListOf(R.drawable.pika1, R.drawable.pika2, R.drawable.pika3, R.drawable.pika4)
        ),
    )

    private var currentImageIndex = 0

    private var _state: MutableStateFlow<MatchMakingState> = MutableStateFlow(MatchMakingState())
    var state: StateFlow<MatchMakingState> = _state.asStateFlow()


    init {
        _state.update { currentState ->
            val nextCandidate = candidates.first()
            currentState.copy(
                currentCandidate = nextCandidate,
                imageID = if (nextCandidate.pictures.isEmpty()) 0 else nextCandidate.pictures[currentImageIndex]
            )
        }
    }

    fun nextCandidate() {
        candidates = candidates.drop(1).toMutableList()

        currentImageIndex = 0

        _state.update { currentState ->
            val nextCandidate = if(candidates.isNotEmpty()) candidates.first() else nullCandidate
            currentState.copy(
                currentCandidate = nextCandidate,
                imageID = if (nextCandidate.pictures.isEmpty()) 0 else nextCandidate.pictures[currentImageIndex]
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
            if (currentImageIndex + direction >= state.value.currentCandidate.pictures.size) {
                return
            }
            currentImageIndex++
        }

        _state.update { currentState ->
            currentState.copy(
                imageID = state.value.currentCandidate.pictures[currentImageIndex]
            )
        }
    }
}