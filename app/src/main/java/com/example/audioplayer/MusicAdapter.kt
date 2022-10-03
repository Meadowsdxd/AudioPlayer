package com.example.audioplayer

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.util.TimeUtils.formatDuration
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.example.audioplayer.databinding.MusicviewBinding

class MusicAdapter(private val  context:Context,private val  musicList:ArrayList<Music>): RecyclerView.Adapter<MusicAdapter.MyHolder>() {

    class MyHolder(binding: MusicviewBinding):RecyclerView.ViewHolder(binding.root) {
        val title=binding.songNameMV
        val album=binding.songAlbumMV
        val image=binding.imageView
        val duration=binding.songDuration
        val root =binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicAdapter.MyHolder {
        return MyHolder(MusicviewBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        holder.title.text=musicList[position].title
        holder.album.text=musicList[position].album
        holder.duration.text= formatDuration(musicList[position].duration)
        Glide.with(context).load(musicList[position].artURI).apply(RequestOptions()
            .placeholder(R.drawable.ic_music).centerCrop())
            .into(holder.image)
        holder.root.setOnClickListener{
            val intent=Intent(context,activity_player::class.java)
            ContextCompat.startActivity(context,intent,null)
        }
    }

    override fun getItemCount(): Int {
       return musicList.size
    }
}