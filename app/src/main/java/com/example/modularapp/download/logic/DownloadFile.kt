package com.example.modularapp.download.logic

import android.content.Context
import android.util.Log
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLException
import com.yausername.youtubedl_android.YoutubeDLRequest
import org.json.JSONObject

fun downloadFile(context: Context, url: String, downloadType: String): String {
    Log.d("MainActivity", "Fetching direct download URL for: $url (Type: $downloadType)")
    var directUrl = ""

    try {
        try {
            YoutubeDL.getInstance().init(context)
            Log.d("MainActivity", "YoutubeDL initialized")
        } catch (e: YoutubeDLException) {
            Log.e("MainActivity", "Failed to initialize YoutubeDL", e)
            return ""
        }

        val request = YoutubeDLRequest(url)
        request.addOption("--skip-download")
        request.addOption("--dump-json")
        val response = YoutubeDL.getInstance().execute(request)
        val output = response.out
        Log.d("MainActivity", "Raw yt-dlp JSON output: $output")

        val json = JSONObject(output)

        when (downloadType) {
            "Audio" -> {
                val formats = json.getJSONArray("formats")
                for (i in 0 until formats.length()) {
                    val format = formats.getJSONObject(i)
                    val asr = format.optInt("asr", -1)
                    if (asr != -1) {
                        directUrl = format.getString("url")
                        Log.d("MainActivity", "Extracted audio URL: $directUrl")
                        break
                    }
                }
                if (directUrl == "") Log.e("MainActivity", "No audio format URL found.")
            }
            "Video" -> {
                val formats = json.getJSONArray("formats")
                for (i in formats.length() - 1 downTo 0) {
                    val format = formats.getJSONObject(i)
                    val vcodec = format.optString("vcodec", "none")
                    val acodec = format.optString("acodec", "none")
                    if (vcodec != "none" && acodec != "none") {
                        directUrl = format.getString("url")
                        Log.d("MainActivity", "Extracted video URL: $directUrl")
                        break
                    }
                }
                if (directUrl == "") Log.e("MainActivity", "No video format URL found.")
            }
            else -> {
                Log.e("MainActivity", "Unknown download type: $downloadType")
                return ""
            }
        }

    } catch (e: Exception) {
        Log.e("MainActivity", "Error fetching direct URL", e)
    }

    return directUrl
}
