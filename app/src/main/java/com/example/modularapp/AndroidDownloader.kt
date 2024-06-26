package com.example.modularapp

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat


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
         val decodedTitle = decodeHtmlEntities(title)
         val sanitizedTitle = sanitizeFileName(decodedTitle)
        if(type == "Audio"){
            val request = DownloadManager.Request(url.toUri())
                .setMimeType("audio/mpeg")
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setTitle(sanitizedTitle)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "$sanitizedTitle.mpeg")
            return downloadManager.enqueue(request)
        }
         val request = DownloadManager.Request(url.toUri())
             .setMimeType("video/mpeg")
             .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
             .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
             .setTitle(sanitizedTitle)
             .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "$sanitizedTitle.mpeg")
         return downloadManager.enqueue(request)

    }
}