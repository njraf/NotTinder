package com.example.nottinder

import android.net.Uri
import androidx.collection.IntList
import androidx.collection.emptyIntList

data class User(val id: Int, val name: String, val biography: String, val pictures: IntList, val pictureUris: List<Uri>)
