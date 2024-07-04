package com.example.modularapp.pages.content.songs

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.unit.dp
import com.example.modularapp.audioplaying.data.SongState

@Composable
fun AddSongDialog(
    state: SongState,
    onEvent: (SongEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(SongEvent.hideDialog)
        },
        title = { Text(text = "Add song") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = state.title,
                    onValueChange = { onEvent(SongEvent.setTitle(it)) },
                    placeholder = {
                        Text(text = "Title")
                    }
                )
                TextField(
                    value = state.artist,
                    onValueChange = { onEvent(SongEvent.setArtist(it)) },
                    placeholder = {
                        Text(text = "Artist")
                    }
                )
                TextField(
                    value = state.duration.toString(),
                    onValueChange = { onEvent(SongEvent.setDuration(it.toIntOrNull() ?: 0)) },
                    placeholder = {
                        Text(text = "Duration")
                    }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onEvent(SongEvent.saveSong)
            }) {
                Text("Save")
            }
        }
    )
}
//
//val selectAudioLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
//    onResult = { uri ->
//        uri?.let(viewModel::addAudioUri)
//    })


//@Composable
//fun AddSongDialog2(
//    state: SongState,
//    onEvent: (SongEvent) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val selectAudioLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
//    onResult = { uri ->
//        uri?.let(viewModel::addAudioUri)
//    })
//}