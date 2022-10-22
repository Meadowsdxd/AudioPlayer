package com.example.audioplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.audioplayer.databinding.ActivityMainBinding
import com.example.audioplayer.databinding.ActivityPlaylistDetailsBinding
import com.example.audioplayer.databinding.PlaylistViewBinding

class PlaylistDetails : AppCompatActivity() {
    lateinit var binding: ActivityPlaylistDetailsBinding
    companion object{
        var currentPlayListPos: Int = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_AudioPlayer)

        binding = ActivityPlaylistDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        currentPlayListPos=intent.extras?.get("index") as Int
    }
}