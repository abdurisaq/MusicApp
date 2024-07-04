package com.example.modularapp.audioplaying.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.modularapp.R

class AudioService: Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            Actions.START.toString() ->start()
            Actions.STOP.toString() ->stopSelf()

        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start(){
        //to update notification, just remake a notification with same id
        val notification = NotificationCompat.Builder(this,"running_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Run is active")
            .setContentText("Elapsed time: 00:50")
            .build()
        startForeground(1, notification)
    }

    enum class Actions {
        START, STOP
    }
}