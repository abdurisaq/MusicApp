package com.example.modularapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.modularapp.data.entitites.AudioItem
import com.example.modularapp.data.entitites.ItemConverter
import com.example.modularapp.data.entitites.Playlist
import com.example.modularapp.data.entitites.PlaysIn

@Database(
    entities = [AudioItem::class, Playlist::class, PlaysIn::class],
    version = 1,//hardcoded, might need to change later
//    autoMigrations = [
//        AutoMigration(from =1, to = 2)
//    ]
)
@TypeConverters(ItemConverter::class)
abstract class SongDatabases:RoomDatabase() {
    abstract val dao: SongDao
    companion object {
        @Volatile
        private var INSTANCE: SongDatabases? = null

        fun getDatabase(context: Context): SongDatabases {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SongDatabases::class.java,
                    "songs.db"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}