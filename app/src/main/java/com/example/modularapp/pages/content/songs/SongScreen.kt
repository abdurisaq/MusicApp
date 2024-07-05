package com.example.modularapp.pages.content.songs

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.Log
import com.example.modularapp.audioplaying.data.SongState
import com.example.modularapp.audioplaying.data.SortType
import com.example.modularapp.pages.content.playing.millisecondsToMinuteAndSeconds

@Composable
fun SongScreen2(
    state: SongState,
    onEvent: (SongEvent) ->Unit,
    //padding:PaddingValues
){
    Scaffold(
        floatingActionButton = { FloatingButton(onEvent) }
    ) {
        padding ->
        if(state.isAddingSong){
            AddSongDialog(state = state, onEvent =onEvent )
        }
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    SortType.values().forEach {sortType ->
                        Row(
                            modifier = Modifier.clickable {
                                onEvent(SongEvent.sortSongs(sortType))
                            },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = state.currentSortType ==sortType, onClick = {
                                onEvent(SongEvent.sortSongs(sortType))
                            })
                            Text(text = sortType.name)
                        }

                    }
                }
            }

            items(state.songs){
                    song ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = song.name, fontSize = 20.sp)
                        Text(text = song.artist, fontSize = 12.sp)
                    }
                    Text(text = millisecondsToMinuteAndSeconds(song.duration), fontSize = 15.sp)
                    IconButton(onClick = {
                        onEvent(SongEvent.deleteSong(song))
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete song" )
                    }

                }
            }

        }
    }




}

@Composable
fun FloatingButton(

    onEvent: (SongEvent) ->Unit
){
    FloatingActionButton(onClick = {onEvent(SongEvent.showDialog) }) {
        Icon(Icons.Default.Add, contentDescription = "Add Song")
    }

}


@Composable
fun SongScreen(
    state: SongState,
    onEvent: (SongEvent) ->Unit,
    padding:PaddingValues,
    viewModel: SongViewModel
){
        val audioItems by viewModel.audioItems.collectAsState()
        val context = LocalContext.current
        val selectAudioLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
            onResult = { uri ->
                uri?.let {
                    viewModel.addAudioUri(uri)
                    // Take persistable URI permission
                    val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    context.contentResolver.takePersistableUriPermission(uri, takeFlags)
                }
            }
        )
    val preselectedAudio = rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let {
                viewModel.addAudioUri(uri)
                // Take persistable URI permission
                val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                context.contentResolver.takePersistableUriPermission(uri, takeFlags)
            }
        }
    )
        var uriToAdd by remember { mutableStateOf<Uri?>(null) }

        if(state.isAddingSong){
            //AddSongDialog(state = state, onEvent =onEvent )
            preselectedAudio.launch(arrayOf("audio/*"))

        }
    Column (
        verticalArrangement = Arrangement.SpaceEvenly
    ){



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
                                onEvent(SongEvent.sortSongs(sortType))
                            },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = state.currentSortType == sortType, onClick = {
                                onEvent(SongEvent.sortSongs(sortType))
                            })
                            Text(text = sortType.name)
                        }

                    }
                }
            }

            items(state.songs) { song ->
//                Log.d("playerAudio", song.name)
//                Log.d("playerAudio", song.artist)
//                Log.d("playerAudio", song.duration.toString())
//                Log.d("playerAudio", song.content.toString())
//                Log.d("playerAudio", song.mediaItem.toString())
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = song.name, fontSize = 20.sp,
                            modifier = Modifier.clickable {
                                if(viewModel.audioItems.value.find { it.content == song.content }== null) {
                                    Log.d("playerAudio", "not in player yet, adding to player")
                                    viewModel.addAudioUri(song.content)
                                }else{
                                    viewModel.playAudio(song.content) // Adjusted to play audio
                                    viewModel.player.play()
                                }


                            })
                        Text(text = song.artist, fontSize = 12.sp)
                    }
                    Text(text = millisecondsToMinuteAndSeconds(song.duration), fontSize = 15.sp)
                    IconButton(onClick = {
                        onEvent(SongEvent.deleteSong(song))
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete song")
                    }

                }
            }


        }


    }

    LaunchedEffect(uriToAdd) {
        uriToAdd?.let {
            viewModel.addAudioUri(it)
            uriToAdd = null // Reset the URI after adding
        }
    }

}