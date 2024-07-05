package com.example.modularapp.pages.content.search



import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.modularapp.download.Downloader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
@Composable
fun DirectDownload(downloader: Downloader, permissionGranted:Boolean,selectedDownloadType: String){


    var videoUrl by remember { mutableStateOf("") } // Replace with actual video URL

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
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
                            downloader.downloadFile2(downloadFile(context,videoUrl),"placeholder",selectedDownloadType)
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