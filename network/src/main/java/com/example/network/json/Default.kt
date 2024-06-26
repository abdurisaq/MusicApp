package com.example.network.json

import kotlinx.serialization.Serializable

@Serializable
data class Default(
    val height: Int,
    val url: String,
    val width: Int
)