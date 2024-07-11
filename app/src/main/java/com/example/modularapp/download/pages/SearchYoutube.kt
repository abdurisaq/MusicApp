package com.example.modularapp.download.pages

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import coil.compose.AsyncImage
import com.example.modularapp.download.logic.Downloader
import com.example.network.KtorClient
import com.example.network.json.YoutubeResponse
import com.example.network.json.YoutubeResponseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.modularapp.BuildConfig
import com.example.modularapp.download.logic.downloadFile

@Composable
fun SearchYoutube(downloader: Downloader, permissionGranted:Boolean, selectedDownloadType: String){
    var videoKeyword by remember { mutableStateOf("") }
    var searches by remember { mutableStateOf<YoutubeResponse?>(null) }
    val ktorClient = KtorClient()
    val apiKey =BuildConfig.API_KEY
    val coroutineScope = rememberCoroutineScope()

    Row (modifier = Modifier.padding(16.dp)){
        TextField(
            value = videoKeyword,
            onValueChange = { videoKeyword = it },
            label = { Text("Search For Video") },
            placeholder = { Text("Search") },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = {
                coroutineScope.launch {
                    withContext(Dispatchers.IO){
                        searches = ktorClient.searchYoutube(videoKeyword,apiKey)
                    }
                }
                //Log.d("KtorClient",searches.toString())
            }
        ){
            Text("Search")
        }
    }

    ShowResults(searches,downloader,permissionGranted,selectedDownloadType)
}
@Composable
fun ShowResults(response: YoutubeResponse?, downloader: Downloader, permissionGranted:Boolean, selectedDownloadType: String){

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            response?.items?.forEach{ item ->
                DisplayItem(item = item,downloader,permissionGranted,selectedDownloadType)

            }


    }

}

@Composable
fun DisplayItem(item: YoutubeResponseItem, downloader: Downloader, permissionGranted:Boolean, selectedDownloadType: String){
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val tag = "MainActivity"
    var downloading by remember {
        mutableStateOf(false)
    }
    fun sanitizeFileName(name: String): String {
        var sanitized = name.replace("[^a-zA-Z0-9.-]".toRegex(), "_")
        // Replace multiple consecutive underscores with a single underscore
        sanitized = sanitized.replace("_+".toRegex(), "_")
        return sanitized.replace("_"," ")
    }

    fun decodeHtmlEntities(html: String): String {
        return HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
    }
    val decodedTitle = decodeHtmlEntities(item.snippet.title)
    val sanitizedTitle = sanitizeFileName(decodedTitle)
    Surface (modifier =
    Modifier
        .background(color = Color.Gray)
        .padding(10.dp)

    ){
        Row {
            AsyncImage(
                model = item.snippet.thumbnails.medium.url,
                contentDescription = null,

                )
            Text(text = sanitizedTitle
                , modifier = Modifier
                    .width(200.dp)
                    .height(50.dp)
            )
            if (downloading){
                AsyncImage(
                    model = "https://static.vecteezy.com/system/resources/thumbnails/008/202/375/original/loading-circle-icon-loading-gif-loading-screen-gif-loading-spinner-gif-loading-animation-loading-free-video.jpg",
                    contentDescription = null,

                    )
            }else{

                AsyncImage(
                    model = "https://static.vecteezy.com/system/resources/previews/000/574/204/original/vector-sign-of-download-icon.jpg",
                    contentDescription = null, // add changing this to a loading image while the video is being processed, so user knows something is happening
                    modifier = Modifier
                        .clickable {
                            if (permissionGranted) {
                                coroutineScope.launch {
                                    Log.d("MainActivity", "Starting download")
                                    val url = "https://www.youtube.com/watch?v=${item.id.videoId}"
                                    Log.d(tag, "started download for${item.snippet.title}")
                                    withContext(Dispatchers.IO) {
                                        downloading = true

                                        downloader.downloadFile(
                                            downloadFile(context, url),
                                            item.snippet.title,
                                            selectedDownloadType
                                        )
                                        downloading = false
                                    }
                                    Log.d(tag, "finished download for${item.snippet.title}")
                                    Toast
                                        .makeText(
                                            context,
                                            "Download completed",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                }
                            } else {
                                Toast
                                    .makeText(
                                        context,
                                        "Permission not granted",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            }

                        }
                        .padding(8.dp)
                )
            }
        }

    }

}