package com.example.modularapp.audioplaying.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
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

    @Transaction
    @Query("SELECT * FROM PlaysIn WHERE playlistId = :playlistId ORDER BY playlistPosition")
    suspend fun getPlaylist(playlistId: Int): List<PlaysIn>

    @Transaction
    @Query("""
        SELECT AudioItem.* FROM PlaysIn 
        INNER JOIN AudioItem ON PlaysIn.songId = AudioItem.id 
        WHERE PlaysIn.playlistId = :playlistId 
        ORDER BY PlaysIn.playlistPosition
    """)
    suspend fun getListOfSongs(playlistId: Int): List<AudioItem>

}