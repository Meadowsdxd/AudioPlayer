package com.example.audioplayer

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.audioplayer.databinding.FragmentNowPlayingBinding
import com.example.audioplayer.databinding.MusicviewBinding

class NowPlaying : Fragment() {
    companion object{
    @SuppressLint("StaticFieldLeak")
    lateinit var binding: FragmentNowPlayingBinding
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =inflater.inflate(R.layout.fragment_now_playing, container, false)
        binding=FragmentNowPlayingBinding.bind(view)
        binding.root.visibility=View.INVISIBLE
        binding.playPauseNP.setOnClickListener {
            if(activity_player.isPlaying)pauseMusic()else playMusic()
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        if(activity_player.musicService!=null){
            binding.root.visibility=View.VISIBLE
            Glide.with(this).load(activity_player.musicListPA[activity_player.songPosition].artURI).apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_music).centerCrop())
                .into(binding.songimgNP)
            binding.songNameNP.text=activity_player.musicListPA[activity_player.songPosition].title
            if(activity_player.isPlaying) binding.playPauseNP.setIconResource(R.drawable.ic_pause)
            else binding.playPauseNP.setIconResource(R.drawable.ic_play)

        }
    }
    private fun playMusic(){
        activity_player.musicService!!.mediaPlayer!!.start()
        binding.playPauseNP.setIconResource(R.drawable.ic_pause)
        activity_player.musicService!!.showNotification(R.drawable.ic_pause)
        activity_player.binding.playNextPA.setIconResource(R.drawable.ic_pause)
        activity_player.isPlaying=true
    }
    private fun pauseMusic(){
        activity_player.musicService!!.mediaPlayer!!.pause()
        binding.playPauseNP.setIconResource(R.drawable.ic_play)
        activity_player.musicService!!.showNotification(R.drawable.ic_play)
        activity_player.binding.playNextPA.setIconResource(R.drawable.ic_play)
        activity_player.isPlaying=false
    }
}