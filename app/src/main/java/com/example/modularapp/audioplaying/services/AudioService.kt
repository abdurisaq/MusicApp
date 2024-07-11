package com.example.modularapp.audioplaying.services

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.annotation.OptIn
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaStyleNotificationHelper
import com.example.modularapp.R
import com.example.modularapp.audioplaying.AudioPlayerApp
import com.example.modularapp.audioplaying.data.SongDatabases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.core.app.NotificationCompat.Style
import kotlin.properties.Delegates


class AudioService(): Service() {

    private lateinit var player: Player
    private var isPlaying by Delegates.notNull<Boolean>()
    private var currentSongName: String = ""

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    private fun retrieveSongName(mediaItem: MediaItem?, callback: (String) -> Unit) {
        if (mediaItem == null) {
            callback("")
            return
        }

        val db = SongDatabases.getDatabase(applicationContext)
        val songDao = db.dao
        CoroutineScope(Dispatchers.IO).launch {
            val songName = songDao.getSongName(mediaItem).firstOrNull() ?: ""
            withContext(Dispatchers.Main) { // Switch to the main thread here
                callback(songName)
            }
        }
    }


    private var playPausePendingIntent: PendingIntent? = null

    private val playerListener = object : Player.Listener {
        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            if (mediaItem != null) {
                retrieveSongName(mediaItem) { songName ->
                    currentSongName = songName
                    updateNotification() // Update notification when song changes
                }
            }
        }

        override fun onPlaybackStateChanged(state: Int) {
            val mediaItem = player.currentMediaItem
            if (mediaItem != null) {
                retrieveSongName(mediaItem) { songName ->
                    currentSongName = songName
                    updateNotification() // Update notification when playback state changes
                }
            }
        }


    }

    override fun onCreate() {
        super.onCreate()

        player = AudioPlayerApp.appModule.audioPlayer
        player.addListener(playerListener)
        val playPauseIntent = Intent(this, AudioReceiver::class.java).apply {
            action = "com.example.modularapp.ACTION_PLAY_PAUSE"
        }

        playPausePendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            playPauseIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            Actions.START.toString() ->start()
            Actions.STOP.toString() ->stopSelf()
            Actions.PLAY.toString() -> updateNotification()

        }
        return super.onStartCommand(intent, flags, startId)
    }


    private fun start(){


        val mediaItem = player.currentMediaItem
        if(mediaItem != null){
            val coroutineScope = CoroutineScope(Dispatchers.IO)

            retrieveSongName(mediaItem) { songName ->
                currentSongName = songName
                updateNotification() // Update notification when song changes
            }

        }else{
            Log.d("test","media item passed to onStart transition is null")
        }


//        val notification = buildNotification("", "")
//
//        startForeground(1, notification)
    }



    private fun buildNotification(title: String, artist: String): Notification {
        val icon = if (player.isPlaying) R.drawable.ic_pause else R.drawable.ic_play

        return NotificationCompat.Builder(this, "running_channel")
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .addAction(NotificationCompat.Action.Builder(
                R.drawable.ic_prev, "Previous", playPausePendingIntent)
                .setAuthenticationRequired(false)
                .build())
            .addAction(NotificationCompat.Action.Builder(
                icon, "Pause/Play", playPausePendingIntent)
                .setAuthenticationRequired(false)
                .build())
            .addAction(NotificationCompat.Action.Builder(
                    R.drawable.ic_next, "Skip", playPausePendingIntent)
                .setAuthenticationRequired(false)
                .build())
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(1) // Show pause button in compact view
                .setCancelButtonIntent(playPausePendingIntent) // Set pause action as cancel button
                .setShowCancelButton(true) // Show cancel button
                .setMediaSession(null) // Set media session to null
            )


            .setContentTitle(title)
            .setContentText(artist)

            .build()
    }

    private fun updateNotification() {
        Log.d("test","updating notification ${player.currentMediaItem?.mediaMetadata?.title}")
        val notification = buildNotification(currentSongName, "Unknown")
        startForeground(1, notification)
    }

    override fun onDestroy() {
        player.removeListener(playerListener)
        super.onDestroy()
    }

    enum class Actions {
        START, STOP,PLAY
    }
}