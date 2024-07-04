package com.example.modularapp.audioplaying.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface SongDao {

    @Upsert
    suspend fun upsertSong(audioItem: AudioItemSimplified)

    @Delete
    suspend fun deleteSong(audioItem: AudioItemSimplified)

    @Query("SELECT * FROM AudioItemSimplified ORDER BY name ASC")
    fun getSongsOrderedByTitle(): Flow<List<AudioItemSimplified>>

    @Query("SELECT * FROM AudioItemSimplified ORDER BY timestamp DESC")
    fun getSongsOrderedByTimeAdded(): Flow<List<AudioItemSimplified>>

}