package com.example.modularapp.pages.content.songs


//private val songs: Flow<List<AudioItem>> = combine(
//    _sortType,
//    dao.getSongsOrderedByTitle(), // Default flow
//    dao.getSongsOrderedByTimeAdded()
//) { sortType, songsByTitle, songsByTimeAdded ->
//    when (sortType) {
//        SortType.TITLE -> songsByTitle
//        SortType.TIME -> songsByTimeAdded
//    }
//}
//fun updateSortType(sortType: SortType) {
//    _sortType.value = sortType
//} try later to remove experimental thing
//class SongViewModel(
//    private val dao: SongDao
//):ViewModel() {
//
//    private val _state = MutableStateFlow(SongState())
//    private val _sortType = MutableStateFlow(SortType.TITLE)
//    @OptIn(ExperimentalCoroutinesApi::class)
//    private val _songs = _sortType.flatMapLatest{ sortType ->
//        when(sortType){
//            SortType.TITLE -> dao.getSongsOrderedByTitle()
//            SortType.TIME -> dao.getSongsOrderedByTimeAdded()
//        }
//    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
//
//    val state = combine(_state,_sortType, _songs){ state,sortType,songs ->
//        state.copy(
//            songs = songs,
//            currentSortType = sortType,
//
//        )
//    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),SongState())
//    fun onEvent(event: SongEvent){
//        when(event){
//            is SongEvent.deleteSong -> {
//                viewModelScope.launch {
//                    dao.deleteSong(event.song)
//                }
//
//            }
//            SongEvent.hideDialog -> {
//                _state.update { it.copy(
//                    isAddingSong = false
//                ) }
//            }
//            SongEvent.saveSong -> {
//                val title = state.value.title
//                val artist = state.value.artist
//                val duration = state.value.duration
//
//                if(title.isBlank() || artist.isBlank() || duration == 0){
//                    return
//                }
//
//                val song = AudioItemSimplified(
//                    name = title,
//                    artist = artist,
//                    duration = duration,
//                )
//                viewModelScope.launch {
//                    //dao.upsertSong(song)
//                }
//                _state.update { it.copy(
//                    isAddingSong = false,
//                    title = "",
//                    artist = "",
//                    duration = 0,
//                ) }
//            }
//            is SongEvent.setArtist -> {
//                _state.update { it.copy(
//                    artist = event.artist
//                ) }
//            }
//            is SongEvent.setDuration -> {
//                _state.update { it.copy(
//                    duration = event.duration
//                ) }
//            }
//            is SongEvent.setTitle -> {
//                _state.update { it.copy(
//                    title = event.title
//                ) }
//            }
//            SongEvent.showDialog -> {
//                _state.update { it.copy(
//                    isAddingSong = true
//                ) }
//            }
//            is SongEvent.sortSongs -> {
//                _sortType.value = event.sortType
//            }
//        }
//    }
//private val songs
//}