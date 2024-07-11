package com.example.modularapp.audioplaying

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentCallbacks2
import android.content.Context
import android.os.Build


class AudioPlayerApp :Application(), ComponentCallbacks2 {


    companion object {
        lateinit var appModule: AudioPlayerModule
    }
    //var staticAppModule = appModule
    override fun onCreate() {
        super.onCreate()
        appModule = AudioPlayerModuleImpl(this)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                "running_channel",
                "Running Notifications",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        registerComponentCallbacks(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        // Release player resources
        appModule.audioPlayer.release()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level >= ComponentCallbacks2.TRIM_MEMORY_MODERATE) {
            // Release player resources when memory is low
            appModule.audioPlayer.release()
        }
    }

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        // Release player resources when memory is low
        appModule.audioPlayer.release()
    }

}
