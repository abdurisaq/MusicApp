package com.example.modularapp.audioplaying

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentCallbacks2
import android.content.Context
import android.os.Build
import android.util.Log
import com.example.modularapp.audioplaying.data.SongDatabases


class AudioPlayerApp :Application() {//, ComponentCallbacks2


    companion object {
        @Volatile
        private var INSTANCE: AudioPlayerModule? = null

        val appModule: AudioPlayerModule
            get() = INSTANCE ?: throw IllegalStateException("AudioPlayerModule not initialized")

        fun initializeAppModule(app: Application) {
            synchronized(this) {
                INSTANCE?.audioPlayer?.release()
                INSTANCE = AudioPlayerModuleImpl(app).also {
                    Log.d("AudioPlayerApp", "AudioPlayerModule initialized")
                }
            }
        }
    }
    //var staticAppModule = appModule
    override fun onCreate() {
        super.onCreate()
        //val db = SongDatabases.getDatabase(this)
        Log.d("playerAudio","building audioplayer")
        initializeAppModule(this)
        //appModule = AudioPlayerModuleImpl(this)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                "running_channel",
                "Running Notifications",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            //registerComponentCallbacks(this)
        }
    }


    override fun onTerminate() {
        super.onTerminate()
        // Release player resources
        appModule.audioPlayer.release()
    }



}
