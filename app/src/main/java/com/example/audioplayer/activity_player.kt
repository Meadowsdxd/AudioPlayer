package com.example.audioplayer

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.audioplayer.databinding.ActivityFavoriteBinding
import com.example.audioplayer.databinding.ActivityMainBinding
import com.example.audioplayer.databinding.ActivityPlayerBinding
import java.lang.Exception

class activity_player : AppCompatActivity(),ServiceConnection {
    companion object {
       lateinit var musicListPA : ArrayList<Music>
       var songPosition: Int = 0

        var isPlaying:Boolean=false
        var musicService:MusicService?=null
    }
    private lateinit var binding: ActivityPlayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val  intent=Intent(this,MusicService::class.java)
        bindService(intent,this, BIND_AUTO_CREATE)
        startService(intent)
        initLayout()
        binding.playPausePA.setOnClickListener{
            if(isPlaying) pauseMusic() else playMusic()
        }
        binding.playBackPA.setOnClickListener {
    prevNextSong(increment = false)
        }
        binding.playNextPA.setOnClickListener {
            prevNextSong(increment = true)
        }
    }
    private fun setLayout(){
        Glide.with(this).load(musicListPA[songPosition].artURI).apply(
            RequestOptions()
            .placeholder(R.drawable.ic_music).centerCrop())
            .into(binding.songImagePA)
        binding.songNamePA.text= musicListPA[songPosition].title
    }
    private fun createMediaPlayer() {
        try {
            if (musicService!!.mediaPlayer == null) musicService!!.mediaPlayer = MediaPlayer()
            musicService!!.mediaPlayer!!.reset()
            musicService!!.mediaPlayer!!.setDataSource(musicListPA[songPosition].path)
            musicService!!.mediaPlayer!!.prepare()
            musicService!!.mediaPlayer!!.start()
            isPlaying=true
            binding.playPausePA.setIconResource(R.drawable.ic_pause)
        } catch (e: Exception) {return}

    }
    private fun initLayout(){
        songPosition = intent.getIntExtra("index",0)
        when(intent.getStringExtra("class")){
            "MusicAdapter"->{
                musicListPA= ArrayList()
                musicListPA.addAll(MainActivity.MusiListMA)
                setLayout()
                createMediaPlayer()

            }
            "MainActivity"->{
                musicListPA= ArrayList()
                musicListPA.addAll(MainActivity.MusiListMA)
                musicListPA.shuffle()
                setLayout()
                createMediaPlayer()

            }
        }
    }
    private  fun  playMusic(){
    binding.playPausePA.setIconResource(R.drawable.ic_pause)
        isPlaying=true
        musicService!!.mediaPlayer!!.start()
    }
    private  fun  pauseMusic(){
        binding.playPausePA.setIconResource(R.drawable.ic_play)
        isPlaying=false
        musicService!!.mediaPlayer!!.pause()
    }
    private  fun prevNextSong(increment:Boolean){
        if(increment){
            setSongPosition(increment=true)
            setLayout()
            createMediaPlayer()
        }else{
            setSongPosition(increment=false)
            setLayout()
            createMediaPlayer()
        }
    }
    private fun setSongPosition(increment: Boolean){
        if(increment){
            if(musicListPA.size-1== songPosition) songPosition=0 else ++songPosition
        }else{  if(0== songPosition) songPosition= musicListPA.size-1 else --songPosition
        }
    }

    override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
       val binder=service as MusicService.MyBinder
        musicService=binder.currentService()

    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        musicService=null
    }
}