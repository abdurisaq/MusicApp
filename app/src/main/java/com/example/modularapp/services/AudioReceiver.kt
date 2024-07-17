package com.example.modularapp.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.modularapp.AudioPlayerApp

class AudioReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            val player = AudioPlayerApp.appModule.audioPlayer
            when (intent.action) {
                "com.example.modularapp.ACTION_PLAY_PAUSE" -> {



                    if (player.isPlaying) {
                        player.pause()
                    } else {
                        player.play()
                    }
                    Intent(context, AudioService::class.java).also {
                        it.action = AudioService.Actions.PLAY.toString()

                        context?.startService(it)
                    }
                }
                "com.example.modularapp.ACTION_SKIP" -> {

                    player.seekToNextMediaItem()
                    Intent(context, AudioService::class.java).also {
                        it.action = AudioService.Actions.PLAY.toString()

                        context?.startService(it)
                    }
                }
                "com.example.modularapp.ACTION_PREVIOUS" -> {


                    player.seekToPreviousMediaItem()
                    Intent(context, AudioService::class.java).also {
                        it.action = AudioService.Actions.PLAY.toString()

                        context?.startService(it)
                    }
                }
            }
        }
    }
}

