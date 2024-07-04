package com.example.modularapp.pages.navBar


import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.modularapp.pages.content.DirectDownloadScreen
import com.example.modularapp.pages.content.PlaylistScreen
import com.example.modularapp.pages.content.SearchScreen
import com.example.modularapp.pages.content.SettingScreen
import com.example.modularapp.pages.content.SongScreen
import com.example.modularapp.pages.content.playing.AudioController
import com.example.modularapp.pages.content.songs.SongViewModel2

@Composable
fun BottomNavigationBar(
    items: List<BottomNavigationItem>,
    selectedItemIndex: Int,
    onItemSelected: (Int) -> Unit,
    navController: NavController,
    viewModel: SongViewModel2
) {
    Column {
        AudioController(player = viewModel.player)
        NavigationBar {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = selectedItemIndex == index,
                    onClick = { onItemSelected(index)
                        when (index) {
                            0 -> navController.navigate(SongScreen)
                            1 -> navController.navigate(PlaylistScreen)
                            2 -> navController.navigate(SearchScreen)
                            3 -> navController.navigate(DirectDownloadScreen)
                            4 -> navController.navigate(SettingScreen)
                        } },
                    label = { Text(text = item.title) },
                    icon = {
                        BadgedBox(badge = {
                            when {
                                item.badgeCount != null -> {
                                    Badge { Text(text = item.badgeCount.toString()) }
                                }
                                item.hasNews -> {
                                    Badge()
                                }
                            }
                        }) {
                            Icon(
                                imageVector = if (index == selectedItemIndex) {
                                    item.selectedIcon
                                } else {
                                    item.unselectedIcon
                                },
                                contentDescription = item.title
                            )
                        }
                    }
                )
            }
        }
    }


}
