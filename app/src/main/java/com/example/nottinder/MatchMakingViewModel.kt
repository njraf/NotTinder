package com.example.nottinder

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class MatchMakingViewModel : ViewModel() {
    val candidates = mutableStateListOf<User>(
        User("Jack"),
        User("Alan"),
        User("Jessica"),
        User("zoe"),
        User("Sammy"),
    )

    fun nextCandidate() {
        candidates.drop(0)
    }
}