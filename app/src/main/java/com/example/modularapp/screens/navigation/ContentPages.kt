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
import com.example.modularapp.data.states.PlaylistState
import com.example.modularapp.data.states.SongState
import com.example.modularapp.download.logic.AndroidDownloader
import com.example.modularapp.download.pages.DirectDownload
import com.example.modularapp.download.pages.DropDown
import com.example.modularapp.download.pages.SearchYoutube
import com.example.modularapp.screens.playlists.PlaylistScreen
import com.example.modularapp.screens.playlists.PlaylistViewModel
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
    songState: SongState,
    songViewModel: SongViewModel,
    playlistState: PlaylistState,
    playlistViewModel: PlaylistViewModel,
){

    Box(modifier = Modifier
        .padding(innerPadding)//enterTransition = { EnterTransition.None }, exitTransition = { ExitTransition.None }
        .fillMaxSize()){
        NavHost(navController = navController, startDestination = "SongScreen"
            ) {
            composable(route = "SongScreen"){
//                Text(text = "SongScreen")
                SongScreen(songState,songViewModel::onEvent,innerPadding,songViewModel)
            }
            composable(route = "PlaylistScreen"){
                PlaylistScreen(state = playlistState, onEvent =playlistViewModel::onEvent , padding = innerPadding, viewModel =playlistViewModel)
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


