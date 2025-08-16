package com.example.nottinder

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.CancellationSignal
import android.text.Layout
import android.util.Log
import android.util.Size
import android.widget.GridLayout
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import java.io.IOException

fun Modifier.roundedBorder(): Modifier = this.border(
    width = 2.dp,
    color = Color.Black,
    shape = RoundedCornerShape(15.dp)
)

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    topPage: Route,
    onBottomNavigate: (Route) -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val photoUris = remember { state.self.pictureUris.toMutableStateList() }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomBar(topPage, onBottomNavigate)
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            PhotoSelectorArea(
                photoUris,
                { uri ->
                    if (uri !in photoUris) {
                        photoUris.add(uri)
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("Cannot add the same photo twice")
                        }
                    }
                },
                { uri ->
                    photoUris.remove(uri)
                })

            InputFields(state.self) { newName, newBio ->
                if (newName.isEmpty() || newBio.isEmpty() || photoUris.isEmpty()) {
                    scope.launch {
                        snackbarHostState.showSnackbar("Empty field or photo")
                    }
                    return@InputFields
                }
                viewModel.updateName(newName)
                viewModel.updateBio(newBio)
                viewModel.setPhotos(photoUris.toList())
            }
        }
    }
}

@Composable
fun InputFields(user: User, onSubmit: (String, String) -> Unit) {
    var name by rememberSaveable { mutableStateOf(user.name) }
    var bio by rememberSaveable { mutableStateOf(user.biography) }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(5.dp)
        ) {
            val textModifier =
                Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterHorizontally)

            item(span = { GridItemSpan(1) }) {
                Text(
                    "Name",
                    fontSize = 20.sp,
                    modifier = textModifier
                )
            }
            item(span = { GridItemSpan(2) }) {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    singleLine = true)
            }
            item(span = { GridItemSpan(1) }) {
                Text(
                    "Bio",
                    fontSize = 20.sp,
                    modifier = textModifier
                )
            }
            item(span = { GridItemSpan(2) }) {
                TextField(
                    value = bio,
                    onValueChange = { bio = it },
                    singleLine = true)
            }
        }

        Spacer(Modifier.weight(1f))

        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.8f),
            onClick = {
                onSubmit(name, bio)
            }) { Text("Save") }
    }
}

@Composable
fun PhotoSelector(photoURI: Uri?, onPhotoAdded: (Uri) -> Unit, onPhotoDeleted: (Uri) -> Unit) {

    var pickMedia: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>? = null
    var legacyPickerLauncher: ManagedActivityResultLauncher<String, Uri?>? = null

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        pickMedia =
            rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    Log.d("PhotoPicker", "Selected URI: $uri")
                    onPhotoAdded(uri)
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }

    } else {
        legacyPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            if (uri != null) {
                onPhotoAdded(uri)
            }
        }
    }



    Box( // rounded rect with plus
        modifier = Modifier
            .height(200.dp)
            .padding(2.dp)
    ) {
        if (photoURI == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .roundedBorder()
                    .clickable {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            (pickMedia as ActivityResultLauncher<PickVisualMediaRequest>)
                                .launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        } else {
                            legacyPickerLauncher?.launch("image/*")
                        }
                    }
            ) {
                Text(
                    "+",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center), fontSize = 30.sp
                )
            }
        } else {
            val photoBitmap: Bitmap? = uriToBitmap(LocalContext.current, photoURI)

            if (photoBitmap != null) {
                Box {
                    Image(
                        BitmapPainter(photoBitmap.asImageBitmap()),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = "  -  ",
                        fontSize = 30.sp,
                        color = Color.Red,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .border(
                                width = 2.dp,
                                color = Color.Black,
                                shape = RoundedCornerShape(20.dp))
                            .clickable {
                                onPhotoDeleted(photoURI)
                            }
                    )
                }
            } else {
                Text(
                    "+", modifier = Modifier
                    .align(Alignment.Center),
                    fontSize = 30.sp
                )
            }
        }
    }
}

@Composable
fun PhotoSelectorArea(
    photoURIs: List<Uri>,
    onPhotoAdded: (Uri) -> Unit,
    onPhotoDeleted: (Uri) -> Unit
) {
    LazyVerticalGrid(columns = GridCells.Fixed(3)) {
        items(6) { idx ->
            val uri: Uri? = if (photoURIs.size > idx) photoURIs[idx] else null
            PhotoSelector(uri, onPhotoAdded, onPhotoDeleted)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPhotoSelectorArea() {
    PhotoSelectorArea(emptyList(), {}, {})
}

@Preview(showBackground = true)
@Composable
fun PreviewInputFields() {
    InputFields(User(-1, "", "", emptyList())) { s1, s2 -> }
}