package com.example.modularapp.screens.playlists

import com.example.modularapp.data.entitites.Playlist
import com.example.modularapp.data.states.SortType

sealed interface PlaylistsEvent {
    data object SavePlaylist: PlaylistsEvent
    data object ShowDialog: PlaylistsEvent

    data object HideDialog: PlaylistsEvent

    data class SetTitle(val title:String):PlaylistsEvent
    data class SortPlaylists(val sortType: SortType): PlaylistsEvent
    data class DeletePlaylist(val playlist: Playlist): PlaylistsEvent

}