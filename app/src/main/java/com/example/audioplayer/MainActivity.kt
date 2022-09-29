package com.example.audioplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.audioplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.shuffleBTN.setOnClickListener{
            val intent= Intent(this@MainActivity,activity_player::class.java)
            startActivity(intent)
        }
        binding.favoriteBTN.setOnClickListener{
            val intent= Intent(this@MainActivity,Favorite::class.java)
            startActivity(intent)
        }
        binding.playlistBTN.setOnClickListener{
            val intent= Intent(this@MainActivity,PlaylistActivity::class.java)
            startActivity(intent)
        }

    }
}