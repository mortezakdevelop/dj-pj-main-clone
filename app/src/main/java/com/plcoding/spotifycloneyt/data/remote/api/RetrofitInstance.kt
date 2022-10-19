package com.plcoding.spotifycloneyt.data.remote.api

import com.plcoding.spotifycloneyt.other.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    private val apiInterface:APIInterface


    init {

        val retrofit = Retrofit
            .Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiInterface = retrofit.create(APIInterface::class.java)
    }

    suspend fun getSongs() : SongsResponse {
        return apiInterface.getSongs()
    }

    suspend fun getGenres() : GenresResponse {
        return apiInterface.getGenres()
    }

    suspend fun getGenreSongs(genreId : String) : SongsResponse {
        return apiInterface.getGenreSongs(genreId)
    }


    suspend fun likeSong(likeSong: LikeSong) : MessageResponse {
        return apiInterface.likeSong(likeSong)
    }

    suspend fun commentOnSong(commentOnSong: CommentOnSong) : MessageResponse {
        return apiInterface.commentOnSong(commentOnSong)
    }

    suspend fun removeLikeSong(likeSong: LikeSong) : MessageResponse {
        return apiInterface.removeLikeSong(likeSong)
    }
}