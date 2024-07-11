package com.example.modularapp.pages.content.songs


import androidx.media3.common.MediaItem
import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import com.example.modularapp.audioplaying.AudioDataReader
import com.example.modularapp.audioplaying.AudioPlayerApp
import com.example.modularapp.audioplaying.data.AudioItem
import com.example.modularapp.audioplaying.data.SongDao
import com.example.modularapp.audioplaying.data.SongState
import com.example.modularapp.audioplaying.data.SortType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class SongViewModel(
    private val savedStateHandle: SavedStateHandle,
    val player:Player,
    private val metaDataReader: AudioDataReader,
    private val dao: SongDao


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

    fun addAudioUri(uri: Uri,title:String){//androidx.media3.common.MediaMetadata@3d1fc8ee
        Log.d("test","title passed to addAudioUri $title")

        try {
            savedStateHandle.set("audioUris", audioUris.value + uri)
        } catch (e: Exception) {
            Log.e("ViewModel", "Error updating audioUris: ${e.message}")
        }

        viewModelScope.launch {

            viewModelScope.launch {
                dao.getSongCountFromUri(uri.toString()).collect { listOfAudioItems ->
                    if (listOfAudioItems.isEmpty()) {
                        // No songs returned by the query
                        audioItems.value.find { it.content == uri }?.let {
                            dao.upsertSong(it)

                            val metadata = MediaMetadata.Builder()
                                .setTitle(title)
                                .build()

                            val mediaItem = MediaItem.Builder()
                                .setUri(uri) // Replace with your media URI
                                .setMediaMetadata(metadata)
                                .build()
                            Log.d("test","mediaItem string passed to addAudioUri $mediaItem")
                            player.addMediaItem(mediaItem)
                        }
                        Log.d("playerAudio","No songs found with the URI: ${uri.toString()}")

                    } else {
                        // Songs found
                        Log.d("playerAudio","Found ${listOfAudioItems.size} songs with the URI: ${uri.toString()}")
                        val metadata = MediaMetadata.Builder()
                            .setTitle(title)
                            .build()

                        val mediaItem = MediaItem.Builder()
                            .setUri(uri) // Replace with your media URI
                            .setMediaMetadata(metadata)
                            .build()
                        Log.d("test","mediaItem string passed to addAudioUri $mediaItem")
                        player.addMediaItem(mediaItem)
                    }
                }
            }



        }
        _state.value.isAddingSong = false
    }
    fun playAudio(uri:Uri){
        val mediaItem = audioItems.value.find { it.content == uri }?.mediaItem
        if(mediaItem == null) {
            Log.d("playerAudio","song isn't in audioItmes $uri")

        }else{
            Log.d("playerAudio","song is in audioItmes $uri")
            Log.d("playerAudio","is player playing ${player.isPlaying}")

            player.setMediaItem(mediaItem)
            player.play()
            Log.d("playerAudio","is player playing ${player.isPlaying}")
        }
//        player.setMediaItem(
//            audioItems.value.find { it.content == uri }?.mediaItem ?: return
//        )
//        player.play()
    }


    override fun onCleared() {
        player.release()
        AudioPlayerApp.appModule.audioPlayer.release()
    }

    private val _state = MutableStateFlow(SongState())
    private val _sortType = MutableStateFlow(SortType.TITLE)
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _songs = _sortType.flatMapLatest{ sortType ->
        when(sortType){
            SortType.TITLE -> dao.getSongsOrderedByTitle()
            SortType.TIME -> dao.getSongsOrderedByTimeAdded()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state = combine(_state,_sortType, _songs){ state,sortType,songs ->
        state.copy(
            songs = songs,
            currentSortType = sortType,

            )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SongState())
    fun onEvent(event: SongEvent){
        when(event){
            is SongEvent.deleteSong -> {
                try {
                    val updatedUris = audioUris.value.filter { it != event.song.content }
                    savedStateHandle.set("audioUris", updatedUris)
                } catch (e: Exception) {
                    Log.e("ViewModel", "Error updating audioUris: ${e.message}")
                }
                viewModelScope.launch {

                    dao.deleteSong(event.song)
                    player
                }

            }
            SongEvent.hideDialog -> {
                _state.update { it.copy(
                    isAddingSong = false
                ) }
            }
            SongEvent.saveSong -> {
                Log.d("SongViewModel", "Save Song Event Triggered")
                val title = state.value.title
                val artist = state.value.artist
                val duration = state.value.duration

                if(title.isBlank()  || duration == 0){
                    return
                }

                val song = audioItems.value.find { it.name ==title}?: return
                viewModelScope.launch {
                    dao.upsertSong(song)
                }

                _state.update {
                    val newState = it.copy(
                        isAddingSong = false,
                        title = "",
                        artist = "",
                        duration = 0,
                    )
                    Log.d("SongViewModel", "saveSong: $newState")
                    newState

                }

            }
            is SongEvent.setArtist -> {
                _state.update { it.copy(
                    artist = event.artist
                ) }
            }
            is SongEvent.setDuration -> {
                _state.update { it.copy(
                    duration = event.duration
                ) }
            }
            is SongEvent.setTitle -> {
                _state.update { it.copy(
                    title = event.title
                ) }
            }
            SongEvent.showDialog -> {
                _state.update { it.copy(
                    isAddingSong = true
                ) }
            }
            is SongEvent.sortSongs -> {
                _sortType.value = event.sortType
            }
        }
    }


}