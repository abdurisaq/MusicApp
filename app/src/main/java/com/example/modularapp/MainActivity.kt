package com.example.modularapp

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.modularapp.ui.theme.ModularAppTheme
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.compose.rememberNavController
import com.example.modularapp.audioplaying.AudioPlayerApp
import com.example.modularapp.audioplaying.MainViewModel2
import com.example.modularapp.download.AndroidDownloader
import com.example.modularapp.pages.content.ContentPages
import com.example.modularapp.pages.navBar.BottomNavigationBar
import com.example.modularapp.pages.navBar.items
import com.example.modularapp.videoplaying.MainViewModel
import com.example.modularapp.videoplaying.VideoPlayerApp
import com.example.modularapp.videoplaying.ViewModelFactory
import com.example.modularapp.audioplaying.ViewModelFactory2
import com.example.modularapp.audioplaying.services.AudioService

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val downloader = AndroidDownloader(this)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }

        setContent {

            var permissionGranted by remember { mutableStateOf(false) }
            var selectedDownloadType by remember { mutableStateOf("Video") }
            val context = LocalContext.current
            var selectedNavItemIndex by remember { mutableStateOf(0) }
            val navController = rememberNavController()

            val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                permissionGranted = granted
                if (!granted) {
                    Toast.makeText(context, "Permission not granted", Toast.LENGTH_SHORT).show()
                }
            }
            val viewModel: MainViewModel by viewModels {
                ViewModelFactory(
                    this,
                    player = VideoPlayerApp.appModule.videoPlayer,
                    metaDataReader =VideoPlayerApp.appModule.metaDataReader
                )
            }
            val viewModel2: MainViewModel2 by viewModels {
                ViewModelFactory2(
                    this,
                    player = AudioPlayerApp.appModule.audioPlayer ,
                    metaDataReader =AudioPlayerApp.appModule.metaDataReader
                )
            }
            LaunchedEffect(Unit) {
                // Request the required permissions
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_VIDEO)
                } else {
                    permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }

            }
            //val player = ExoPlayer.Builder(context).build()

            ModularAppTheme {

//                Column(
//                    modifier = Modifier.fillMaxSize(),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    Button(onClick = {
//                        Intent(applicationContext,AudioService::class.java).also {
//                            it.action =AudioService.Actions.START.toString()
//                            startService(it)
//                        }
//                    }) {
//                        Text(text = "start service")
//                    }
//
//                    Button(onClick = {
//                        Intent(applicationContext,AudioService::class.java).also {
//                            it.action = AudioService.Actions.STOP.toString()
//                            startService(it)
//                        }
//                    }) {
//                        Text(text = "stop service")
//                    }
//                }
                Scaffold(

                    bottomBar = {
                        BottomNavigationBar(items,selectedNavItemIndex,onItemSelected = { item -> selectedNavItemIndex = item},navController)
                    },
                    content = { innerPadding ->
                        ContentPages(innerPadding,navController,permissionGranted,downloader,selectedDownloadType,onTypeSelected ={ type -> selectedDownloadType = type},viewModel2)
                    }
                )
            }
        }
    }
}

