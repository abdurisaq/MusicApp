package com.example.modularapp

import android.os.Build
import android.os.Bundle
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
import androidx.compose.material3.Scaffold
import androidx.compose.ui.platform.LocalContext
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.compose.rememberNavController
import com.example.modularapp.download.AndroidDownloader
import com.example.modularapp.pages.content.ContentPages
import com.example.modularapp.pages.navBar.BottomNavigationBar
import com.example.modularapp.pages.navBar.items
import com.example.modularapp.videoplaying.MainViewModel
import com.example.modularapp.videoplaying.VideoPlayerApp
import com.example.modularapp.videoplaying.ViewModelFactory
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val downloader = AndroidDownloader(this)

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


                Scaffold(

                    bottomBar = {
                        BottomNavigationBar(items,selectedNavItemIndex,onItemSelected = { item -> selectedNavItemIndex = item},navController)
                    },
                    content = { innerPadding ->
                        ContentPages(innerPadding,navController,permissionGranted,downloader,selectedDownloadType,onTypeSelected ={ type -> selectedDownloadType = type})
                    }
                )
            }
        }
    }
}

