package com.example.modularapp.screens.songs


import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.modularapp.audio.AudioDataReader
import com.example.modularapp.AudioPlayerApp
import com.example.modularapp.data.entitites.AudioItem
import com.example.modularapp.data.database.SongDao
import com.example.modularapp.data.entitites.PlaysIn
import com.example.modularapp.data.states.SongState
import com.example.modularapp.data.states.SortType
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
    private val metaDataReader: AudioDataReader,
    private val dao: SongDao,
    private val playlistId: Int


):ViewModel() {
    val player = AudioPlayerApp.appModule.audioPlayer
    private val audioPath = "audioUris$playlistId"
    private val audioUris = savedStateHandle.getStateFlow(audioPath, emptyList<Uri>())

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
        Log.d("test","do we crash here")
        try {
            Log.d("test",audioPath)
            Log.d("test",audioUris.value.toString())
            Log.d("test","do we crash here")
            val updatedList = audioUris.value + uri
            savedStateHandle.set(audioPath, updatedList)
            Log.d("test","do we crash here2")
        } catch (e: Exception) {
            Log.e("ViewModel", "Error updating audioUris: ${e.message}")
        }
        Log.d("test","do we crash here")
        Log.d("test","do we crash here")
            viewModelScope.launch {
                Log.d("test","do we crash here")

                if(playlistId ==-1){
                    Log.d("test","in main song screen")
                    dao.getSongCountFromUri(uri.toString()).collect { listOfAudioItems ->
                        Log.d("test","do we crash here")
                        if (listOfAudioItems.isEmpty()) {
                            Log.d("test","do we crash here")
                            // No songs returned by the query
                            audioItems.value.find { it.content == uri }?.let {
                                Log.d("test","do we crash here")
                                dao.upsertSong(it)
                                Log.d("test","playlist id : $playlistId")

                                Log.d("test","do we crash here")
                                val metadata = MediaMetadata.Builder()
                                    .setTitle(title)
                                    .build()
                                Log.d("test","do we crash here")
                                val mediaItem = MediaItem.Builder()
                                    .setUri(uri) // Replace with your media URI
                                    .setMediaMetadata(metadata)
                                    .build()
                                Log.d("test","mediaItem string passed to addAudioUri $mediaItem")
                                player.addMediaItem(mediaItem)
                            }
                            Log.d("playerAudio","No songs found with the URI: $uri")

                        } else {
                            // Songs found
                            Log.d("playerAudio","Found ${listOfAudioItems.size} songs with the URI: $uri")
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
                }else{
                    Log.d("test","in an actual playlist, add to playsin")
                    dao.getSongCountFromUri(uri.toString()).collect { listOfAudioItems ->
                        val song = listOfAudioItems[0]
                        if(song.id != null){
                            val playlistRelation =  PlaysIn(
                                playlistId = playlistId,
                                songId = song.id,
                                playlistPosition = 0
                            )
                            dao.upsertToPlaylist(playlistRelation)
                        }
                    }
                }

        }
        _state.value.isAddingSong = false
    }


//    if(playlistId!= -1) {
//        val playlistRelation: PlaysIn? = it.id?.let {id ->
//            PlaysIn(
//                playlistId = playlistId,
//                songId = id,
//                playlistPosition = 0
//            )
//        }
//        if (playlistRelation != null) {
//            dao.upsertToPlaylist(playlistRelation)
//        }
//    }
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
    }

    init {
        AudioPlayerApp.appModule.audioPlayer.prepare()

    }

    //https://www.youtube.com/watch?v=08O15lvydB4
    private val _state = MutableStateFlow(SongState())
    private val _sortType = MutableStateFlow(SortType.TITLE)
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _songs = _sortType.flatMapLatest{ sortType ->
        if(playlistId ==-1){
        when(sortType){
            SortType.TITLE -> dao.getSongsOrderedByTitle()
            SortType.TIME -> dao.getSongsOrderedByTimeAdded()
        }
        }else{
            dao.getListOfSongs(playlistId)
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
            is SongEvent.DeleteSong -> {
                try {
                    val updatedUris = audioUris.value.filter { it != event.song.content }
                    savedStateHandle.set(audioPath, updatedUris)
                } catch (e: Exception) {
                    Log.e("ViewModel", "Error updating audioUris: ${e.message}")
                }
                viewModelScope.launch {
                    Log.d("SongViewModel",playlistId.toString())
                    if(playlistId == -1){
                        dao.deleteSong(event.song)

                    }else{
                        val playlistRelation: PlaysIn? = event.song.id?.let {
                            PlaysIn(
                                playlistId = playlistId,
                                songId = it,
                                playlistPosition = 0
                            )
                        }
                        if (playlistRelation != null) {
                            dao.deleteFromPlaylist(playlistRelation)
                        }
                    }
                }

            }
            SongEvent.HideDialog -> {
                _state.update { it.copy(
                    isAddingSong = false
                ) }
            }

            SongEvent.SaveSong -> {
                Log.d("SongViewModel", "Save Song Event Triggered")
                val title = state.value.title
                val duration = state.value.duration

                if(title.isBlank()  || duration == 0){
                    return
                }

                val song = audioItems.value.find { it.name ==title}?: return
                viewModelScope.launch {
                    if(playlistId ==-1){

                        dao.upsertSong(song)
                    }
                    else{
                        val playlistRelation: PlaysIn? = song.id?.let {
                            PlaysIn(
                                playlistId = playlistId,
                                songId = it,
                                playlistPosition = 0
                            )
                        }
                        if (playlistRelation != null) {
                            dao.upsertToPlaylist(playlistRelation)
                        }
                    }

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

            SongEvent.ShowDialog -> {
                _state.update { it.copy(
                    isAddingSong = true
                ) }
            }
            is SongEvent.SortSongs -> {
                _sortType.value = event.sortType
            }
            is SongEvent.SetTitle -> {
                _state.update { it.copy(
                    title = event.title
                ) }
            }
        }
    }


}