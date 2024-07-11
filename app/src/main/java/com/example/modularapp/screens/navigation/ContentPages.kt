package com.example.modularapp.screens.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.modularapp.data.states.SongState
import com.example.modularapp.download.logic.AndroidDownloader
import com.example.modularapp.download.pages.DirectDownload
import com.example.modularapp.download.pages.DropDown
import com.example.modularapp.download.pages.SearchYoutube
import com.example.modularapp.screens.songs.SongScreen
import com.example.modularapp.screens.songs.SongViewModel

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


