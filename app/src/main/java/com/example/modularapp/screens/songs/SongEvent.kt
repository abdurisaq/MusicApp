package com.example.modularapp.screens.songs


import com.example.modularapp.data.entitites.AudioItem
import com.example.modularapp.data.states.SortType

sealed interface SongEvent {
    data object SaveSong: SongEvent
    data object ShowDialog: SongEvent

    data object HideDialog: SongEvent

    data class SetTitle(val title:String):SongEvent
    data class SortSongs(val sortType: SortType): SongEvent
    data class DeleteSong(val song: AudioItem): SongEvent

}