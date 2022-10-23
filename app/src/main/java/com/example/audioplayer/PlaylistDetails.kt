package com.example.audioplayer

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
        setTheme(R.style.Theme_AudioPlayer)

        binding = ActivityPlaylistDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        currentPlayListPos=intent.extras?.get("index") as Int
        binding.playlistDetailsRV.setItemViewCacheSize(10)
        binding.playlistDetailsRV.setHasFixedSize(true)
        binding.playlistDetailsRV.layoutManager=LinearLayoutManager(this)
        PlaylistActivity.musicPlaylist.ref[currentPlayListPos].playlist.addAll(MainActivity.MusiListMA)
        adapter= MusicAdapter(this, PlaylistActivity.musicPlaylist.ref[currentPlayListPos].playlist,playlistDetails = true)
        binding.playlistDetailsRV.adapter=adapter

    }

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