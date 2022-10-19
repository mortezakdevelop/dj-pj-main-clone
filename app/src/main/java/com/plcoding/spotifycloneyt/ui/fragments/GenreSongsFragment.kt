package com.plcoding.spotifycloneyt.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import com.plcoding.spotifycloneyt.R
import com.plcoding.spotifycloneyt.adapters.GenreSongsAdapter
import com.plcoding.spotifycloneyt.adapters.SongAdapter
import com.plcoding.spotifycloneyt.data.remote.api.Song
import com.plcoding.spotifycloneyt.exoplayer.FirebaseMusicSource
import com.plcoding.spotifycloneyt.other.Constants
import com.plcoding.spotifycloneyt.ui.viewmodels.GenreSongsViewModel
import com.plcoding.spotifycloneyt.ui.viewmodels.GenreViewModel
import com.plcoding.spotifycloneyt.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_genre_songs.*
import javax.inject.Inject

@AndroidEntryPoint
class GenreSongsFragment : Fragment(R.layout.fragment_genre_songs) {

    @Inject
    lateinit var glide: RequestManager

    lateinit var songAdapter : GenreSongsAdapter
    lateinit var viewModel : GenreSongsViewModel
    lateinit var mainViewModel: MainViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        arguments?.let {
            viewModel = ViewModelProvider(requireActivity())[GenreSongsViewModel::class.java]

            val genreId = it.getString(Constants.NAVIGATION_GENRE_ID)
            subscribeToObservers()

            viewModel.fetchGenreSongs(genreId!!)
        } ?: run {
            Toast.makeText(requireContext(), getString(R.string.something_is_wrong_try_later), Toast.LENGTH_SHORT).show()
        }
    }


    private fun subscribeToObservers(){
        viewModel.genreSongs.observe(viewLifecycleOwner) {
            setupRecyclerView(it.data)
        }
    }


    private fun setupRecyclerView(songsList : List<Song>) = recycler_genre_songs.apply {
        songAdapter = GenreSongsAdapter(songsList,glide)
        songAdapter.setOnItemClickListener { song ->
            val songToPlay = FirebaseMusicSource.allSongs.find { allSongs ->
                song._id == allSongs._id
            }
            songToPlay?.let {
                mainViewModel.playOrToggleSongWithMediaId(it._id,false)
            }
        }
        adapter = songAdapter
    }
}