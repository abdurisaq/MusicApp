package com.example.modularapp

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.modularapp.services.SongViewModelFactory
import com.example.modularapp.data.database.SongDatabases
import com.example.modularapp.download.logic.AndroidDownloader
import com.example.modularapp.screens.navigation.ContentPages

import com.example.modularapp.screens.songs.SongViewModel
import com.example.modularapp.screens.navigation.BottomNavigationBar
import com.example.modularapp.screens.navigation.FloatingPlaylistButton
import com.example.modularapp.screens.navigation.FloatingSongButton
import com.example.modularapp.screens.navigation.items
import com.example.modularapp.screens.playlists.PlaylistViewModel
import com.example.modularapp.services.PlaylistViewModelFactory
import com.example.modularapp.ui.theme.ModularAppTheme

class MainActivity : ComponentActivity() {

    @SuppressLint("AutoboxingStateCreation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AudioPlayerApp.initializeAppModule(this.application)
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


            val db = SongDatabases.getDatabase(context)
            val songViewModel: SongViewModel by viewModels {
                SongViewModelFactory(
                    db.songDao,
                    this,
                    player = AudioPlayerApp.appModule.audioPlayer ,
                    metaDataReader = AudioPlayerApp.appModule.metaDataReader)
            }
            val playlistViewModel : PlaylistViewModel by viewModels {
                PlaylistViewModelFactory(
                    db.playlistDao
                )
            }

            val songState by songViewModel.state.collectAsState()
            val playlistState by playlistViewModel.state.collectAsState()
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
                            FloatingSongButton(songViewModel::onEvent)
                        }
                        if(currentDestination == "PlaylistScreen"){
                           FloatingPlaylistButton(playlistViewModel::onEvent)
                        }
                    },
                    //modifier = Modifier.padding(16.dp),
                    content = { innerPadding ->
                        ContentPages(innerPadding,navController,
                            permissionGranted,downloader,
                            selectedDownloadType,onTypeSelected ={ type -> selectedDownloadType = type},
                            songState , songViewModel,
                            playlistState,playlistViewModel)
                    }
                )
            }
        }
    }

}

