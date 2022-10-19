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
import com.example.audioplayer.databinding.ActivityFavoriteBinding
import com.example.audioplayer.databinding.FavoriteViewBinding

import com.example.audioplayer.databinding.MusicviewBinding

class FavoriteAdapter(private val  context:Context,private var  musicList:ArrayList<Music>): RecyclerView.Adapter<FavoriteAdapter.MyHolder>() {

    class MyHolder(binding: FavoriteViewBinding):RecyclerView.ViewHolder(binding.root) {
        val image=binding.songImgFV
        val name=binding.songNameFV
        val root=binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteAdapter.MyHolder {
        return MyHolder(FavoriteViewBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
    holder.name.text=musicList[position].title
        Glide.with(context)
            .load(musicList[position].artURI )
            .apply(RequestOptions()
            .placeholder(R.drawable.ic_music)
            .centerCrop())
            .into(holder.image)
        holder.root.setOnClickListener {
            val intent=Intent(context,activity_player::class.java)
            intent.putExtra("index",position)
            intent.putExtra("class","FavoriteAdapter")
            ContextCompat.startActivity(context,intent,null)
        }
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

}