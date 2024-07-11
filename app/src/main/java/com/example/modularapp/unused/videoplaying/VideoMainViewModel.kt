package com.example.modularapp.unused.videoplaying

import androidx.media3.common.MediaItem
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


class VideoMainViewModel (
    private val savedStateHandle: SavedStateHandle,
    val player:Player,
    private val metaDataReader: VideoMetaDataReader


):ViewModel() {
    private val videoUris = savedStateHandle.getStateFlow("videoUris", emptyList<Uri>())
    val videoItems = videoUris.map{ uris -> uris.map {
        uri->
        VideoItem(
            content = uri,
            mediaItem = MediaItem.fromUri(uri),
            name = metaDataReader.getMetaDataFromUri(uri)?.fileName?: "No name"
        )
    }}.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    init {
        player.prepare()
    }
    fun addVideoUri(uri: Uri){
        savedStateHandle["videoUris"] = videoUris.value + uri
        player.addMediaItem(MediaItem.fromUri(uri))
    }
    fun playVideo(uri:Uri){
        player.setMediaItem(
            videoItems.value.find { it.content == uri }?.mediaItem ?: return
        )
    }

    override fun onCleared() {
        player.release()
    }

}

