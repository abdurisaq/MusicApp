package com.example.modularapp.pages.navBar


import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.modularapp.pages.content.playing.AudioController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun BottomNavigationBar(
    items: List<BottomNavigationItem>,
    selectedItemIndex: Int,
    onItemSelected: (Int) -> Unit,
    navController: NavHostController = rememberNavController(),
//AudioController()
) {
    var navigateToIndex by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(navigateToIndex) {
        navigateToIndex?.let { index ->
            val route = when (index) {
                0 -> "SongScreen"
                1 -> "PlaylistScreen"
                2 -> "SearchScreen"
                3 -> "DirectDownloadScreen"
                4 -> "SettingScreen"
                else -> null
            }
            route?.let {
                withContext(Dispatchers.Main) {
                    navController.navigate(it) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                Log.d("Navigation","Navigation to screen $route")
                }
                navigateToIndex = null
            }
        }
    }

    Column {
        AudioController()

        
        NavigationBar {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = selectedItemIndex == index,
                    onClick = {
                        onItemSelected(index)
                        navigateToIndex = index
                    },
                    label = { Text(text = item.title) },
                    icon = {
                        BadgedBox(badge = {
                            when {
                                item.badgeCount != null -> Badge { Text(text = item.badgeCount.toString()) }
                                item.hasNews -> Badge()
                            }
                        }) {
                            Icon(
                                imageVector = if (index == selectedItemIndex) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        }

                    }

                )
            }
        }
    }


}
