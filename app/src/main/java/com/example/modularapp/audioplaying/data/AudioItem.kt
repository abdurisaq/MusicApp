package com.example.modularapp.audioplaying.data


import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class AudioItem(

    val content : Uri,
    val mediaItem: MediaItem,
    val name : String,
    val duration: Int,
    val artist: String,
    val timestamp: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true)
    val id:Int? = 0

)

@Entity
data class AudioItemSimplified(

    val name : String,
    val duration: Int,
    val artist: String,

    val timestamp: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true)
    val id:Int? = null

)
