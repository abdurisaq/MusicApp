package com.example.modularapp.screens.playlists

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modularapp.data.states.PlaylistState
import com.example.modularapp.data.states.SortType
import com.example.modularapp.screens.songs.SongEvent


@Composable
fun PlaylistScreen(
    state: PlaylistState,
    onEvent: (PlaylistsEvent) ->Unit,
    padding: PaddingValues,
    viewModel: PlaylistViewModel

) {

    if(state.isAddingPlaylist){
        AddPlaylistDialogue(state = state, onEvent =onEvent )
    }

    LazyColumn {

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
                            onEvent(PlaylistsEvent.SortPlaylists(sortType))
                        },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = state.currentSortType == sortType, onClick = {
                            onEvent(PlaylistsEvent.SortPlaylists(sortType))
                        })
                        Text(text = sortType.name)
                    }

                }
            }
        }

        items(state.playlists){
            playlist->

            Row {
                Text(playlist.name,
                    modifier = Modifier.clickable {
                        Log.d("test","clicked")
                    },
                    fontSize = 30.sp
                    )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = playlist.length.toString(), fontSize = 30.sp)
                IconButton(onClick = {
                    onEvent(PlaylistsEvent.DeletePlaylist(playlist))
                }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete playlist")
                }
            }
        }
    }
}