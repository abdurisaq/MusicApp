package com.example.modularapp.pages.content.search

import android.content.Context
import android.util.Log
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLException
import com.yausername.youtubedl_android.YoutubeDLRequest

fun downloadFile(context: Context, url: String): String {
    Log.d("MainActivity", "Successfully entered function")
    var returnUrl = ""
    try {
        // Initialize YoutubeDL
        try {
            YoutubeDL.getInstance().init(context)
            Log.d("MainActivity", "Successfully initialized YoutubeDL")
        } catch (e: YoutubeDLException) {
            Log.e("MainActivity", "Failed to initialize YoutubeDL", e)
            return ""  // Return early if initialization fails
        }
        YoutubeDL.getInstance().updateYoutubeDL(context)
        val request = YoutubeDLRequest(url)
        request.addOption("-f", "best")
        val streamInfo = YoutubeDL.getInstance().getInfo(request)
        println(streamInfo.url)

        Log.d("MainActivity", "StreamInfo is not null")
        if (streamInfo.url != null) {
            // Direct URL fetched successfully
            println(streamInfo.url)
            Log.d("MainActivity", "fileSize :: ${streamInfo.fileSize}")
            val directUrl = streamInfo.url
            Log.d("MainActivity", "Direct URL: $directUrl")
            if (directUrl != null) {
                returnUrl = directUrl
            }
        } else {
            // URL is null
            Log.e("MainActivity", "URL is null")
        }
    } catch (e: Exception) {
        // Handle any other exceptions
        Log.e("MainActivity", "Error getting direct URL", e)
    }
    return returnUrl

}