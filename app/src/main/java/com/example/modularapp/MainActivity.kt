package com.example.modularapp

import android.content.Context
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.modularapp.ui.theme.ModularAppTheme
import com.example.network.KtorClient
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.core.text.HtmlCompat
import coil.compose.AsyncImage
import com.example.modularapp.download.AndroidDownloader
import com.example.modularapp.download.Downloader
import com.example.network.json.YoutubeResponse
import com.example.network.json.YoutubeResponseItem
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLException
import com.yausername.youtubedl_android.YoutubeDLRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val downloader = AndroidDownloader(this)

        setContent {

            var permissionGranted by remember { mutableStateOf(false) }
            var selectedDownloadType by remember { mutableStateOf("") }
            val context = LocalContext.current


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
//                var navController = rememberNavController()
//                NavHost(navController = navController, startDestination = "home") {
//
//                }
                Surface(modifier = Modifier.fillMaxSize()) {


                    Column {


                        //DirectDownload(downloader = downloader, permissionGranted =permissionGranted )
                        DropDown(selectedDownloadType, onDownloadTypeChange = { selectedDownloadType = it })
                        SearchYoutube(downloader = downloader, permissionGranted =permissionGranted,selectedDownloadType )

                    }
                }
            }
            }
        }

}

private fun onButtonClick(context: Context, url: String): String {
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
        request.addOption("-f", "b")
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
//@Composable
//fun DirectDownload(downloader: Downloader,permissionGranted:Boolean){
//
//
//    var videoUrl by remember { mutableStateOf("") } // Replace with actual video URL
//
//    val coroutineScope = rememberCoroutineScope()
//    val context = LocalContext.current
//    Row(modifier = Modifier.padding(16.dp)) {
//        TextField(
//            value = videoUrl,
//            onValueChange = { videoUrl = it },
//            label = { Text("Video URL") },
//            placeholder = {Text("URL")},
//            modifier = Modifier.weight(1f)
//        )
//        Spacer(modifier = Modifier.width(8.dp))
//        Button(
//            onClick = {
//                if (permissionGranted) {
//                    coroutineScope.launch {
//                        Log.d("MainActivity", "Starting download")
//                        withContext(Dispatchers.IO) {
//                            downloader.downloadFile(onButtonClick(context,videoUrl))
//                        }
//                        Toast.makeText(
//                            context,
//                            "Download completed",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                } else {
//                    Toast.makeText(
//                        context,
//                        "Permission not granted",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            },
//            modifier = Modifier.align(Alignment.CenterVertically)
//        ) {
//            Text("Download")
//        }
//    }
//}


@Composable
fun DropDown(selectedDownloadType: String, onDownloadTypeChange: (String) -> Unit) {

    var expanded by remember { mutableStateOf(false) }
    val choices = listOf("Audio","Video")
    //var selectedText by remember { mutableStateOf("") }

    var textFieldSize by remember { mutableStateOf(Size.Zero)}

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown


    Column(Modifier.padding(20.dp)) {
        OutlinedTextField(
            value = selectedDownloadType,
            onValueChange = {  },//selectedText = it
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textFieldSize = coordinates.size.toSize()
                },
            label = {Text("Choose Download Type")},
            readOnly = true,
            trailingIcon = {
                Icon(icon,"contentDescription",
                    Modifier.clickable { expanded = !expanded })
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current){textFieldSize.width.toDp()})
        ) {
            choices.forEach { label ->
                DropdownMenuItem(
                    text = {Text(text = label)},
                    onClick = {
                        onDownloadTypeChange(label)
                    expanded = false
                })
            }
        }
    }

}

@Composable
 fun SearchYoutube(downloader: Downloader, permissionGranted:Boolean, selectedDownloadType: String){
    var videoKeyword by remember { mutableStateOf("")}
    var searches by remember { mutableStateOf<YoutubeResponse?>(null)}
    val ktorClient = KtorClient()
    val apiKey = "AIzaSyDo6ohIdPYPd5YaAnwY-f2Wys7C7tUJjTw"
    val coroutineScope = rememberCoroutineScope()

    Row (modifier = Modifier.padding(16.dp)){
        TextField(
            value = videoKeyword,
            onValueChange = { videoKeyword = it },
            label = { Text("Search For Video") },
            placeholder = {Text("Search")},
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
    ModularAppTheme {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            response?.items?.forEach{ item ->
                DisplayItem(item = item,downloader,permissionGranted,selectedDownloadType)

            }

        }
    }

}

@Composable
fun DisplayItem(item:YoutubeResponseItem, downloader: Downloader, permissionGranted:Boolean, selectedDownloadType: String){
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
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
        .background(color = Color.Cyan)
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
            AsyncImage(
                model = "https://static.vecteezy.com/system/resources/previews/000/574/204/original/vector-sign-of-download-icon.jpg",
                contentDescription = null, // add changing this to a loading image while the video is being processed, so user knows something is happening
                modifier = Modifier
                    .clickable {
                        if (permissionGranted) {
                            coroutineScope.launch {
                                Log.d("MainActivity", "Starting download")
                                val url = "https://www.youtube.com/watch?v=${item.id.videoId}"
                                withContext(Dispatchers.IO) {
                                    downloader.downloadFile2(
                                        onButtonClick(context, url),
                                        item.snippet.title,
                                        selectedDownloadType
                                    )
                                }
                                Log.d("MainActivity", item.snippet.title)
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
