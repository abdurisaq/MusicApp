package com.example.modularapp.download.pages



import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.modularapp.download.logic.Downloader
import com.example.modularapp.download.logic.downloadFile
import com.example.network.KtorClient
import com.example.network.json.YoutubeNoembedResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun DirectDownload(downloader: Downloader, permissionGranted:Boolean, selectedDownloadType: String){


    var videoUrl by remember { mutableStateOf("") } // Replace with actual video URL

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var search by remember { mutableStateOf<YoutubeNoembedResponse?>(null) }
    val ktorClient = KtorClient()
    Row(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = videoUrl,
            onValueChange = { videoUrl = it },
            label = { Text("Video URL") },
            placeholder = {Text("URL")},
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = {
                if (permissionGranted) {
                    coroutineScope.launch {
                        Log.d("MainActivity", "Starting download")
                        withContext(Dispatchers.IO) {
                            search = ktorClient.getYoutubeVideoName(videoUrl)
                            downloader.downloadFile(
                                downloadFile(context,videoUrl,selectedDownloadType),
                                search?.title ?:"No name",selectedDownloadType)

                        }
                        Toast.makeText(
                            context,
                            "Download completed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Permission not granted",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text("Download")
        }
    }
}