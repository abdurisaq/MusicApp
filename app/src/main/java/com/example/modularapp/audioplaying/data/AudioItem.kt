package com.example.modularapp.audioplaying.data


import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
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

@Entity
data class AudioItemSimplified(

    val name : String,
    val duration: Int,
    val artist: String,

    val timestamp: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true)
    val id:Int? = null

)

@Entity
data class Playlist(
    val name: String,
    val length:Int,
    @PrimaryKey(autoGenerate = true)
    val playlistId:Int? = null
)

@Entity(
    primaryKeys = ["songId", "playlistId"],
    foreignKeys = [
        ForeignKey(entity = AudioItem::class,
            parentColumns = ["id"],
            childColumns = ["songId"],
            onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Playlist::class,
            parentColumns = ["playlistId"],
            childColumns = ["playlistId"],
            onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index(value = ["playlistId"]), Index(value = ["songId"])]
)
data class PlaysIn(
    val songId: Int,
    val playlistId: Int,
    val playlistPosition: Int
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