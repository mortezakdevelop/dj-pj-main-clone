package com.plcoding.spotifycloneyt.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.plcoding.spotifycloneyt.R
import com.plcoding.spotifycloneyt.adapters.GenreAdapter
import com.plcoding.spotifycloneyt.data.remote.api.Genre
import com.plcoding.spotifycloneyt.other.Constants
import com.plcoding.spotifycloneyt.ui.viewmodels.GenreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_genre.*

@AndroidEntryPoint
class GenreFragment : Fragment(R.layout.fragment_genre) {

    lateinit var genreAdapter: GenreAdapter
    lateinit var genreViewModel: GenreViewModel
    var genreList: List<Genre> = listOf()
    var searchedGenreList: MutableList<Genre> = mutableListOf()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        genreViewModel = ViewModelProvider(requireActivity()).get(GenreViewModel::class.java)
        subscribeToObservers()

        search_view.setOnCloseListener {
            setupRecyclerView(genreList)
            false
        }


        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(txtToSearch: String?): Boolean {
                txtToSearch?.let {
                    searchedGenreList.clear()
                    if (txtToSearch.isEmpty()) {
                        setupRecyclerView(genreList)
                    } else {
                        for (item in genreList) {
                            if (item.title.toLowerCase().contains(txtToSearch)) {
                                searchedGenreList.add(item)
                            }
                        }

                        setupRecyclerView(searchedGenreList)
                    }
                } ?: run {
                    setupRecyclerView(genreList)
                }
                return true
            }

        })

    }

    private fun setupRecyclerView(itemsList: List<Genre>) {
        genreAdapter = GenreAdapter(itemsList)
        genreAdapter.setOnItemClickListener {
            val bundle = Bundle()
            bundle.putString(Constants.NAVIGATION_GENRE_ID, it._id)
            findNavController().navigate(R.id.action_genreFragment_to_genreSongsFragment,bundle)
        }
        recycler_genre.adapter = genreAdapter
    }

    private fun subscribeToObservers() {
        genreViewModel.response.observe(viewLifecycleOwner, Observer {
            genreList = it.data
            setupRecyclerView(genreList)
        })
    }

}