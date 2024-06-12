package com.example.modularapp

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.modularapp.ui.theme.ModularAppTheme
import com.example.network.Character
import com.example.network.KtorClient
import com.example.network.TestFile
import kotlinx.coroutines.delay
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.modularapp.ui.theme.ModularAppTheme
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLException
import com.yausername.youtubedl_android.YoutubeDLRequest
import kotlinx.coroutines.launch
class MainActivity : ComponentActivity() {
    private val ktorClient = KtorClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val downloader = AndroidDownloader(this)

        setContent {
            var videoUri by remember { mutableStateOf<Uri?>(null) }
            var permissionGranted by remember { mutableStateOf(false) }



            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()

            val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                permissionGranted = granted
                if (!granted) {
                    Toast.makeText(context, "Permission not granted", Toast.LENGTH_SHORT).show()
                }
            }

            LaunchedEffect(Unit) {
                // Request the required permissions
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_VIDEO)
                } else {
                    permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }

            ModularAppTheme {
                Column {
                    Greeting("Android")

                    var videoUrl by remember { mutableStateOf("https://www.example.com/video.mp4") } // Replace with actual video URL
                    Row(modifier = Modifier.padding(16.dp)) {
                        TextField(
                            value = videoUrl,
                            onValueChange = { videoUrl = it },
                            label = { Text("Video URL") },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (permissionGranted) {
                                    coroutineScope.launch {
                                        Log.d("MainActivity", "Starting download")
                                        val uri = downloader.downloadFile(onButtonClick(videoUrl))
                                        if (uri != null) {

                                            Toast.makeText(context, "Download completed", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, "Download failed", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                } else {
                                    Toast.makeText(context, "Permission not granted", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Text("Download")
                        }
                    }
                    videoUri?.let {
                        Button(onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, it).apply {
                                setDataAndType(it, "video/mp4")
                                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                            }
                            context.startActivity(intent)
                        }) {
                            Text("Play Video")
                        }
                    }

                }
            }
            }
        }
    private fun onButtonClick(url: String): String {
        var returnUrl:String = ""
        lifecycleScope.launch {
            try {
                // Initialize YoutubeDL
                try {
                    YoutubeDL.getInstance().init(applicationContext)
                    Log.d("MainActivity", "Successfully initialized YoutubeDL")
                } catch (e: YoutubeDLException) {
                    Log.e("MainActivity", "Failed to initialize YoutubeDL", e)
                    return@launch  // Return early if initialization fails
                }


                val request = YoutubeDLRequest(url)
                request.addOption("-f", "best")
                val streamInfo = YoutubeDL.getInstance().getInfo(request)
                println(streamInfo.url)

                if (streamInfo != null) {
                    Log.d("MainActivity", "StreamInfo is not null")
                    if (streamInfo.url != null) {
                        // Direct URL fetched successfully
                        println(streamInfo.url)
                        Log.d("MainActivity", "filesize :: ${streamInfo.fileSize}")
                        val directUrl = streamInfo.url
                        Log.d("MainActivity", "Direct URL: $directUrl")
                        if (directUrl != null) {
                            returnUrl = directUrl
                        }

                    } else {
                        // URL is null
                        Log.e("MainActivity", "URL is null")
                    }
                } else {
                    // streamInfo is null
                    Log.e("MainActivity", "streamInfo is null")
                }
            } catch (e: Exception) {
                // Handle any other exceptions
                Log.e("MainActivity", "Error getting direct URL", e)
            }
        }
        return returnUrl

    }

}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ModularAppTheme {
        Greeting("Android")
    }
}

