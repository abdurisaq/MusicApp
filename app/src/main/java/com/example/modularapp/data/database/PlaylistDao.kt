package com.example.modularapp.data.database

import androidx.media3.common.MediaItem
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.modularapp.data.entitites.AudioItem
import com.example.modularapp.data.entitites.Playlist
import kotlinx.coroutines.flow.Flow


@Dao
interface PlaylistDao {

    @Upsert
    suspend fun upsertPlaylist(playlist: Playlist)

    @Delete
    suspend fun deletePlaylist(playlist: Playlist)


    @Query("SELECT * FROM Playlist ORDER BY name ASC")
    fun getPlaylistsOrderedByTitle(): Flow<List<Playlist>>

    @Query("SELECT * FROM Playlist ORDER BY timestamp DESC")
    fun getPlaylistsOrderedByTimeAdded(): Flow<List<Playlist>>
}