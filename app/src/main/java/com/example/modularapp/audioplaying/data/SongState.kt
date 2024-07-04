package com.example.modularapp.audioplaying.data

data class SongState(
    val songs : List<AudioItemSimplified> = emptyList(),
    val title:String = "",
    val artist:String = "",
    val duration:Int = 0,
    val isAddingSong: Boolean = false,
    val currentSortType:SortType = SortType.TITLE

)
