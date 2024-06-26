package com.example.network.json

import kotlinx.serialization.Serializable

@Serializable
data class YoutubeResponse (
    val kind: String,
    val etag: String,
    val items: List<YoutubeResponseItem>

)