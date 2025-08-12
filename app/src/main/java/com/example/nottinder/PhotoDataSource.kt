package com.example.nottinder

import android.net.Uri
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoDataSource @Inject constructor() {

    private val _photos: MutableStateFlow<List<Uri>> = MutableStateFlow(emptyList())
    val photos: StateFlow<List<Uri>> = _photos.asStateFlow()

    init {
        Log.d("photo", "PhotoDataSource init") // should only run once
    }

    fun addPhoto(photo: Uri) {
        _photos.value = _photos.value + photo
    }
}