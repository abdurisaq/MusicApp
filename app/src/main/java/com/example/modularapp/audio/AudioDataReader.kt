package com.example.modularapp.audio

import android.app.Application
import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.util.Log

data class MetaData(
    val fileName:String,
    val duration:Int,
    val artist:String
)

interface AudioDataReader {
    fun getMetaDataFromUri (contentUri : Uri): MetaData?
}

class AudioDataReaderImpl(
    private val app: Application
): AudioDataReader {
    private val tag = "metaData"
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
        )?.use {cursor ->
            if(cursor.moveToFirst()){
                val index = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DISPLAY_NAME)
                cursor.moveToFirst()
                cursor.getString(index)
            }else{
                null
            }


        }

        return fileName?.let{filePath ->
            Log.d(tag,fileName)

            MetaData(

                fileName = Uri.parse(filePath).lastPathSegment ?:return null,
                duration = getAudioFileDuration(app.applicationContext, contentUri)?:return null,
                artist = "No one"
            )
        }
    }
    private fun getAudioFileDuration(context: Context, uri: Uri): Int? {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(context, uri)

        val durationString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        return durationString?.toInt()
    }

}