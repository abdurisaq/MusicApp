package com.example.modularapp

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri


class AndroidDownloader(
    private val context: Context
): Downloader {
    private val downloadManager = context.getSystemService(DownloadManager::class.java)
    override fun downloadFile(url: String): Long {
        val request = DownloadManager.Request(url.toUri())
            .setMimeType("video/mpeg")
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle("video.mpeg")
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "video.mpeg")
        return downloadManager.enqueue(request)
    }
     override fun downloadFile2(url: String,title: String,type:String):Long{
        if(type == "audio"){
            val request = DownloadManager.Request(url.toUri())
                .setMimeType("audio/mpeg")
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setTitle("${title}.mpeg")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "${title}.mpeg")
            return downloadManager.enqueue(request)
        }
         val request = DownloadManager.Request(url.toUri())
             .setMimeType("video/mpeg")
             .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
             .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
             .setTitle("${title}.mpeg")
             .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "${title}.mpeg")
         return downloadManager.enqueue(request)

    }
}