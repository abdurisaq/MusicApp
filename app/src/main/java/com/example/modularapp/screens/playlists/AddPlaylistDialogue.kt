package com.example.modularapp.screens.playlists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import com.example.modularapp.data.states.PlaylistState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable

fun AddPlaylistDialogue(
    state: PlaylistState,
    onEvent: (PlaylistsEvent) -> Unit,
    modifier:Modifier = Modifier
){


    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(PlaylistsEvent.HideDialog)
        },
        title = { Text(text = "Add Playlist") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = state.title,
                    onValueChange = { onEvent(PlaylistsEvent.SetTitle(it)) },
                    placeholder = {
                        Text(text = "Title")
                    }
                )


            }
        },
        confirmButton = {
            Button(onClick = {
                onEvent(PlaylistsEvent.SavePlaylist)
            }) {
                Text("Save")
            }
        }
    )
}