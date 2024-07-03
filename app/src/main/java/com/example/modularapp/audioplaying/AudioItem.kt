package com.example.modularapp.audioplaying


import android.net.Uri
import androidx.media3.common.MediaItem

data class AudioItem(
    val content : Uri,
    val mediaItem: MediaItem,
    val name : String,
    val duration: Int,
    val artist: String
    )
