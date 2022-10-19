package com.plcoding.spotifycloneyt.ui.fragments

import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import com.plcoding.spotifycloneyt.R
import com.plcoding.spotifycloneyt.adapters.CommentAdapter
import com.plcoding.spotifycloneyt.data.entities.Song
import com.plcoding.spotifycloneyt.data.remote.api.Comment
import com.plcoding.spotifycloneyt.data.remote.api.CommentOnSong
import com.plcoding.spotifycloneyt.data.remote.api.Like
import com.plcoding.spotifycloneyt.data.remote.api.LikeSong
import com.plcoding.spotifycloneyt.exoplayer.FirebaseMusicSource
import com.plcoding.spotifycloneyt.exoplayer.isPlaying
import com.plcoding.spotifycloneyt.exoplayer.toSong
import com.plcoding.spotifycloneyt.other.Status.SUCCESS
import com.plcoding.spotifycloneyt.ui.MainActivity
import com.plcoding.spotifycloneyt.ui.viewmodels.MainViewModel
import com.plcoding.spotifycloneyt.ui.viewmodels.SongViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_song.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SongFragment : Fragment(R.layout.fragment_song) {

    @Inject
    lateinit var glide: RequestManager

    private lateinit var mainViewModel: MainViewModel
    private val songViewModel: SongViewModel by viewModels()

    private var curPlayingSong: Song? = null

    private var playbackState: PlaybackStateCompat? = null

    private var shouldUpdateSeekbar = true

    var isUserLikedSong = false

    var mediaId = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        subscribeToObservers()

        ivPlayPauseDetail.setOnClickListener {
            curPlayingSong?.let {
                mainViewModel.playOrToggleSong(it, true)
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    setCurPlayerTimeToTextView(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                shouldUpdateSeekbar = false
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.let {
                    mainViewModel.seekTo(it.progress.toLong())
                    shouldUpdateSeekbar = true
                }
            }
        })

        ivSkipPrevious.setOnClickListener {
            mainViewModel.skipToPreviousSong()
        }

        ivSkip.setOnClickListener {
            mainViewModel.skipToNextSong()
        }

        mediaId = mainViewModel.curPlayingSongMediaId

        val currentSong = FirebaseMusicSource.allSongs.find {
            it._id == mediaId
        }

        currentSong?.let { song ->
            val isUserLiked = song.like.find {
                it.user == MainActivity.DEVICE_ID
            }
            if (isUserLiked != null) {
                isUserLikedSong = true
                imgLike.setImageResource(R.drawable.ic_like_fill)
            }

            txtLikeCount.text = currentSong.like.size.toString()
            if (song.comment.isNotEmpty())
                setupCommentRecycler(song.comment)
        }



        imgLike.setOnClickListener {
            if (isUserLikedSong) {
                songViewModel.removeLike(LikeSong(mediaId, MainActivity.DEVICE_ID))
            } else {
                songViewModel.likeSong(LikeSong(mediaId, MainActivity.DEVICE_ID))
            }
        }

        imgSendComment.setOnClickListener {
            if (edtComment.text.toString().isNotEmpty()) {
                val comment = CommentOnSong(mediaId,MainActivity.DEVICE_ID,edtComment.text.toString())
                songViewModel.commentOnSong(comment)
            } else {
                Toast.makeText(requireContext(), getString(R.string.please_fill_comment), Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun setupCommentRecycler(lstComment: List<Comment>) = recyclerComments.apply {
        adapter = CommentAdapter(lstComment)
    }

    private fun updateTitleAndSongImage(song: Song) {
        val title = "${song.title} - ${song.subtitle}"
        tvSongName.text = title
        glide.load(song.imageUrl).into(ivSongImage)
    }

    private fun subscribeToObservers() {
        mainViewModel.mediaItems.observe(viewLifecycleOwner) {
            it?.let { result ->
                when (result.status) {
                    SUCCESS -> {
                        result.data?.let { songs ->
                            if (curPlayingSong == null && songs.isNotEmpty()) {
                                curPlayingSong = songs[0]
                                updateTitleAndSongImage(songs[0])
                            }
                        }
                    }
                    else -> Unit
                }
            }
        }
        mainViewModel.curPlayingSong.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            curPlayingSong = it.toSong()
            updateTitleAndSongImage(curPlayingSong!!)
        }
        mainViewModel.playbackState.observe(viewLifecycleOwner) {
            playbackState = it
            ivPlayPauseDetail.setImageResource(
                if (playbackState?.isPlaying == true) R.drawable.ic_pause else R.drawable.ic_play
            )
            seekBar.progress = it?.position?.toInt() ?: 0
        }
        songViewModel.curPlayerPosition.observe(viewLifecycleOwner) {
            if (shouldUpdateSeekbar) {
                seekBar.progress = it.toInt()
                setCurPlayerTimeToTextView(it)
            }
        }
        songViewModel.curSongDuration.observe(viewLifecycleOwner) {
            seekBar.max = it.toInt()
            val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
            tvSongDuration.text = dateFormat.format(it).removeRange(0, 1)
        }

        // like song live data response
        songViewModel.likeSongResponse.observe(viewLifecycleOwner) {
            val currentSong = FirebaseMusicSource.allSongs.find { song ->
                song._id == mediaId
            }
            FirebaseMusicSource.allSongs[FirebaseMusicSource.allSongs.indexOf(currentSong)].like.add(Like(MainActivity.DEVICE_ID))

            txtLikeCount.text = FirebaseMusicSource.allSongs[FirebaseMusicSource.allSongs.indexOf(currentSong)].like.size.toString()

            isUserLikedSong = true
            imgLike.setImageResource(R.drawable.ic_like_fill)
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
        }

        // remove song live data response
        songViewModel.removeLikeSong.observe(viewLifecycleOwner) {


            val currentSong = FirebaseMusicSource.allSongs.find { song ->
                song._id == mediaId
            }
            FirebaseMusicSource.allSongs[FirebaseMusicSource.allSongs.indexOf(currentSong)].like.remove(Like(MainActivity.DEVICE_ID))

            txtLikeCount.text = FirebaseMusicSource.allSongs[FirebaseMusicSource.allSongs.indexOf(currentSong)].like.size.toString()

            isUserLikedSong = false
            imgLike.setImageResource(R.drawable.ic_like)
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
        }

        songViewModel.commentOnSongResponse.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setCurPlayerTimeToTextView(ms: Long) {
        val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
        tvCurTime.text = dateFormat.format(ms).removeRange(0, 1)
    }
}





















