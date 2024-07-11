package com.example.modularapp.screens.playlist

import com.example.modularapp.data.entitites.AudioItem
import com.example.modularapp.data.states.SortType

sealed interface PlaylistEvent {
    data object SavePlaylist: PlaylistEvent
    data object ShowDialog: PlaylistEvent

    data object HideDialog: PlaylistEvent


    data class SortPlaylists(val sortType: SortType): PlaylistEvent
    data class DeletePlaylist(val song: AudioItem): PlaylistEvent

}