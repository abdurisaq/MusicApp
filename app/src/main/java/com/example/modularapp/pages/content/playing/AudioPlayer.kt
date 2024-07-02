package com.example.modularapp.pages.content.playing


import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.modularapp.audioplaying.MainViewModel2
@Composable
fun AudioPlayer(viewModel: MainViewModel2) {
    val audioItems by viewModel.audioItems.collectAsState()
    val selectAudioLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let(viewModel::addAudioUri)
        })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        IconButton(onClick = {
            selectAudioLauncher.launch("audio/mpeg") // Change to audio MIME type
        }) {
            Icon(imageVector = Icons.Default.FileOpen, contentDescription = "Select Audio")
        }
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(audioItems) { item ->
                Text(
                    text = item.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.playAudio(item.content) // Adjusted to play audio
                        }
                        .padding(16.dp)
                )
            }
        }
    }
}