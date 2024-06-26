package com.example.network.json

import kotlinx.serialization.Serializable

@Serializable
data class High(
    val height: Int,
    val url: String,
    val width: Int
)