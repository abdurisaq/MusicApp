package com.example.modularapp.audioplaying.data

data class SongState(
    val songs : List<AudioItem> = emptyList(),
    val title:String = "",
    val artist:String = "",
    val duration:Int = 0,
    var isAddingSong: Boolean = false,
    val currentSortType:SortType = SortType.TITLE

)
