package com.example.nottinder

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject
import javax.inject.Singleton

class PhotoDataSource @Inject constructor() {

    private val _photos: MutableStateFlow<List<Uri>> = MutableStateFlow(emptyList())
    val photos: StateFlow<List<Uri>> = _photos.asStateFlow()

    fun addPhoto(photo: Uri) {
        _photos.value = _photos.value + photo
    }
}