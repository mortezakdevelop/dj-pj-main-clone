package com.plcoding.spotifycloneyt.exoplayer

import android.media.MediaMetadata.METADATA_KEY_GENRE
import android.support.v4.media.MediaMetadataCompat
import com.plcoding.spotifycloneyt.data.entities.Song

fun MediaMetadataCompat.toSong(): Song? {
    return description?.let {

        val bundle = it.extras
        val genre = bundle?.getString(METADATA_KEY_GENRE) ?: "Unknown genre"

        Song(
            it.mediaId ?: "",
            it.title.toString(),
            it.subtitle.toString(),
            it.mediaUri.toString(),
            it.iconUri.toString(),
            genre
        )
    }
}