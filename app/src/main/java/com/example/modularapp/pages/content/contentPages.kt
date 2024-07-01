package com.example.modularapp.pages.content

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
import com.example.modularapp.pages.content.search.DropDown
import com.example.modularapp.pages.content.search.SearchYoutube
import com.example.modularapp.download.AndroidDownloader
import kotlinx.serialization.Serializable

@Composable

fun ContentPages(
    innerPadding: PaddingValues,
    navController: NavHostController,
    permissionGranted:Boolean,
    downloader: AndroidDownloader,
    selectedDownloadType: String,
    onTypeSelected: (String) -> Unit,
){


    Box(modifier = Modifier
        .padding(innerPadding)
        .fillMaxSize()){
        NavHost(navController = navController, startDestination = SongScreen) {
            composable<SongScreen>{
                Text(text = "SongScreen")
            }
            composable<PlaylistScreen>{
                Text(text = "PlaylistScreen")
            }
            composable<SearchScreen>{
                Column {
                    DropDown(selectedDownloadType, onDownloadTypeChange = { onTypeSelected(it) })
                    SearchYoutube(downloader = downloader, permissionGranted =permissionGranted,selectedDownloadType )

                }
            }
            composable<SettingScreen>{
                Text(text = "SettingScreen")
            }

        }
    }

}



@Serializable
object SongScreen

@Serializable
object PlaylistScreen

@Serializable
object SearchScreen

@Serializable
object SettingScreen
