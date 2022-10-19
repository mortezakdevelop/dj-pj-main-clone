package com.plcoding.spotifycloneyt.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.plcoding.spotifycloneyt.R
import com.plcoding.spotifycloneyt.data.remote.api.Comment
import com.plcoding.spotifycloneyt.data.remote.api.Genre
import kotlinx.android.synthetic.main.item_comment.view.*
import kotlinx.android.synthetic.main.item_genre.view.*

class CommentAdapter constructor(
    private val lstComments: List<Comment>
) : RecyclerView.Adapter<CommentAdapter.GenreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent,false)
        return GenreViewHolder(view)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val commentItem = lstComments[position]
        holder.itemView.apply {
            txtUserName.text = commentItem.user + ": "
            txtComment.text = commentItem.comment
            setOnClickListener {
                onItemClickListener?.let {
                    it(commentItem)
                }
            }
        }
    }

    override fun getItemCount() = lstComments.size

    private var onItemClickListener: ((Comment) -> Unit)? = null

    fun setOnItemClickListener(listener: ((Comment) -> Unit)?) {
        onItemClickListener = listener
    }


    inner class GenreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}