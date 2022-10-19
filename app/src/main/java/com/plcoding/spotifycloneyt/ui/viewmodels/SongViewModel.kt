package com.plcoding.spotifycloneyt.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.spotifycloneyt.data.remote.api.CommentOnSong
import com.plcoding.spotifycloneyt.data.remote.api.LikeSong
import com.plcoding.spotifycloneyt.data.remote.api.MessageResponse
import com.plcoding.spotifycloneyt.data.remote.api.RetrofitInstance
import com.plcoding.spotifycloneyt.exoplayer.MusicService
import com.plcoding.spotifycloneyt.exoplayer.MusicServiceConnection
import com.plcoding.spotifycloneyt.exoplayer.currentPlaybackPosition
import com.plcoding.spotifycloneyt.other.Constants.UPDATE_PLAYER_POSITION_INTERVAL
import kotlinx.coroutines.*

class SongViewModel @ViewModelInject constructor(
    musicServiceConnection: MusicServiceConnection,
    val retrofitInstance: RetrofitInstance
) : ViewModel() {

    private val playbackState = musicServiceConnection.playbackState

    private val _curSongDuration = MutableLiveData<Long>()
    val curSongDuration: LiveData<Long> = _curSongDuration

    private val _curPlayerPosition = MutableLiveData<Long>()
    val curPlayerPosition: LiveData<Long> = _curPlayerPosition

    private val _likeSongResponse  = MutableLiveData<MessageResponse>()
    val likeSongResponse : LiveData<MessageResponse> = _likeSongResponse

    private val _commentOnSongResponse  = MutableLiveData<MessageResponse>()
    val commentOnSongResponse : LiveData<MessageResponse> = _commentOnSongResponse

    private val _removeLikeSong = MutableLiveData<MessageResponse>()
    val removeLikeSong : MutableLiveData<MessageResponse> = _removeLikeSong

    init {
        updateCurrentPlayerPosition()
    }

    private fun updateCurrentPlayerPosition() {
        viewModelScope.launch {
            while(true) {
                val pos = playbackState.value?.currentPlaybackPosition
                if(curPlayerPosition.value != pos) {
                    _curPlayerPosition.postValue(pos)
                    _curSongDuration.postValue(MusicService.curSongDuration)
                }
                delay(UPDATE_PLAYER_POSITION_INTERVAL)
            }
        }
    }

    fun likeSong(likeSong : LikeSong) {
        val likeSongJob = Job()

        val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->

        }

        val scope = CoroutineScope(likeSongJob + Dispatchers.IO)

        val result = scope.async(errorHandler) {
            retrofitInstance.likeSong(likeSong)
        }

        runBlocking {
            _likeSongResponse.postValue(result.await())
        }
    }

    fun commentOnSong(commentOnSong: CommentOnSong) {
        val likeSongJob = Job()

        val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->

        }
        val scope = CoroutineScope(likeSongJob + Dispatchers.IO)

        val result = scope.async(errorHandler) {
            retrofitInstance.commentOnSong(commentOnSong)
        }

        runBlocking {
            _commentOnSongResponse.postValue(result.await())
        }
    }



    fun removeLike(likeSong : LikeSong) {
        val removeLikeSongJob = Job()

        val errorHandler = CoroutineExceptionHandler{ coroutineContext, throwable ->

        }

        val scope = CoroutineScope(removeLikeSongJob + Dispatchers.IO)

        val result = scope.async(errorHandler) {
            retrofitInstance.removeLikeSong(likeSong)
        }

        runBlocking {
            _removeLikeSong.postValue(result.await())
        }
    }
}