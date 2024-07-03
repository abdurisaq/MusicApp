package com.example.modularapp.pages.content.playing

import android.util.Log

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import com.example.modularapp.audioplaying.MainViewModel2
//, viewModel: MainViewModel2
@Composable
fun AudioController(player: Player){
    //play, pause, skip next, skip previous
    var paused by remember {
        mutableStateOf(true)
        }

    Row(verticalAlignment = Alignment.Bottom,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)

    ) {
        if(paused){
            IconButton(onClick = {
                paused = false
                player.pause()
            }){
                Icon(imageVector = Icons.Default.Pause, contentDescription = "Pause")
            }

        }else{
            IconButton(onClick = {
                paused = true
                player.play()
            }){

                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Resume")
            }
        }
    }


}






fun millisecondsToMinuteAndSeconds(duration:Int):String{
    val totalSeconds :Int = duration/1000
    val minutes: Int = totalSeconds/ 60
    val leftoverSeconds:Int = totalSeconds - (minutes*60)

    return "${minutes}:${leftoverSeconds}"
}