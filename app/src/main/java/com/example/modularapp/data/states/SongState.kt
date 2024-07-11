package com.example.modularapp.data.states

import com.example.modularapp.data.entitites.AudioItem

data class SongState(
    val songs : List<AudioItem> = emptyList(),
    val title:String = "",
    val artist:String = "",
    val duration:Int = 0,
    var isAddingSong: Boolean = false,
    val currentSortType: SortType = SortType.TITLE

)
