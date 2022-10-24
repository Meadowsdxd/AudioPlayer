package com.example.audioplayer

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.audioplayer.databinding.ActivityMainBinding
import com.example.audioplayer.databinding.ActivityPlaylistDetailsBinding
import com.example.audioplayer.databinding.PlaylistViewBinding

class PlaylistDetails : AppCompatActivity() {
    lateinit var binding: ActivityPlaylistDetailsBinding
    lateinit var adapter: MusicAdapter
    companion object{
        var currentPlayListPos: Int = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)

        binding = ActivityPlaylistDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        currentPlayListPos=intent.extras?.get("index") as Int
        binding.playlistDetailsRV.setItemViewCacheSize(10)
        binding.playlistDetailsRV.setHasFixedSize(true)
        binding.playlistDetailsRV.layoutManager=LinearLayoutManager(this)
        PlaylistActivity.musicPlaylist.ref[currentPlayListPos].playlist.addAll(MainActivity.MusiListMA)
        PlaylistActivity.musicPlaylist.ref[currentPlayListPos].playlist.shuffle()
        adapter= MusicAdapter(this, PlaylistActivity.musicPlaylist.ref[currentPlayListPos].playlist,playlistDetails = true)
        binding.playlistDetailsRV.adapter=adapter
        binding.backBTNPD.setOnClickListener { finish() }
        binding.shuffleDatailsBTN.setOnClickListener {
            val intent= Intent(this,activity_player::class.java)
            intent.putExtra("index",0)
            intent.putExtra("class","PlaylistShuffle")
            startActivity(intent)
    }
    binding.addPd.setOnClickListener {
        startActivity(Intent(this,SelectionActivity::class.java))
    }}

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        binding.playlistNamePD.text=PlaylistActivity.musicPlaylist.ref[currentPlayListPos].name
        binding.moreInfoPD.text="Total ${adapter.itemCount} Songs.\n\n"+
                                "Created on:\n ${PlaylistActivity.musicPlaylist.ref[currentPlayListPos].createdQn}\n\n"+
                                " -- ${PlaylistActivity.musicPlaylist.ref[currentPlayListPos].createdBy}"
    if(adapter.itemCount>0)
    {
        Glide.with(this).load(PlaylistActivity.musicPlaylist.ref[currentPlayListPos].playlist[0].artURI).apply(
            RequestOptions()
            .placeholder(R.drawable.ic_music).centerCrop())
            .into(binding.playlistImagePD)
        binding.shuffleDatailsBTN.visibility= View.VISIBLE
    }
    }
}