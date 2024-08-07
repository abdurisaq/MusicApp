package com.example.network

import android.util.Log
import com.example.network.json.YoutubeNoembedResponse
import com.example.network.json.YoutubeResponse
import io.ktor.client.HttpClient

import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import com.google.gson.Gson
class KtorClient {
    private val tag = "KtorClient"
    private val client = HttpClient(OkHttp) {
        install(Logging) {
            logger = Logger.SIMPLE
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun searchYoutube(keyword: String, apiKey: String) :YoutubeResponse?{
        val url = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=${keyword}&type=video&maxResults=10&key=${apiKey}"
        var searches : YoutubeResponse?  = null

        try {
            val response: HttpResponse = client.get(url) {
                method = HttpMethod.Get
            }
            if (response.status.value == 200) {
                val rawResponse = response.bodyAsText()
                Log.d(tag, "Raw Response: $rawResponse")
                Log.d(tag, "test")
                searches = Gson().fromJson(rawResponse,YoutubeResponse::class.java)

                Log.d(tag, "test1")

                searches.items.forEach{ item ->
                    Log.d(tag, "Title: ${item.snippet.title}")
                }

            } else {
                Log.e(tag, "Error: Received response code ${response.status.value}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(tag, "Error searching Youtube: ${e.message}")
        }

        return searches
    }
    suspend fun getYoutubeVideoName(videoUrl:String):YoutubeNoembedResponse?{
        val url = "https://noembed.com/embed?url=${videoUrl}"
        var search : YoutubeNoembedResponse? = null
        try {
            val response: HttpResponse = client.get(url){
                method = HttpMethod.Get
            }
            if (response.status.value == 200) {
                val rawResponse = response.bodyAsText()
                Log.d(tag, "Raw Response: $rawResponse")
                Log.d(tag, "test")
                search = Gson().fromJson(rawResponse,YoutubeNoembedResponse::class.java)

                Log.d(tag, "test1")

            } else {
                Log.e(tag, "Error: Received response code ${response.status.value}")
            }
        }catch (e:Exception){
            e.printStackTrace()
            Log.e(tag, "Error searching for youtube name through direct url: ${e.message}")
        }
        return search

    }
//    suspend fun downloadAudio(url: String, context: Context): Uri? {
//        try {
//            val response: HttpResponse = client.get(url)
//            if (response.status.value == 200) {
//                val audioBytes = response.body<ByteArray>()
//                Log.d(TAG, "Downloaded audio bytes")
//
//                val values = ContentValues().apply {
//                    put(MediaStore.Audio.Media.DISPLAY_NAME, "downloaded_audio_${UUID.randomUUID()}.mp3")
//                    put(MediaStore.Audio.Media.MIME_TYPE, "audio/mpeg")
//                    put(MediaStore.Audio.Media.RELATIVE_PATH, Environment.DIRECTORY_MUSIC + "/MyApp")
//                    put(MediaStore.Audio.Media.IS_PENDING, 1) // Mark file as pending during the download
//                }
//
//                val resolver = context.contentResolver
//                val uri: Uri? = resolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values)
//
//                if (uri != null) {
//                    resolver.openOutputStream(uri).use { outputStream ->
//                        if (outputStream != null) {
//                            withContext(Dispatchers.IO) {
//                                outputStream.write(audioBytes)
//                                Log.d(TAG, "Audio written to output stream")
//                            }
//                        }
//                    }
//
//                    // Update the file to mark as not pending
//                    values.clear()
//                    values.put(MediaStore.Audio.Media.IS_PENDING, 0)
//                    resolver.update(uri, values, null, null)
//                    Log.d(TAG, "Audio saved to storage with URI: $uri")
//                    return uri
//                } else {
//                    Log.e(TAG, "Failed to create MediaStore entry")
//                }
//            } else {
//                Log.e(TAG, "Failed to download audio: ${response.status}")
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Log.e(TAG, "Error downloading audio: ${e.message}")
//        }
//        return null
//    }
}
//
//@Serializable
//data class Character(
//    val id: Int,
//    val name: String,
//    val origin: Origin,
//) {
//    @Serializable
//    data class Origin(
//        val name: String
//    )
//}

//@Serializable
//data class YoutubeEntry(
//    val videoID: String,
//    val title:String,
//    val thumbnail:String,
//
//
//)
//{
//
//}