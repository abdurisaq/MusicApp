package com.example.modularapp.audioplaying.data

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameTable
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec

@Database(
    entities = [AudioItem::class,Playlist::class,PlaysIn::class],
    version = 1,//hardcoded, might need to change later
//    autoMigrations = [
//        AutoMigration(from =2, to = 3)
//    ]
)
@TypeConverters(ItemConverter::class)
abstract class SongDatabases:RoomDatabase() {
    abstract val dao: SongDao


}

@RenameTable(fromTableName = "AudioItemSimplified", toTableName = "AudioItem")
class MyAutoMigration : AutoMigrationSpec