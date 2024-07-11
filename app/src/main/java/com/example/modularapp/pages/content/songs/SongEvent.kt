package com.example.modularapp.pages.content.songs


import com.example.modularapp.audioplaying.data.AudioItem
import com.example.modularapp.audioplaying.data.SortType

sealed interface SongEvent {
    data object SaveSong:SongEvent
    data object ShowDialog:SongEvent

    data object HideDialog:SongEvent


    data class SortSongs(val sortType: SortType):SongEvent
    data class DeleteSong(val song: AudioItem):SongEvent

}