package com.example.nottinder

import androidx.collection.intListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MatchMakingState(
    val currentCandidate: User = User("No Users Found"),
    val imageID: Int = 0
)

class MatchMakingViewModel : ViewModel() {
    private var candidates = mutableListOf<User>(
        User("Jack", intListOf(R.drawable.link1, R.drawable.link2, R.drawable.link3)),
        User("Alan"),
        User("Jessica"),
        User("zoe"),
        User("Sammy"),
    )

    private var currentImageIndex = 0

    private var _state: MutableStateFlow<MatchMakingState> = MutableStateFlow(MatchMakingState())
    var state: StateFlow<MatchMakingState> = _state.asStateFlow()


    init {
        _state.update { currentState ->
            val nextCandidate = candidates.first()
            currentState.copy(
                currentCandidate = nextCandidate,
                imageID = if(nextCandidate.pictures.isEmpty()) 0 else nextCandidate.pictures[currentImageIndex]
            )
        }
    }

    fun nextCandidate() {
        candidates = candidates.drop(1).toMutableList()

        if(candidates.isEmpty()) {
            return
        }

        currentImageIndex = 0

        _state.update { currentState ->
            val nextCandidate = candidates.first()
            currentState.copy(
                currentCandidate = nextCandidate,
                imageID = if(nextCandidate.pictures.isEmpty()) 0 else nextCandidate.pictures[currentImageIndex]
            )
        }
    }

    fun changeImage(leftTap: Boolean) {
        val direction = if(leftTap) -1 else 1
        if(leftTap) {
            if(currentImageIndex + direction < 0) {
                return
            }
            currentImageIndex--
        } else {
            if(currentImageIndex + direction >= state.value.currentCandidate.pictures.size) {
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