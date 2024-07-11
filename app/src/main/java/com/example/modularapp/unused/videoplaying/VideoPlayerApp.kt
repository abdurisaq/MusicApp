package com.example.modularapp.unused.videoplaying

import android.app.Application


class VideoPlayerApp :Application(){

    companion object {
        lateinit var appModule: VideoPlayerModule
    }

    override fun onCreate() {
        super.onCreate()
        appModule = VideoPlayerModuleImpl(this)
    }
}
