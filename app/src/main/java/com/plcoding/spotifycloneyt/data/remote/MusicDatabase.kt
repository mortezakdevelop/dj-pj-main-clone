package com.plcoding.spotifycloneyt.data.remote

import com.plcoding.spotifycloneyt.data.remote.api.RetrofitInstance
import com.plcoding.spotifycloneyt.data.remote.api.SongsResponse
import kotlinx.coroutines.*

class MusicDatabase {

    private val retrofitInstance = RetrofitInstance()
    private var response : SongsResponse = SongsResponse(listOf(),"")

    fun fetchSongs() : List<com.plcoding.spotifycloneyt.data.remote.api.Song> {
        val fetchSongsJob = Job()

        val errorHandler = CoroutineExceptionHandler{ coroutineContext, throwable ->
            val temp = throwable.message
            val tt = throwable.localizedMessage
        }

        val scope = CoroutineScope(fetchSongsJob + Dispatchers.IO)



        val i = scope.async(errorHandler) {
            retrofitInstance.getSongs()
        }


        runBlocking {
            response = i.await()
        }

        return response.data

    }
}