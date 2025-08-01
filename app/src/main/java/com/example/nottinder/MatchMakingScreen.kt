package com.example.nottinder

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Preview(showBackground = true)
@Composable
fun MatchMakingScreen() {

    val viewModel: MatchMakingViewModel = viewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProfileCard(state.currentCandidate.name, { viewModel.nextCandidate() }, { viewModel.nextCandidate() })
}

@Composable
fun ProfileCard(name: String, onYes: () -> Unit, onNo: () -> Unit) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Column {
            Text(
                text = name,
                modifier = Modifier
                    .padding(10.dp)
            )

            Row {
                Button(onClick = onYes) {
                    Text("Yes")
                }
                Button(onClick = onNo) {
                    Text("No")
                }
            }
        }
    }
}