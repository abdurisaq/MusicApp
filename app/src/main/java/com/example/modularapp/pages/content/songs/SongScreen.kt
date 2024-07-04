package com.example.modularapp.pages.content.songs

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modularapp.audioplaying.data.SongState
import com.example.modularapp.audioplaying.data.SortType

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
                    Text(text = song.duration.toString(), fontSize = 15.sp)
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
    padding:PaddingValues
){


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
                    Text(text = song.duration.toString(), fontSize = 15.sp)
                    IconButton(onClick = {
                        onEvent(SongEvent.deleteSong(song))
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete song" )
                    }

                }
            }

        }





}