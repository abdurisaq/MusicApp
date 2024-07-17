package com.example.modularapp.screens.playlists.playlist


import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modularapp.audio.millisecondsToMinuteAndSeconds
import com.example.modularapp.data.states.SongState
import com.example.modularapp.screens.songs.SongEvent
import com.example.modularapp.screens.songs.SongViewModel

@Composable
fun SelectSongDialogue(
    mainState: SongState,
    selectedPlaylistState:SongState,
    onEvent: (SongEvent) -> Unit,
    modifier:Modifier = Modifier,
    viewModel: SongViewModel
){
    val songsToShow = mainState.songs
    val songsToRemoveSet = selectedPlaylistState.songs.toSet()
    val filteredSongs = songsToShow.filterNot { it in songsToRemoveSet }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(SongEvent.HideDialog)
        },
        title = { Text(text = "Add Song") },
        text = {
            LazyColumn(

                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                items(filteredSongs){ song ->
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = song.name, fontSize = 20.sp,
                                modifier = Modifier.clickable {
                                    if(viewModel.audioItems.value.find { it.content == song.content }== null) {
                                        Log.d("playerAudio", "not in player yet, adding to player songscreen")
                                        viewModel.addAudioUri(song.content,song.name)
                                    }else{
                                        Log.d("playerAudio", "song is in player, playing song songscreen")
                                        //viewModel.playAudio(song.content) // Adjusted to play audio
                                        onEvent(SongEvent.HideDialog)
                                    }


                                })
                            Text(text = song.artist, fontSize = 12.sp)
                        }
                        Text(text = millisecondsToMinuteAndSeconds(song.duration), fontSize = 15.sp)

                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onEvent(SongEvent.SaveSong)
            }) {
                Text("Save")
            }
        }
    )
}