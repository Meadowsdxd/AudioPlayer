package com.example.audioplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.audioplayer.databinding.ActivityFavoriteBinding
import com.example.audioplayer.databinding.ActivityMainBinding

class Favorite : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: FavoriteAdapter

    companion object{
        var favoriteSongs: ArrayList<Music> = ArrayList()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBTNPFA.setOnClickListener { finish() }
        binding.favoriteRV.setHasFixedSize(true)
        binding.favoriteRV.setItemViewCacheSize(13)
        binding.favoriteRV.layoutManager= LinearLayoutManager(this)
        binding.favoriteRV.layoutManager=GridLayoutManager(this,4)
        adapter= FavoriteAdapter(this, favoriteSongs)
        binding.favoriteRV.adapter=adapter
    }
}