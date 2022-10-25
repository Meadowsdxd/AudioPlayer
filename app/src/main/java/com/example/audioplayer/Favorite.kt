package com.example.audioplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.audioplayer.databinding.ActivityFavoriteBinding


class Favorite : AppCompatActivity() {


    private lateinit var adapter: FavoriteAdapter
    private lateinit var binding: ActivityFavoriteBinding
    companion object{
        var favoriteSongs: ArrayList<Music> = ArrayList()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        favoriteSongs= checkPlaylist(favoriteSongs)
        binding.backBTNPFA.setOnClickListener { finish() }
        binding.favoriteRV.setHasFixedSize(true)
        binding.favoriteRV.setItemViewCacheSize(13)
        binding.favoriteRV.layoutManager= LinearLayoutManager(this)
        binding.favoriteRV.layoutManager=GridLayoutManager(this,4)
        adapter= FavoriteAdapter(this, favoriteSongs)
        binding.favoriteRV.adapter=adapter
        if(favoriteSongs.size<1) binding.shuffleBTNFA.visibility= View.INVISIBLE
        binding.shuffleBTNFA.setOnClickListener {
            val intent= Intent(this,activity_player::class.java)
            intent.putExtra("index",0)
            intent.putExtra("class","Favorite")
            startActivity(intent)
        }
    }
}