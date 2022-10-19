package com.plcoding.spotifycloneyt.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.plcoding.spotifycloneyt.R
import com.plcoding.spotifycloneyt.data.remote.api.Song
import com.plcoding.spotifycloneyt.other.Constants
import kotlinx.android.synthetic.main.item_list.view.*
import javax.inject.Inject

class GenreSongsAdapter @Inject constructor(
    private val songs: List<Song>,
    private val glide: RequestManager
) : RecyclerView.Adapter<GenreSongsAdapter.GenreSongsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreSongsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return GenreSongsViewHolder(view)
    }

    override fun onBindViewHolder(holder: GenreSongsViewHolder, position: Int) {
        val song = songs[position]
        holder.itemView.apply {
            tvPrimary.text = song.title
            tvSecondary.text = song.subtitle

            glide.load(Constants.COVER_BASE_URL + song.image).into(ivItemImage)

            setOnClickListener {
                onItemClickListener?.let { click ->
                    click(song)
                }
            }
        }
    }

    override fun getItemCount() = songs.size

    private var onItemClickListener: ((Song) -> Unit)? = null

    fun setOnItemClickListener(listener: ((Song) -> Unit)?) {
        onItemClickListener = listener
    }


    inner class GenreSongsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}