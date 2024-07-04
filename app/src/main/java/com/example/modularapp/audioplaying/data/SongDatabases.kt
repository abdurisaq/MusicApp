package com.example.modularapp.audioplaying.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [AudioItemSimplified::class],
    version = 1//hardcoded, might need to change later
)
abstract class SongDatabases:RoomDatabase() {
    abstract val dao: SongDao


}