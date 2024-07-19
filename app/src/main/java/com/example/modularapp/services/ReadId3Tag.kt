package com.example.modularapp.services

import android.os.Environment
import android.util.Log
import androidx.core.text.HtmlCompat
import com.mpatric.mp3agic.Mp3File
import com.mpatric.mp3agic.ID3v1
import com.mpatric.mp3agic.ID3v2

fun sanitizeFileName(name: String): String {
    val decoded = HtmlCompat.fromHtml(name, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
    val sanitized = decoded.replace("[^a-zA-Z0-9.-]".toRegex(), "_")
    // Replace multiple consecutive underscores with a single underscore
    return sanitized.replace("_+".toRegex(), "_")
}

fun readID3Tags(fileName: String) {
    Log.d("ID3","entered Function")
    val fullFilePath = "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}/$fileName"
    Log.d("ID3","filepath :$fullFilePath")
    try {
        val mp3File = Mp3File(fullFilePath)

        if (mp3File.hasId3v1Tag()) {
            Log.d("ID3","has an ID3v1")
            val id3v1Tag: ID3v1 = mp3File.id3v1Tag
            Log.d("ID3","Track: " + id3v1Tag.track)
            Log.d("ID3","Artist: " + id3v1Tag.artist)
            Log.d("ID3","Title: " + id3v1Tag.title)
            Log.d("ID3","Album: " + id3v1Tag.album)
            Log.d("ID3","Year: " + id3v1Tag.year)
            Log.d("ID3","Genre: " + id3v1Tag.genreDescription)
            Log.d("ID3","Comment: " + id3v1Tag.comment)
        }

        if (mp3File.hasId3v2Tag()) {
            Log.d("ID3","has an ID3v2")
            val id3v2Tag: ID3v2 = mp3File.id3v2Tag
            Log.d("ID3","Track: " + id3v2Tag.track)
            Log.d("ID3","Artist: " + id3v2Tag.artist)
            Log.d("ID3","Title: " + id3v2Tag.title)
            Log.d("ID3","Album: " + id3v2Tag.album)
            Log.d("ID3","Year: " + id3v2Tag.year)
            Log.d("ID3","Genre: " + id3v2Tag.genreDescription)
            Log.d("ID3","Comment: " + id3v2Tag.comment)
        }

    } catch (e: Exception) {
        Log.d("ID3",e.printStackTrace().toString())
    }
}