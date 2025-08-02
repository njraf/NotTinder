package com.example.nottinder

import androidx.collection.intListOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MatchMakingState(
    val currentCandidate: User = User("No Users Found"),
    val imageIndex: Int = 0
)

class MatchMakingViewModel : ViewModel() {
    private var candidates = mutableListOf<User>(
        User("Jack", intListOf(R.drawable.link1, R.drawable.link2, R.drawable.link3)),
        User("Alan"),
        User("Jessica"),
        User("zoe"),
        User("Sammy"),
    )

    private var _state: MutableStateFlow<MatchMakingState> = MutableStateFlow(MatchMakingState())
    var state: StateFlow<MatchMakingState> = _state.asStateFlow()


    init {
        _state.update { currentState ->
            currentState.copy(
                currentCandidate = candidates.first()
            )
        }
    }

    fun nextCandidate() {
        candidates = candidates.drop(1).toMutableList()

        if(candidates.isEmpty()) {
            return
        }

        _state.update { currentState ->
            currentState.copy(
                currentCandidate = candidates.first(),
                imageIndex = 0
            )
        }
    }

    fun nextImage() {
        if(state.value.imageIndex + 1 >= state.value.currentCandidate.pictures.size) {
            return
        }

        _state.update { currentState ->
            currentState.copy(
                currentCandidate = candidates.first(),
                imageIndex = currentState.imageIndex + 1
            )
        }
    }
}