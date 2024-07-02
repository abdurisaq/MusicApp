package com.example.modularapp.videoplaying

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer


 interface VideoPlayerModule{
    val videoPlayer :Player
    val metaDataReader: MetaDataReader


 }
class VideoPlayerModuleImpl(app:Application) :VideoPlayerModule{
    override val videoPlayer: Player by lazy {
        ExoPlayer.Builder(app).build()
    }

    override val metaDataReader: MetaDataReader by lazy {
        MetaDataReaderImpl(app)
    }

}