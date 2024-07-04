package com.example.modularapp.pages.content.playing

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import com.example.modularapp.audioplaying.MainViewModel2
import kotlin.math.roundToInt
import kotlin.math.roundToLong

//, viewModel: MainViewModel2
@Composable
fun AudioController(player: Player){
    //play, pause, skip next, skip previous
    var playing by remember {
        mutableStateOf(false)
        }
    var totalDuration:Long by remember { mutableStateOf(0) }
    var currentPosition: Long by remember {
        mutableStateOf(0)
    }
    DisposableEffect(Unit) {
        val listener = object : Player.Listener {

        }

        player.addListener(listener)

        onDispose {
            player.removeListener(listener)
        }
    }


    if(currentPosition != player.currentPosition){
        currentPosition = player.currentPosition
    }
    Log.d("MusicPlaying",currentPosition.toString())
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)

        ) {

            if(playing){

                totalDuration = player.contentDuration
                if(currentPosition != player.currentPosition){
                    currentPosition = player.currentPosition
                }
                Log.d("MusicPlaying",currentPosition.toString())
                IconButton(onClick = {
                    playing = false
                    player.pause()
                }){
                    Icon(imageVector = Icons.Default.Pause, contentDescription = "Pause")
                }

            }else{
                if(currentPosition != player.currentPosition){
                    currentPosition = player.currentPosition
                }
                IconButton(onClick = {
                    playing = true
                    player.play()
                }){

                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Resume")
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))  
        Slider(value = currentPosition.toFloat(),
            onValueChange = { newValue ->
                currentPosition = newValue.roundToLong()
                player.seekTo(currentPosition) // Seek to the new position in milliseconds
            },
            valueRange = 0f..totalDuration.toFloat(),
            steps = 100,
            modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))

        // Current position and total duration labels
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = millisecondsToMinuteAndSeconds(currentPosition.toInt()))
            Text(text = millisecondsToMinuteAndSeconds(totalDuration.toInt()))

        }
        
    }

    


}






fun millisecondsToMinuteAndSeconds(duration:Int):String{
    val totalSeconds :Int = duration/1000
    val minutes: Int = totalSeconds/ 60
    val leftoverSeconds:Int = totalSeconds - (minutes*60)
    var leftoverSecondsString:String = leftoverSeconds.toString()
    if(leftoverSecondsString.length <= 1){
        leftoverSecondsString = "0${leftoverSecondsString}"
    }
    return "${minutes}:${leftoverSecondsString}"
}