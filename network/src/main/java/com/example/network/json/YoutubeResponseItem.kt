package com.example.network.json

import kotlinx.serialization.Serializable

@Serializable
data class YoutubeResponseItem(
    val etag: String,
    val id: Id,
    val kind: String,
    val snippet: Snippet
)