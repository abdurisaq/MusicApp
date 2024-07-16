package com.example.modularapp.services

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.Player
import androidx.savedstate.SavedStateRegistryOwner
import com.example.modularapp.audio.AudioDataReader
import com.example.modularapp.data.database.PlaylistDao
import com.example.modularapp.data.database.SongDao
import com.example.modularapp.screens.playlists.PlaylistViewModel
import com.example.modularapp.screens.songs.SongViewModel

class SongViewModelFactory(
    private val dao: SongDao,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null,
    private val metaDataReader: AudioDataReader,
    private val playlistId:Int = -1
) :AbstractSavedStateViewModelFactory(owner,defaultArgs){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(SongViewModel::class.java)) {

            return SongViewModel(handle,metaDataReader,dao,playlistId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class PlaylistViewModelFactory(private val dao: PlaylistDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlaylistViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlaylistViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
