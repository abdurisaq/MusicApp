package com.example.modularapp.audioplaying

import android.app.Application
import android.net.Uri
import android.provider.MediaStore

data class MetaData(
    val fileName:String
)

interface AudioDataReader {
    fun getMetaDataFromUri (contentUri : Uri): MetaData?
}

class AudioDataReaderImpl(
    private val app: Application
): AudioDataReader {
    override fun getMetaDataFromUri(contentUri: Uri): MetaData? {
        if(contentUri.scheme != "content"){
            return null
        }
        val fileName = app.contentResolver.query(
            contentUri,
            arrayOf(MediaStore.Audio.AudioColumns.DISPLAY_NAME),
            null,
            null,
             null
        )
            ?.use {cursor ->
                val index = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DISPLAY_NAME)
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