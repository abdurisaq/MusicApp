package com.example.modularapp.audioplaying

import android.app.Application


class AudioPlayerApp :Application(){

    companion object {
        lateinit var appModule: AudioPlayerModule
    }

    override fun onCreate() {
        super.onCreate()
        appModule = AudioPlayerModuleImpl(this)
    }
}
