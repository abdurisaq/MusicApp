package com.example.modularapp.download

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
import java.io.File


class AndroidDownloader(
    context: Context
): Downloader {
    private val downloadManager = context.getSystemService(DownloadManager::class.java)
    private fun sanitizeFileName(name: String): String {
        val sanitized = name.replace("[^a-zA-Z0-9.-]".toRegex(), "_")
        // Replace multiple consecutive underscores with a single underscore
        return sanitized.replace("_+".toRegex(), "_")
    }

    private fun decodeHtmlEntities(html: String): String {
        return HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
    }
    private fun isFileAlreadyDownloaded(fileName: String): File {
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(directory, fileName)
        Log.d("MainActivity","found ${file.name}")
        return file
    }

    override fun downloadFile(url: String,title: String,type:String):Long?{

        val decodedTitle = decodeHtmlEntities(title)
        val sanitizedTitle = sanitizeFileName(decodedTitle)
        Log.d("MainActivity","in downloader 3")

        if(type == "Audio"){
            val searchedForFile = isFileAlreadyDownloaded("${sanitizedTitle}.mp3")
            Log.d("MainActivity","${sanitizedTitle}.mp3")
            if(searchedForFile.exists() and (searchedForFile.name == "${sanitizedTitle}.mp3")){
                Log.d("MainActivity","file $title is already downloaded")
                return null
            }
            val request = DownloadManager.Request(url.toUri())
                .setMimeType("audio/mpeg")
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setTitle(sanitizedTitle)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, sanitizedTitle)//"$sanitizedTitle.mpeg"

            return downloadManager.enqueue(request)
        }
        val searchedForFile = isFileAlreadyDownloaded("${sanitizedTitle}.mp4")
         if(searchedForFile.exists() and (searchedForFile.name == "${sanitizedTitle}.mp4")){
             Log.d("MainActivity","file $title is already downloaded")
             return null
         }
        val request = DownloadManager.Request(url.toUri())
            .setMimeType("video/mp4")
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle(sanitizedTitle)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "$sanitizedTitle.mp4")
        return downloadManager.enqueue(request)

    }
}