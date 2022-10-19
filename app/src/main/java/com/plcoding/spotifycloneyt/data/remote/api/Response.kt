package com.plcoding.spotifycloneyt.data.remote.api

data class SongsResponse(
    val data : List<Song>,
    val message : String = ""
)

data class MessageResponse(
    val message : String = ""
)

data class GenresResponse(
    val data : List<Genre>,
    val message : String = ""
)

data class Song(
    val _id: String = "",
    val title: String = "",
    val subtitle: String = "",
    val genre: Genre,
    val song: String = "",
    val image: String = "",
    val like : MutableList<Like>,
    val comment: List<Comment>,
)

data class Comment (
    val _id : String = "",
    val songId : String = "",
    val user : String = "",
    val comment : String = "",
    val status : String = "",
        )


data class Like(
    val user: String = ""
)

data class Genre(
    val _id: String = "",
    val title: String = "",
)
