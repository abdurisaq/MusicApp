package com.example.modularapp

import android.Manifest
import android.annotation.SuppressLint
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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.modularapp.audioplaying.AudioMainViewModel
import com.example.modularapp.audioplaying.AudioPlayerApp
import com.example.modularapp.audioplaying.AudioViewModelFactory
import com.example.modularapp.audioplaying.SongViewModelFactory
import com.example.modularapp.download.AndroidDownloader
import com.example.modularapp.pages.content.ContentPages
import com.example.modularapp.pages.navBar.BottomNavigationBar
import com.example.modularapp.pages.navBar.items
import com.example.modularapp.audioplaying.data.SongDatabases
import com.example.modularapp.pages.content.songs.FloatingButton
import com.example.modularapp.pages.content.songs.SongViewModel

class MainActivity : ComponentActivity() {
//    private val db by lazy {
//        Room.databaseBuilder(
//            applicationContext,
//            SongDatabases::class.java,
//            "songs.db"
//        ).build()
//    }


    @SuppressLint("AutoboxingStateCreation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SongDatabases.getDatabase(this)
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
            val audioViewModel: AudioMainViewModel by viewModels {
                AudioViewModelFactory(
                    this,
                    player = AudioPlayerApp.appModule.audioPlayer ,
                    metaDataReader =AudioPlayerApp.appModule.metaDataReader
                )
            }

            val db = SongDatabases.getDatabase(context)
            val songViewModel: SongViewModel by viewModels {
                SongViewModelFactory(
                    db.dao,
                    this,
                    player = AudioPlayerApp.appModule.audioPlayer ,
                    metaDataReader =AudioPlayerApp.appModule.metaDataReader)
            }
            val state by songViewModel.state.collectAsState()
            val currentBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = currentBackStackEntry?.destination?.route
            Log.d("Navigation","current destination = $currentDestination")

            LaunchedEffect(Unit) {
                // Request the required permissions
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_VIDEO)
                } else {
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }

            }


            ModularAppTheme {

                Scaffold(

                    bottomBar = {
                        BottomNavigationBar(items,selectedNavItemIndex,onItemSelected = { item -> selectedNavItemIndex = item},navController)
                    },
                    floatingActionButton = {
                        if (currentDestination == "SongScreen") {
                            FloatingButton(songViewModel::onEvent)
                        }
                    },
                    //modifier = Modifier.padding(16.dp),
                    content = { innerPadding ->

                        ContentPages(innerPadding,navController,permissionGranted,downloader,selectedDownloadType,onTypeSelected ={ type -> selectedDownloadType = type},audioViewModel,state , songViewModel)
                    }
                )
            }
        }
    }
}

