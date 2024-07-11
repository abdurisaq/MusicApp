package com.example.modularapp.pages.content.playing

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.Player
import com.example.modularapp.audioplaying.AudioPlayerApp
import com.example.modularapp.audioplaying.services.AudioService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlin.math.roundToLong
import kotlinx.coroutines.launch

@SuppressLint("AutoboxingStateCreation")
@Composable
fun AudioController(){
    //play, pause, skip next, skip previous
    val player = AudioPlayerApp.appModule.audioPlayer
    var playing by remember {
        mutableStateOf(player.isPlaying)
    }
    var totalDuration:Long by remember { mutableStateOf(if(player.isPlaying) {player.duration}else{0}) }
    var currentPosition: Long by remember {
        mutableStateOf(if(player.playbackState != Player.STATE_IDLE||player.playbackState != Player.STATE_ENDED||player.currentPosition!=0L) {player.currentPosition}else{0})

    }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var updateJob: Job? by remember { mutableStateOf(null) }
    DisposableEffect(Unit) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                playing = isPlaying
                if (isPlaying) {
                    updateJob = coroutineScope.launch {
                        while (player.isPlaying) {
                            currentPosition = player.currentPosition
                            delay(500)
                        }
                    }
                } else {
                    updateJob?.cancel()
                }
            }

            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_READY) {
                    val duration = player.duration
                    if (duration > 0) {
                        //currentPosition = 0
                        totalDuration = duration
                        Intent(context, AudioService::class.java).also {
                            it.action = AudioService.Actions.START.toString()

                            context.startService(it)
                        }
                    }
                }
            }

        }

        player.addListener(listener)

        onDispose {
            player.removeListener(listener)
            Intent(context, AudioService::class.java).also {
                it.action = AudioService.Actions.STOP.toString()

                context.startService(it)
            }
        }
    }

    LaunchedEffect(player.isPlaying) {
        if (player.isPlaying) {
            while (player.isPlaying) {
                currentPosition = player.currentPosition
                delay(500) // Update position every 500ms
            }
        }
    }

    Column (

    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()

        ) {

            if(playing){


                IconButton(onClick = {
                    playing = false
                    player.pause()
                }){
                    Icon(imageVector = Icons.Default.Pause, contentDescription = "Pause")
                }

            }else{
                IconButton(onClick = {
                    playing = true
                    player.play()
                }){

                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Resume")
                }
            }
            Slider(value = currentPosition.toFloat(),
                onValueChange = { newValue ->
                    currentPosition = newValue.roundToLong()
                    player.seekTo(currentPosition) // Seek to the new position in milliseconds
                },
                valueRange = 0f..totalDuration.toFloat(),
                steps = 100,
                modifier = Modifier.fillMaxWidth())
        }


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