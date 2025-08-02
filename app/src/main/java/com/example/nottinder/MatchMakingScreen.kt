package com.example.nottinder

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Preview(showBackground = true)
@Composable
fun MatchMakingScreen() {

    val viewModel: MatchMakingViewModel = viewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProfileCard(
        state.currentCandidate,
        state.imageIndex,
        { viewModel.nextCandidate() },
        { viewModel.nextCandidate() },
        { viewModel.nextImage() })
}

@Composable
fun ProfileCard(
    user: User,
    imageIndex: Int,
    onYes: () -> Unit,
    onNo: () -> Unit,
    onImageClick: () -> Unit
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if(user.pictures.isNotEmpty()) {
                Image(
                    painter = painterResource(user.pictures[imageIndex]),
                    contentDescription = "",
                    modifier = Modifier
                        .clickable(onClick = { onImageClick() })
                        .fillMaxSize()
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Text(
                    text = user.name,
                    fontSize = 25.sp
                )

                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Button(onClick = onYes) {
                        Text("Yes")
                    }
                    Spacer(modifier = Modifier.padding(horizontal = 100.dp))
                    Button(onClick = onNo) {
                        Text("No")
                    }
                }
            }
        }
    }
}