package com.example.modularapp.audio

import android.app.Application
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer


object AudioPlayerState {
    var currentPlaylist: Int = 0
}

interface AudioPlayerModule{
    val audioPlayer :Player
    val metaDataReader: AudioDataReader
    var currentPlaylist: Int
        get() = AudioPlayerState.currentPlaylist
        set(value) {
            AudioPlayerState.currentPlaylist = value
        }

}


class AudioPlayerModuleImpl(app:Application) : AudioPlayerModule {

    override val audioPlayer: Player by lazy {
        ExoPlayer.Builder(app).build()
            .also { exoPlayer ->

            exoPlayer.playWhenReady = true
        }
    }

    override val metaDataReader: AudioDataReader by lazy {
        AudioDataReaderImpl(app)
    }

}