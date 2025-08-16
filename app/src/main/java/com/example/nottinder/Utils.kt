package com.example.nottinder

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Log
import java.io.IOException

fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
    val contentResolver = context.contentResolver
    return try {
        val source = ImageDecoder.createSource(contentResolver, uri)
        ImageDecoder.decodeBitmap(source)
    } catch (e: IOException) {
        Log.e("image", e.message ?: "Unknown error: could not change URI to bitmap")
        null
    }
}