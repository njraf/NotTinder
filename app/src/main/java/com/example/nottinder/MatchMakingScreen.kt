package com.example.nottinder

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun MatchMakingScreen() {

    val viewModel: MatchMakingViewModel = viewModel()

    LazyColumn {
        items(viewModel.candidates) { candidate ->
            Text(candidate.name)
        }
    }

}