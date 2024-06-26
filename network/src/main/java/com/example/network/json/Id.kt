package com.example.network.json

import kotlinx.serialization.Serializable

@Serializable
data class Id(
    val kind: String,
    val videoId: String
)