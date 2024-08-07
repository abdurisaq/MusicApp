package com.example.modularapp.unused.videoplaying

import android.app.Application
import android.net.Uri
import android.provider.MediaStore

data class MetaData(
    val fileName:String
)

interface VideoMetaDataReader {
    fun getMetaDataFromUri (contentUri : Uri): MetaData?
}

class VideoMetaDataReaderImpl(
    private val app: Application
): VideoMetaDataReader {
    override fun getMetaDataFromUri(contentUri: Uri): MetaData? {
        if(contentUri.scheme != "content"){
            return null
        }
        val fileName = app.contentResolver.query(
            contentUri,
            arrayOf(MediaStore.Video.VideoColumns.DISPLAY_NAME),
            null,
            null,
             null
        )
            ?.use {cursor ->
                val index = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DISPLAY_NAME)
                cursor.moveToFirst()
                cursor.getString(index)

            }
        return fileName?.let{filePath ->
            MetaData(
                fileName = Uri.parse(filePath).lastPathSegment ?:return null
            )
        }
    }

}