package com.example.modularapp.screens.songs

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.Log
import com.example.modularapp.data.states.SongState
import com.example.modularapp.data.states.SortType
import com.example.modularapp.audio.millisecondsToMinuteAndSeconds



@Composable
fun SongScreen(
    state: SongState,
    onEvent: (SongEvent) ->Unit,
    padding:PaddingValues,
    viewModel: SongViewModel
){
    viewModel.audioItems.collectAsState()
    val context = LocalContext.current

    val preselectedAudio = rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let {
                val title = getFileName(context, uri) ?: "Unknown Title"
                viewModel.addAudioUri(uri,title)
                // Take persistable URI permission
                val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                context.contentResolver.takePersistableUriPermission(uri, takeFlags)
            }
        }
    )

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
                                onEvent(SongEvent.SortSongs(sortType))
                            },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = state.currentSortType == sortType, onClick = {
                                onEvent(SongEvent.SortSongs(sortType))
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
                        Text(text = song.name, fontSize = 20.sp,
                            modifier = Modifier.clickable {
                                if(viewModel.audioItems.value.find { it.content == song.content }== null) {
                                    Log.d("playerAudio", "not in player yet, adding to player songscreen")
                                    viewModel.addAudioUri(song.content,song.name)
                                }else{
                                    Log.d("playerAudio", "song is in player, playing song songscreen")
                                    viewModel.playAudio(song.content) // Adjusted to play audio
//                                    viewModel.player.play()
                                }


                            })
                        Text(text = song.artist, fontSize = 12.sp)
                    }
                    Text(text = millisecondsToMinuteAndSeconds(song.duration), fontSize = 15.sp)
                    IconButton(onClick = {
                        onEvent(SongEvent.DeleteSong(song))
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete song")
                    }

                }
            }


        }


    }



}

fun getFileName(context: Context, uri: Uri): String? {
    var name: String? = null
    val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1) {
                name = it.getString(nameIndex)
            }
        }
    }
    return name
}