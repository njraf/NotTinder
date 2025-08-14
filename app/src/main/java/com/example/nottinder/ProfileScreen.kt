package com.example.nottinder

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.CancellationSignal
import android.util.Log
import android.util.Size
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
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
        Column(modifier = Modifier.padding(innerPadding)) {
            PhotoSelectorArea(state.self.pictureUris, { uri ->
                viewModel.addPhoto(uri)
                //onProfileUpdated(state.self)
            })

            InputFields { newName, newBio ->
                viewModel.updateName(newName)
                viewModel.updateBio(newBio)
            }
        }
    }
}

@Composable
fun InputFields(onSubmit: (String, String) -> Unit) {
    var name by rememberSaveable { mutableStateOf("") }
    var bio by rememberSaveable { mutableStateOf("") }
    Row {
        Text("Name")
        TextField(value = name, onValueChange = { name = it })
    }
    Row {
        Text("Bio")
        TextField(value = bio, onValueChange = { bio = it })
    }
    Button(onClick = {
        onSubmit(name, bio)
        name = ""
        bio = ""
    }) { Text("Save") }
}

@Composable
fun PhotoSelector(photoURI: Uri?, onPhotoAdded: (self: Uri) -> Unit) {

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

    Box(
        modifier = Modifier
            .height(200.dp)
            .padding(1.dp)
            //.background(color = Color.Red)
            .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(15.dp))
            .clickable {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    (pickMedia as androidx.activity.result.ActivityResultLauncher<PickVisualMediaRequest>)
                        .launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                } else {
                    legacyPickerLauncher?.launch("image/*")
                }
            }) {
        if (photoURI == null) {
            Text("+", modifier = Modifier.align(Alignment.Center), fontSize = 30.sp)
        } else {
            val contentResolver = LocalContext.current.contentResolver
            val thumbnail: Bitmap? = try {
                contentResolver.loadThumbnail(
                    photoURI, Size(10, 10),
                    CancellationSignal()
                )

            } catch (e: java.io.IOException) {
                Log.e("images", "Could not load profile image: ${e.message}")
                null
            }

            if (thumbnail != null) {
                Image(
                    BitmapPainter(thumbnail.asImageBitmap()),
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            } else {
                Text("+", modifier = Modifier.align(Alignment.Center), fontSize = 30.sp)
            }
        }
    }
}

@Composable
fun PhotoSelectorArea(photoURIs: List<Uri>, onPhotoAdded: (self: Uri) -> Unit) {
    requestReadMediaPermission()

    LazyVerticalGrid(columns = GridCells.Fixed(3)) {
        items(6) { idx ->
            val uri: Uri? = if (photoURIs.size > idx) photoURIs[idx] else null
            PhotoSelector(uri, onPhotoAdded)
        }
    }
}

@Composable
fun requestReadMediaPermission() {
    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {

            } else {

            }
        }

    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        when (ContextCompat.checkSelfPermission(context, permission)) {
            PackageManager.PERMISSION_GRANTED -> {
                //onPermissionGranted()
            }

            else -> {
                permissionLauncher.launch(permission)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPhotoSelectorArea() {
    PhotoSelectorArea(emptyList(), {})
}