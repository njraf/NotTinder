package com.example.nottinder

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    topPage: Route,
    onBottomNavigate: (Route) -> Unit,
    onProfileUpdated: (self: User) -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomBar(topPage, onBottomNavigate)
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            PhotoSelectorArea(viewModel, onProfileUpdated)

            var bio by rememberSaveable { mutableStateOf("") }
            TextField(value = bio, onValueChange = { bio = it })
            Button(onClick = {
                viewModel.updateBio(bio)
                //bio = ""
                onProfileUpdated(state.self)
            }) { Text("Submit") }
        }
    }
}

@Composable
fun PhotoSelector(viewModel: ProfileViewModel, onProfileUpdated: (self: User) -> Unit) {



    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val pickMedia =
            rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    Log.d("PhotoPicker", "Selected URI: $uri")
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }

        Box(modifier = Modifier.background(color = Color.Red)) {
            Text("asd", modifier = Modifier.clickable{
            (pickMedia as androidx.activity.result.ActivityResultLauncher<PickVisualMediaRequest>)
                .launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            })
        }

    } else {
        val legacyPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            //onPhotosSelected(listOfNotNull(uri))
        }
        legacyPickerLauncher.launch("image/*")
    }
}

@Composable
fun PhotoSelectorArea(viewModel: ProfileViewModel, onProfileUpdated: (self: User) -> Unit) {
    requestReadMediaPermission(viewModel)

    LazyVerticalGrid(columns = GridCells.Fixed(3)) {
        items(6) {
            PhotoSelector(viewModel, onProfileUpdated)
        }
    }
}

@Composable
fun requestReadMediaPermission(viewModel: ProfileViewModel) {
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