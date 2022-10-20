package com.example.audioplayer

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.media.audiofx.AudioEffect
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.SeekBar
import androidx.core.content.ContextCompat
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
        var repeat:Boolean = false
        var nowPlayingId: String=""
        var isFavorite:Boolean = false
        var fIndex: Int = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
     /*   val  intent=Intent(this,MusicService::class.java)
        bindService(intent,this, BIND_AUTO_CREATE)
        startService(intent)*/
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
        binding.repeatBtnPA.setOnClickListener {
            if(!repeat){
                repeat=true
                binding.repeatBtnPA.setColorFilter(ContextCompat.getColor(this,R.color.purple_500))
            }else {
                repeat=false
                binding.repeatBtnPA.setColorFilter(ContextCompat.getColor(this, R.color.teal_200))

            }
        }
        binding.backBTNPA.setOnClickListener { finish() }
        try {
            binding.equalizerBTNPA.setOnClickListener { val EqIntent=Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL)
                EqIntent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, musicService!!.mediaPlayer!!.audioSessionId)
                EqIntent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME,baseContext.packageName)
                EqIntent.putExtra(AudioEffect.EXTRA_CONTENT_TYPE,AudioEffect.CONTENT_TYPE_MUSIC)
                startActivityForResult(EqIntent,13)
            }
        }catch (e:Exception){}
        binding.shareBTNPA.setOnClickListener {
            val shareIntent=Intent()
            shareIntent.action=Intent.ACTION_SEND
            shareIntent.type="audio/*"
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(musicListPA[songPosition].path))
            startActivity(Intent.createChooser(shareIntent,"Sharing Music File"))
        }
        binding.favoriteBTNPA.setOnClickListener {

            if(isFavorite){
            isFavorite=false
            binding.favoriteBTNPA.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            Favorite.favoriteSongs.removeAt(fIndex)
            }else{
                isFavorite=true
                binding.favoriteBTNPA.setImageResource(R.drawable.ic_baseline_favoritefull_24)
                Favorite.favoriteSongs.add(musicListPA[songPosition])
        }
        }
      }
    private fun setLayout(){
        fIndex = favoriteChecker(musicListPA[songPosition].id)
        Glide.with(this).load(musicListPA[songPosition].artURI).apply(
            RequestOptions()
            .placeholder(R.drawable.ic_music).centerCrop())
            .into(binding.songImagePA)
        binding.songNamePA.text= musicListPA[songPosition].title
        if(repeat) binding.repeatBtnPA.setColorFilter(ContextCompat.getColor(this,R.color.purple_500))
        if(isFavorite) binding.favoriteBTNPA.setImageResource(R.drawable.ic_baseline_favoritefull_24)
        else binding.favoriteBTNPA.setImageResource(R.drawable.ic_baseline_favorite_border_24)

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
            nowPlayingId= musicListPA[songPosition].id
        } catch (e: Exception) {return}

    }
    private fun initLayout(){
        songPosition = intent.getIntExtra("index",0)
        when(intent.getStringExtra("class")){
            "Favorite"->{
                val  intent=Intent(this,MusicService::class.java)
                bindService(intent,this, BIND_AUTO_CREATE)
                startService(intent)
                musicListPA= ArrayList()
                musicListPA.addAll(Favorite.favoriteSongs)
                musicListPA.shuffle()
                setLayout()
            }
            "FavoriteAdapter"->{
                val  intent=Intent(this,MusicService::class.java)
                bindService(intent,this, BIND_AUTO_CREATE)
                startService(intent)
                musicListPA= ArrayList()
                musicListPA.addAll(Favorite.favoriteSongs)
                setLayout()
            }
            "NowPlaying"->{
                setLayout()
                binding.tvSeekBarStart.text= formatDuration(musicService!!.mediaPlayer!!.currentPosition.toLong())
                binding.tvSeekBarEnd.text=formatDuration(musicService!!.mediaPlayer!!.duration.toLong())
                binding.seekBarPA.progress= musicService!!.mediaPlayer!!.currentPosition
                binding.seekBarPA.max= musicService!!.mediaPlayer!!.duration
                if(isPlaying) binding.playPausePA.setIconResource(R.drawable.ic_pause)
                else binding.playPausePA.setIconResource(R.drawable.ic_play)
            }
            "MusicAdapterSearch"->{
                val  intent=Intent(this,MusicService::class.java)
                bindService(intent,this, BIND_AUTO_CREATE)
                startService(intent)
                musicListPA= ArrayList()
                musicListPA.addAll(MainActivity.musicListSearch)
                setLayout()
            }
            "MusicAdapter"->{
                val  intent=Intent(this,MusicService::class.java)
                bindService(intent,this, BIND_AUTO_CREATE)
                startService(intent)
                musicListPA= ArrayList()
                musicListPA.addAll(MainActivity.MusiListMA)
                setLayout()
                createMediaPlayer()

            }
            "MainActivity"->{
                val  intent=Intent(this,MusicService::class.java)
                bindService(intent,this, BIND_AUTO_CREATE)
                startService(intent)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==13|| resultCode== RESULT_OK){
            return
        }
    }
}