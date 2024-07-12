package com.example.modularapp.data.states

import com.example.modularapp.data.entitites.Playlist

data class PlaylistState(
    val playlists : List<Playlist> = emptyList(),
    val title:String = "",
    var isAddingPlaylist: Boolean = false,
    val currentSortType: SortType = SortType.TITLE

)