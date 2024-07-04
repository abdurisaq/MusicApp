package com.example.modularapp.audioplaying

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build


class AudioPlayerApp :Application(){


    companion object {
        lateinit var appModule: AudioPlayerModule
    }

    override fun onCreate() {
        super.onCreate()
        appModule = AudioPlayerModuleImpl(this)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                "running_channel",
                "Running Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
