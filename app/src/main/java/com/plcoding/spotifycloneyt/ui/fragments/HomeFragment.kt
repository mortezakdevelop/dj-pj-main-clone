package com.plcoding.spotifycloneyt.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.view.marginStart
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.plcoding.spotifycloneyt.R
import com.plcoding.spotifycloneyt.adapters.SongAdapter
import com.plcoding.spotifycloneyt.data.entities.Song
import com.plcoding.spotifycloneyt.other.Status
import com.plcoding.spotifycloneyt.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    lateinit var mainViewModel: MainViewModel


    @Inject
    lateinit var glide : RequestManager


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        subscribeToObservers()

    }

    private fun setupRecyclerView(rvSong: RecyclerView,songAdapter: SongAdapter) = rvSong.apply {
        adapter = songAdapter
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun subscribeToObservers() {

        mainViewModel.mediaItems.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    allSongsProgressBar.isVisible = false
                    result.data?.let { songs ->

                        val songsGroupByGenre = songs.groupBy {
                            it.genre
                        }

                        for (item in songsGroupByGenre) {

                            val recyclerView = RecyclerView(requireContext())

                            val txtTitle = TextView(requireContext())

                            txtTitle.text = item.key
                            txtTitle.textSize = 30f


                            val params = RecyclerView.LayoutParams(
                                RecyclerView.LayoutParams.MATCH_PARENT,
                                RecyclerView.LayoutParams.WRAP_CONTENT
                            )

                            recyclerView.layoutParams = params

                            homeMain.addView(txtTitle)
                            homeMain.addView(recyclerView)


                            val tempAdapter = SongAdapter(glide)

                            tempAdapter.setItemClickListener {
                                mainViewModel.playOrToggleSong(it)
                            }

                            setupRecyclerView(recyclerView,tempAdapter)

                            tempAdapter.songs = item.value
                        }
                    }
                }
                Status.ERROR -> Unit
                Status.LOADING -> allSongsProgressBar.isVisible = true
            }
        }
    }
}
















