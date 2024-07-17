package com.example.modularapp.screens.playlists.playlist



import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modularapp.AudioPlayerApp
import com.example.modularapp.audio.millisecondsToMinuteAndSeconds
import com.example.modularapp.data.states.SortType
import com.example.modularapp.screens.songs.SongEvent
import com.example.modularapp.screens.songs.SongViewModel


@Composable

fun SelectedPlaylistScreen(
    padding :PaddingValues,
    viewModel: SongViewModel,
    mainSongViewModel: SongViewModel,
    playlistId :Int,
){

    viewModel.audioItems.collectAsState()
    val state by  viewModel.state.collectAsState()

    val mainState by mainSongViewModel.state.collectAsState()


    if(state.isAddingSong){
        //AddSongDialog(state = state, onEvent =onEvent )
        Log.d("test","test. clicked")
        SelectSongDialogue(mainState =mainState ,selectedPlaylistState = state, onEvent =viewModel::onEvent , viewModel = viewModel)

    }

    LazyColumn(
        contentPadding = padding,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SortType.values().forEach { sortType ->
                    Row(
                        modifier = Modifier.clickable {
                            (viewModel::onEvent)(SongEvent.SortSongs(sortType))
                        },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = state.currentSortType == sortType, onClick = {
                            (viewModel::onEvent)(SongEvent.SortSongs(sortType))
                        })
                        Text(text = sortType.name)
                    }

                }
            }
        }

        items(state.songs) { song ->

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = song.name.replace("_"," "), fontSize = 20.sp,
                        modifier = Modifier.clickable {


                            if(AudioPlayerApp.appModule.currentPlaylist != playlistId){
                                viewModel.loadPlayer()
                                AudioPlayerApp.appModule.currentPlaylist = playlistId
                            }
                            viewModel.playAudio(song.content)

//                            if(viewModel.audioItems.value.find { it.content == song.content }== null) {
//                                Log.d("playerAudio", "not in player yet, adding to player songscreen")
//                                viewModel.addAudioUri(song.content,song.name)
//                            }else{
//                                Log.d("playerAudio", "song is in player, playing song songscreen")
//                                viewModel.playAudio(song.content) // Adjusted to play audio
////                                    viewModel.player.play()
//                            }


                        })
                    Text(text = song.artist, fontSize = 12.sp)
                }
                Text(text = millisecondsToMinuteAndSeconds(song.duration), fontSize = 15.sp)
                IconButton(onClick = {
                    (viewModel::onEvent)(SongEvent.DeleteSong(song))
                }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete song")
                }

            }
        }


    }

}