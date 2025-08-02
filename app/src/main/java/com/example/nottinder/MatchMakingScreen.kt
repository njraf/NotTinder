package com.example.nottinder

import androidx.collection.emptyIntList
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
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
        user = state.currentCandidate,
        imageID = state.imageID,
        onYes = { viewModel.nextCandidate() },
        onNo = { viewModel.nextCandidate() },
        onImageClick = { viewModel.changeImage(it) })
}

@Composable
fun ProfileCard(
    user: User,
    imageID: Int,
    onYes: () -> Unit,
    onNo: () -> Unit,
    onImageClick: (leftTap: Boolean) -> Unit
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        if (user.pictures.isNotEmpty() && user.name.isNotEmpty()) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (user.pictures.isNotEmpty()) {
                    UserImage(imageID, onImageClick)
                } else {
                    Text(text = "No user found", modifier = Modifier.align(Alignment.Center))
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

                    Text(text = user.biography)

                    Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        Button(
                            enabled = user.name != "" && user.pictures.isNotEmpty(),
                            onClick = onYes
                        ) {
                            Text("Yes")
                        }
                        Spacer(modifier = Modifier.padding(horizontal = 100.dp))
                        Button(
                            enabled = user.name != "" && user.pictures.isNotEmpty(),
                            onClick = onNo
                        ) {
                            Text("No")
                        }
                    }
                }
            }
        } else {
            Text(text = "No user found", modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}

@Composable
fun UserImage(imageID: Int, onImageClick: (Boolean) -> Unit) {
    val imageResource = painterResource(imageID)
    var imageWidth by remember { mutableIntStateOf(0) }
    Image(
        painter = imageResource,
        contentDescription = "",
        modifier = Modifier
            //.clickable(onClick = { onImageClick() })
            .onGloballyPositioned {
                imageWidth = it.size.width
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { offset ->
                        val leftTap =
                            if (offset.x < imageWidth / 2) {
                                true
                            } else {
                                false
                            }
                        onImageClick(leftTap)
                    }
                )
            }
            .fillMaxSize()
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileCard() {
    val user = User("", "", emptyIntList())
    ProfileCard(user, 0, {}, {}, {})
}