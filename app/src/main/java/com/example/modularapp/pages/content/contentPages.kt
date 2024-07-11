package com.example.modularapp.pages.content

import android.content.Intent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.modularapp.audioplaying.AudioMainViewModel
import com.example.modularapp.audioplaying.data.SongState
import com.example.modularapp.audioplaying.services.AudioService
import com.example.modularapp.pages.content.search.DropDown
import com.example.modularapp.pages.content.search.SearchYoutube
import com.example.modularapp.download.AndroidDownloader
import com.example.modularapp.pages.content.playing.AudioPlayer
import com.example.modularapp.pages.content.search.DirectDownload
import com.example.modularapp.pages.content.songs.SongScreen
import com.example.modularapp.pages.content.songs.SongViewModel
import kotlinx.serialization.Serializable

@Composable

fun ContentPages(
    innerPadding: PaddingValues,
    navController: NavHostController = rememberNavController(),
    permissionGranted:Boolean,
    downloader: AndroidDownloader,
    selectedDownloadType: String,
    onTypeSelected: (String) -> Unit,
    state: SongState,
    songViewModel2: SongViewModel
){
    val context = LocalContext.current

    Box(modifier = Modifier
        .padding(innerPadding)//enterTransition = { EnterTransition.None }, exitTransition = { ExitTransition.None }
        .fillMaxSize()){
        NavHost(navController = navController, startDestination = "SongScreen"
            ) {
            composable(route = "SongScreen"){
//                Text(text = "SongScreen")
                SongScreen(state,songViewModel2::onEvent,innerPadding,songViewModel2)
            }
            composable(route = "PlaylistScreen"){
                Text(text = "PlaylistScreen")
            }
            composable(route = "SearchScreen"){
//                Text(text = "SearchScreen")
                Column {

                    DropDown(selectedDownloadType, onDownloadTypeChange = { onTypeSelected(it) })
                    SearchYoutube(downloader = downloader, permissionGranted =permissionGranted,selectedDownloadType )

                }
            }
            composable(route = "DirectDownloadScreen"){
//                Text(text = "DSearchScreen")
                Column {
                    DropDown(selectedDownloadType, onDownloadTypeChange = { onTypeSelected(it) })
                    DirectDownload(downloader,permissionGranted,selectedDownloadType)

                }
            }
            composable(route = "SettingScreen"){
                Text(text = "SettingsScreen")
            }



        }
    }

}


