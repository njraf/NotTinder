package com.example.nottinder

import android.graphics.Bitmap
import android.net.Uri
import android.os.CancellationSignal
import android.util.Log
import android.util.Size
import androidx.collection.emptyIntList
import androidx.collection.intListOf
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun MatchMakingScreen(
    viewModel: MatchMakingViewModel,
    topPage: Route,
    onBottomNavigate: (Route) -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomBar(topPage, onBottomNavigate)
        }
    ) { innerPadding ->
            ProfileCard(
                user = state.currentCandidate,
                uri = state.photoUri,
                onYes = { viewModel.nextCandidate() },
                onNo = { viewModel.nextCandidate() },
                onImageClick = { viewModel.changeImage(it) },
                modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun ProfileCard(
    user: User,
    uri: Uri,
    onYes: () -> Unit,
    onNo: () -> Unit,
    onImageClick: (leftTap: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        if (/*user.pictures.isNotEmpty() && */user.name.isNotEmpty()) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (user.pictureUris.isNotEmpty()) {
                    UserImage(uri, onImageClick)
                } else {
                    Text(text = "No photos found", modifier = Modifier.align(Alignment.Center))
                }

                BioAndButtons(
                    user, onYes, onNo, modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .align(Alignment.BottomCenter)
                )
            }
        } else {
            Text(text = "No user found", modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}

@Composable
fun UserImage(uri: Uri, onImageClick: (Boolean) -> Unit) {
    val contentResolver = LocalContext.current.contentResolver
    val thumbnail: Bitmap? = try {
        contentResolver.loadThumbnail(
            uri, Size(10, 10),
            CancellationSignal()
        )

    } catch (e: java.io.IOException) {
        Log.e("images", "Could not load profile image: ${e.message}")
        null
    }

    if (thumbnail != null) {
        var imageWidth by remember { mutableIntStateOf(0) }
        Image(
            painter = BitmapPainter(thumbnail.asImageBitmap()),
            contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = Modifier
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
    } else {
        Text("ERROR: Could not load image")
    }
}

@Composable
fun BioAndButtons(
    user: User,
    onYes: () -> Unit,
    onNo: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = user.name,
            fontSize = 25.sp
        )

        Text(text = user.biography)

        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            val buttonsEnabled = user.name != ""
            Button(
                enabled = buttonsEnabled,
                onClick = onYes
            ) {
                Text("Like")
            }
            Spacer(modifier = Modifier.padding(horizontal = 100.dp))
            Button(
                enabled = buttonsEnabled,
                onClick = onNo
            ) {
                Text("Dislike")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileCard() {
    val user = User(1, "Jack", "First name: Lumber", emptyList())
    ProfileCard(user, "".toUri(), {}, {}, {})
}

@Preview(showBackground = true)
@Composable
fun PreviewBioAndButtons() {
    val user = User(1, "Jack", "First name: Lumber", emptyList())
    BioAndButtons(user, {}, {})
}