package com.plcoding.spotifycloneyt.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.plcoding.spotifycloneyt.R
import com.plcoding.spotifycloneyt.data.remote.api.Genre
import kotlinx.android.synthetic.main.item_genre.view.*

class GenreAdapter constructor(
    private val lstGenre: List<Genre>
) : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_genre, parent,false)
        return GenreViewHolder(view)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val genreItem = lstGenre[position]
        holder.itemView.apply {
            txt_genre.text = genreItem.title
            setOnClickListener {
                onItemClickListener?.let {
                    it(genreItem)
                }
            }
        }
    }

    override fun getItemCount() = lstGenre.size

    private var onItemClickListener: ((Genre) -> Unit)? = null

    fun setOnItemClickListener(listener: ((Genre) -> Unit)?) {
        onItemClickListener = listener
    }


    inner class GenreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}