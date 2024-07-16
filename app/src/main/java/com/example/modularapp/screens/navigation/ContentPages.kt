package com.example.modularapp.screens.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.modularapp.AudioPlayerApp
import com.example.modularapp.SharedViewModel
import com.example.modularapp.data.database.SongDatabases
import com.example.modularapp.data.states.PlaylistState
import com.example.modularapp.data.states.SongState
import com.example.modularapp.download.logic.AndroidDownloader
import com.example.modularapp.download.pages.DirectDownload
import com.example.modularapp.download.pages.DropDown
import com.example.modularapp.download.pages.SearchYoutube
import com.example.modularapp.screens.playlists.PlaylistScreen
import com.example.modularapp.screens.playlists.PlaylistViewModel
import com.example.modularapp.screens.playlists.playlist.SelectedPlaylistScreen
import com.example.modularapp.screens.songs.SongScreen
import com.example.modularapp.screens.songs.SongViewModel
import com.example.modularapp.services.SongViewModelFactory

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
    sharedViewModel:SharedViewModel
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
                PlaylistScreen(state = playlistState, onEvent =playlistViewModel::onEvent , padding = innerPadding, viewModel =playlistViewModel,
                    navController
                    )
            }
            composable(route="SelectedPlaylistScreen/{playlistId}", arguments = listOf(navArgument("playlistId"){type = NavType.IntType}
            )){backStackEntry ->
                val playlistId:Int = backStackEntry.arguments?.getInt("playlistId") ?: -1
                //Log.d("SongViewModel","between navigating, going to navigate to playlist with id $playlistId")
                val context = LocalContext.current
                val factory = SongViewModelFactory(
                    SongDatabases.getDatabase(context).songDao,
                    backStackEntry,
                    metaDataReader = AudioPlayerApp.appModule.metaDataReader,
                    playlistId = playlistId)
                val viewModel:SongViewModel = viewModel(factory = factory)
                sharedViewModel.setCurrentViewModel(viewModel)
                SelectedPlaylistScreen(padding = innerPadding, viewModel,songViewModel)


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


