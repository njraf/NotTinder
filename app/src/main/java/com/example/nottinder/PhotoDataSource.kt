package com.example.nottinder

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow

data class PhotoDataSource(private val _photos: MutableStateFlow<List<Uri>>) {
    val photos: StateFlow<List<Uri>> = _photos.asStateFlow()

    fun addPhoto(photo: Uri) {
        _photos.value = _photos.value + photo
    }
}