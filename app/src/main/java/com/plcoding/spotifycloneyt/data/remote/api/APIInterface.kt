package com.plcoding.spotifycloneyt.data.remote.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface APIInterface {

    @GET("songs")
    suspend fun getSongs(): SongsResponse

    @GET("genre")
    suspend fun getGenres(): GenresResponse

    @GET("genre/songs/{id}")
    suspend fun getGenreSongs(@Path("id") id : String): SongsResponse

    @POST("song/like")
    suspend fun likeSong(@Body likeSongBody : LikeSong) : MessageResponse

    @POST("song/like/delete")
    suspend fun removeLikeSong(@Body likeSongBody: LikeSong) : MessageResponse

    @POST("song/comment")
    suspend fun commentOnSong(@Body commentBody : CommentOnSong) : MessageResponse

}