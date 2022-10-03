package com.example.audioplayer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.audioplayer.databinding.ActivityFavoriteBinding
import com.example.audioplayer.databinding.ActivityMainBinding
import com.example.audioplayer.databinding.ActivityPlayerBinding

class activity_player : AppCompatActivity() {
    companion object {
       lateinit var musicListPA : ArrayList<Music>
       var songPosition: Int = 0
       var mediaPlayer:MediaPlayer? = null
    }
    private lateinit var binding: ActivityPlayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        songPosition = intent.getIntExtra("index",0)
        when(intent.getStringExtra("class")){
            "MusicAdapter"->{
                musicListPA= ArrayList()
                musicListPA.addAll(MainActivity.MusiListMA)
                if(mediaPlayer==null) mediaPlayer= MediaPlayer()
                mediaPlayer!!.reset()
                mediaPlayer!!.setDataSource(musicListPA[songPosition].path)
                mediaPlayer!!.prepare()
                mediaPlayer!!.start()
            }
        }
    }
}