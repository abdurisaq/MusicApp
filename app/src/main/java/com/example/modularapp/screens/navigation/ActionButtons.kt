package com.example.modularapp.screens.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlaylistAddCircle
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.example.modularapp.screens.playlists.PlaylistsEvent
import com.example.modularapp.screens.songs.SongEvent

@Composable
fun FloatingPlaylistButton(
    onEvent: (PlaylistsEvent) ->Unit
){
    FloatingActionButton(onClick = {onEvent(PlaylistsEvent.ShowDialog) }) {
        Icon(Icons.Default.PlaylistAddCircle, contentDescription = "Add Playlist")
    }
}

@Composable
fun FloatingSongButton(

    onEvent: (SongEvent) ->Unit
){
    FloatingActionButton(onClick = {onEvent(SongEvent.ShowDialog) }) {
        Icon(Icons.Default.Add, contentDescription = "Add Song")
    }

}

