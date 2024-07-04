package com.example.modularapp.audioplaying.data

import android.net.Uri
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface SongDao {

    @Upsert
    suspend fun upsertSong(audioItem: AudioItem)

    @Delete
    suspend fun deleteSong(audioItem: AudioItem)

    @Query("SELECT * FROM AudioItem ORDER BY name ASC")
    fun getSongsOrderedByTitle(): Flow<List<AudioItem>>

    @Query("SELECT * FROM AudioItem ORDER BY timestamp DESC")
    fun getSongsOrderedByTimeAdded(): Flow<List<AudioItem>>

    @Query("SELECT * FROM AudioItem WHERE content= :uriString")
    fun getSongCountFromUri(uriString: String):Flow<List<AudioItem>>
}