package com.example.modularapp.data.entitites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Playlist(
    val name: String,
    val length:Int,
    @PrimaryKey(autoGenerate = true)
    val playlistId:Int? = null
)