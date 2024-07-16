package com.example.modularapp.screens.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


@Composable

fun TopBar(
    navController : NavHostController = rememberNavController()
){
    IconButton(onClick = {navController.navigate("PlaylistScreen") }) {
        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "backArrow")
    }
}