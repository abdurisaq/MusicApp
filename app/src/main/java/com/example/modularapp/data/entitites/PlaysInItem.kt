package com.example.modularapp.data.entitites

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = ["songId", "playlistId"],
    foreignKeys = [
        ForeignKey(entity = AudioItem::class,
            parentColumns = ["id"],
            childColumns = ["songId"],
            onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Playlist::class,
            parentColumns = ["playlistId"],
            childColumns = ["playlistId"],
            onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index(value = ["playlistId"]), Index(value = ["songId"])]
)
data class PlaysIn(
    val songId: Int,
    val playlistId: Int,
    val playlistPosition: Int
)