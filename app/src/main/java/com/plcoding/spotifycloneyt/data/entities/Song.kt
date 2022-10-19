package com.plcoding.spotifycloneyt.data.entities

import com.plcoding.spotifycloneyt.data.remote.api.Like

data class Song(
    val mediaId: String = "",
    val title: String = "",
    val subtitle: String = "",
    val songUrl: String = "",
    val imageUrl: String = "",
    val genre: String = "",
    val likes : List<Like> = listOf()
)