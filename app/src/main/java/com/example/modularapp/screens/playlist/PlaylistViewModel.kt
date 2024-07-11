package com.example.modularapp.screens.playlist

import androidx.lifecycle.ViewModel

class PlaylistViewModel(): ViewModel() {


    fun onEvent(event: PlaylistEvent) {


        when (event) {
            is PlaylistEvent.SavePlaylist -> {

            }
            is PlaylistEvent.ShowDialog -> {

            }
            is PlaylistEvent.HideDialog -> {

            }
            is PlaylistEvent.SortPlaylists -> {

            }
            is PlaylistEvent.DeletePlaylist -> {

            }
        }
    }


}