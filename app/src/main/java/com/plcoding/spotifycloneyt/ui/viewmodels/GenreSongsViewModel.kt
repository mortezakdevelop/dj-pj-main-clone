package com.plcoding.spotifycloneyt.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.plcoding.spotifycloneyt.data.remote.api.RetrofitInstance
import com.plcoding.spotifycloneyt.data.remote.api.Song
import com.plcoding.spotifycloneyt.data.remote.api.SongsResponse
import kotlinx.coroutines.*

class GenreSongsViewModel @ViewModelInject constructor(
    private val retrofitInstance: RetrofitInstance
)  : ViewModel() {


    private var _genreSongs = MutableLiveData<SongsResponse>()
    val genreSongs : MutableLiveData<SongsResponse> = _genreSongs


    fun fetchGenreSongs(genreId : String) {
       val fetchGenreSongsJob = Job()

        val errorHandler = CoroutineExceptionHandler{coroutineContext, throwable ->

            val i = throwable
        }

        val scope = CoroutineScope(fetchGenreSongsJob + Dispatchers.IO)

        val result = scope.async(errorHandler) {
            retrofitInstance.getGenreSongs(genreId)
        }

        runBlocking {
            _genreSongs.postValue(result.await())
        }
    }
}