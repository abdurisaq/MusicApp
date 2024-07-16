package com.example.modularapp.data.database

import androidx.media3.common.MediaItem
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.modularapp.data.entitites.AudioItem
import com.example.modularapp.data.entitites.PlaysIn
import kotlinx.coroutines.flow.Flow


//@Dao
//interface PlaysInDao {
//
//
//    @Upsert
//    suspend fun upsertSong(audioItem: AudioItem,playlistId: Int)
//
//    @Delete
//    suspend fun deleteSong(audioItem: AudioItem,playlistId: Int)
//        @Transaction
//    @Query("SELECT * FROM PlaysIn WHERE playlistId = :playlistId ORDER BY playlistPosition")
//    suspend fun getPlaylist(playlistId: Int): List<PlaysIn>
//
//    @Transaction
//    @Query("""
//        SELECT AudioItem.* FROM PlaysIn
//        INNER JOIN AudioItem ON PlaysIn.songId = AudioItem.id
//        WHERE PlaysIn.playlistId = :playlistId
//        ORDER BY PlaysIn.playlistPosition
//    """)
//    suspend fun getListOfSongs(playlistId: Int): List<AudioItem>
//
//    @Query("SELECT * FROM AudioItem WHERE mediaItem= :mediaItem")
//    fun getSongID(mediaItem: MediaItem):List<AudioItem>
//    @Query("SELECT * FROM PlaysIn WHERE songId= :songId AND playlistId = playlistId")
//    fun getSongCountFromUriAndPlaylist(songId: Int,playlistId:Int): Flow<List<AudioItem>>
//
//    @Query("SELECT * FROM AudioItem ORDER BY name ASC")
//    fun getSongsOrderedByTitle(): Flow<List<AudioItem>>
//
//    @Query("SELECT * FROM AudioItem ORDER BY timestamp DESC")
//    fun getSongsOrderedByTimeAdded(): Flow<List<AudioItem>>
//
//
//}