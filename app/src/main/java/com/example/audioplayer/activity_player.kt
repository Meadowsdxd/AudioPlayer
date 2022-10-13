package com.example.audioplayer

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.SeekBar
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.audioplayer.databinding.ActivityPlayerBinding

class activity_player : AppCompatActivity(),ServiceConnection,MediaPlayer.OnCompletionListener {
    companion object {
       lateinit var musicListPA : ArrayList<Music>
       var songPosition: Int = 0
        @SuppressLint("StaticFieldLeak")
        lateinit var binding: ActivityPlayerBinding
        var isPlaying:Boolean=false
        var musicService:MusicService?=null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val  intent=Intent(this,MusicService::class.java)
        bindService(intent,this, BIND_AUTO_CREATE)
        startService(intent)
        initLayout()
        binding.playPausePA.setOnClickListener{ if(isPlaying) pauseMusic() else playMusic() }
        binding.playBackPA.setOnClickListener { prevNextSong(increment = false) }
        binding.playNextPA.setOnClickListener { prevNextSong(increment = true) }
        binding.seekBarPA.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar:SeekBar?, progress: Int, p2: Boolean) {
            if(p2) musicService!!.mediaPlayer!!.seekTo(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) = Unit

            override fun onStopTrackingTouch(p0: SeekBar?) = Unit

        })
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
            musicService!!.showNotification(R.drawable.ic_pause)
            binding.tvSeekBarStart.text= formatDuration(musicService!!.mediaPlayer!!.currentPosition.toLong())
            binding.tvSeekBarEnd.text= formatDuration(musicService!!.mediaPlayer!!.duration.toLong())
            binding.seekBarPA.progress=0
            binding.seekBarPA.max= musicService!!.mediaPlayer!!.duration
            musicService!!.mediaPlayer!!.setOnCompletionListener(this)

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
        musicService!!.showNotification(R.drawable.ic_pause)
        isPlaying=true
        musicService!!.mediaPlayer!!.start()
    }
    private  fun  pauseMusic(){
        binding.playPausePA.setIconResource(R.drawable.ic_play)
        musicService!!.showNotification(R.drawable.ic_play)
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


    override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
       val binder=service as MusicService.MyBinder
        musicService=binder.currentService()
        createMediaPlayer()
        musicService!!.seekBarSetup()

    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        musicService=null
    }

    override fun onCompletion(p0: MediaPlayer?) {
        setSongPosition(increment = true)
        createMediaPlayer()
        try {
            setLayout()
        }catch (e:Exception){}
    }
}