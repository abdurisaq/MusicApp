package com.example.modularapp.audioplaying


import androidx.media3.common.MediaItem
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import com.example.modularapp.audioplaying.data.AudioItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


class MainViewModel2 (
    private val savedStateHandle: SavedStateHandle,
    val player:Player,
    private val metaDataReader: AudioDataReader


):ViewModel() {
    private val audioUris = savedStateHandle.getStateFlow("audioUris", emptyList<Uri>())
    val audioItems = audioUris.map{ uris -> uris.map {
            uri->
        AudioItem(
            content = uri,
            mediaItem = MediaItem.fromUri(uri),
            name = metaDataReader.getMetaDataFromUri(uri)?.fileName?: "No name",
            duration = metaDataReader.getMetaDataFromUri(uri)?.duration?: 999,
            artist = metaDataReader.getMetaDataFromUri(uri)?.artist?: "No name"
        )
    }}.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    init {
        player.prepare()
    }
    fun addAudioUri(uri: Uri){
        savedStateHandle["audioUris"] = audioUris.value + uri
        player.addMediaItem(MediaItem.fromUri(uri))
    }
    fun playAudio(uri:Uri){
        player.setMediaItem(
            audioItems.value.find { it.content == uri }?.mediaItem ?: return
        )
    }


    override fun onCleared() {
        player.release()
    }


}