package com.plcoding.spotifycloneyt.data.remote.api

data class LikeSong(
    val id : String = "",
    val user : String = ""
)

data class CommentOnSong(
    val id : String = "",
    val user : String = "",
    val comment : String = "",
)