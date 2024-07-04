package com.example.modularapp.audioplaying

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.Player
import androidx.savedstate.SavedStateRegistryOwner
import com.example.modularapp.audioplaying.data.SongDao
import com.example.modularapp.pages.content.songs.SongViewModel

class ViewModelFactory2(
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
        if (modelClass.isAssignableFrom(MainViewModel2::class.java)) {
            return MainViewModel2(handle,player,metaDataReader) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class SongViewModelFactory(private val dao: SongDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SongViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SongViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}