package com.example.nottinder

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MatchMakingState(
    val currentCandidate: User = User("No Users Found")
)

class MatchMakingViewModel : ViewModel() {
    private var candidates = mutableListOf<User>(
        User("Jack"),
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
                currentCandidate = candidates.first()
            )
        }
    }
}