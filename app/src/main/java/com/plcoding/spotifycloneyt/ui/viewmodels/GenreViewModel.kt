package com.plcoding.spotifycloneyt.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.plcoding.spotifycloneyt.data.entities.Song
import com.plcoding.spotifycloneyt.data.remote.api.Genre
import com.plcoding.spotifycloneyt.data.remote.api.GenresResponse
import com.plcoding.spotifycloneyt.data.remote.api.RetrofitInstance
import com.plcoding.spotifycloneyt.exoplayer.MusicServiceConnection
import com.plcoding.spotifycloneyt.other.Resource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow

class GenreViewModel @ViewModelInject constructor(
    private val musicServiceConnection: MusicServiceConnection,
    private val retrofit: RetrofitInstance
) : ViewModel() {


    private val _response = MutableLiveData<GenresResponse> ()
    val response:LiveData<GenresResponse> = _response



    init {
        fetchGenres()
    }


    private fun fetchGenres() {
        val fetchGenres = Job()

        val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->

        }

        val scope = CoroutineScope(fetchGenres + Dispatchers.IO)

        val i = scope.async(errorHandler) {
            retrofit.getGenres()
        }
        runBlocking {
            _response.postValue(i.await())
        }
    }

}