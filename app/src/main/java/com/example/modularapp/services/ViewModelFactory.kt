package com.example.modularapp.services

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.media3.common.Player
import androidx.savedstate.SavedStateRegistryOwner
import com.example.modularapp.audio.AudioDataReader
import com.example.modularapp.data.database.SongDao
import com.example.modularapp.screens.songs.SongViewModel

class SongViewModelFactory(
    private val dao: SongDao,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null,
    private val metaDataReader: AudioDataReader,
    private val player: Player
) :AbstractSavedStateViewModelFactory(owner,defaultArgs){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(SongViewModel::class.java)) {

            return SongViewModel(handle,metaDataReader,dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
