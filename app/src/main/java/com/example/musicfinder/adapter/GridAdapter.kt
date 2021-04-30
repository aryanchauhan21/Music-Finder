package com.example.musicfinder.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.musicfinder.R
import com.example.musicfinder.models.Song

class GridAdapter(
    val context: Context,
    val songs: List<Song>
): BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if(convertView == null) {
            convertView = LayoutInflater.from(parent?.context).inflate(R.layout.grid_element, parent, false)
        }
        val imgview = convertView!!.findViewById<ImageView>(R.id.artistImage)
        val nameTv = convertView.findViewById<TextView>(R.id.artistName)
        val songTv = convertView.findViewById<TextView>(R.id.songName)
        Glide.with(convertView!!).load(songs[position].artworkUrl100).into(imgview)
        nameTv.text = songs[position].artistName
        songTv.text = songs[position].trackName
        return convertView
    }

    override fun getItem(position: Int): Any {
        return songs[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return songs.size
    }
}