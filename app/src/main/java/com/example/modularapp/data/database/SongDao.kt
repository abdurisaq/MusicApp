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

    @Query("SELECT name FROM AudioItem WHERE mediaItem= :mediaItem")
    fun getSongName(mediaItem:MediaItem):List<String>

    @Query("SELECT * FROM Playlist ORDER BY name ASC")
    fun getPlaylistsOrderedByTitle(): Flow<List<Playlist>>

    @Query("SELECT * FROM Playlist ORDER BY timestamp DESC")
    fun getPlaylistsOrderedByTimeAdded(): Flow<List<Playlist>>

//    @Transaction
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

}