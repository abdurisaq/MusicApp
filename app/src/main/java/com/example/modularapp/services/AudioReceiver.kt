package com.example.modularapp.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.modularapp.AudioPlayerApp

class AudioReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            when (intent.action) {
                "com.example.modularapp.ACTION_PLAY_PAUSE" -> {

                    val player = AudioPlayerApp.appModule.audioPlayer

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
            }
        }
    }
}


//
//val mediaItem = player.currentMediaItem
//if(context != null && (mediaItem != null)){
//    val coroutineScope = CoroutineScope(Dispatchers.IO)
//    coroutineScope.launch {
//        Log.d("test","breakpoint 1")
//        val db = SongDatabases.getDatabase(context)
//        Log.d("test","breakpoint 2")
//        val songDao = db.dao
//        Log.d("test","breakpoint 3")
//        val name = songDao.getSongName(mediaItem)[0]
//    }
//
//}
//
//Log.d("test","pausing ${player.currentPosition}")
//Log.d("test","mediaitem ${player.currentMediaItem?.mediaMetadata}")