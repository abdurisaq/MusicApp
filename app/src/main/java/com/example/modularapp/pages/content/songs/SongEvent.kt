package com.example.modularapp.pages.content.songs


import com.example.modularapp.audioplaying.data.AudioItemSimplified
import com.example.modularapp.audioplaying.data.SortType

sealed interface SongEvent {
    data object saveSong:SongEvent
    data object showDialog:SongEvent

    data object hideDialog:SongEvent

    data class setTitle(val title:String):SongEvent
    data class setArtist(val artist: String):SongEvent
    data class setDuration(val duration:Int):SongEvent

    data class sortSongs(val sortType: SortType):SongEvent
    data class deleteSong(val song: AudioItemSimplified):SongEvent

}