package com.example.modularapp.data.entitites


import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter


@Entity
data class AudioItem(

    @ColumnInfo(name = "content", defaultValue = "content://default")
    val content : Uri= Uri.parse("content://default"),
    @ColumnInfo(name = "mediaItem", defaultValue = "{}")
    val mediaItem: MediaItem = MediaItem.Builder().setUri(Uri.parse("content://default")).build(),
    val name : String,
    val duration: Int,
    val artist: String,
    val timestamp: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true)
    val id:Int? = null

)


class ItemConverter{

    @TypeConverter
    fun uriToString(uri: Uri):String{
            return uri.toString()
    }
    @TypeConverter
    fun stringToUri(uri:String):Uri{
        return Uri.parse(uri)
    }

    @TypeConverter
    fun mediaItemToString(mediaItem: MediaItem):String{

        return mediaItem.localConfiguration?.uri?.toString() ?: ""
    }
    @TypeConverter
    fun stringToMediaItem(mediaItemString:String):MediaItem{
        return MediaItem.Builder().setUri(Uri.parse(mediaItemString)).build()
    }
}