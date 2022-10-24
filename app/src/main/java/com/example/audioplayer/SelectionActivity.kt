package com.example.audioplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.audioplayer.databinding.ActivityPlayerBinding
import com.example.audioplayer.databinding.ActivitySelectionBinding

class SelectionActivity : AppCompatActivity() {
    lateinit var binding: ActivitySelectionBinding
    lateinit var adapter: MusicAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)
        binding = ActivitySelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.SelectionRV.setItemViewCacheSize(30)
        binding.SelectionRV.setHasFixedSize(true)
        binding.SelectionRV.layoutManager = LinearLayoutManager(this)
        adapter = MusicAdapter(this, MainActivity.MusiListMA, selectionActivity = true)
        binding.SelectionRV.adapter = adapter
        binding.backBTNPSA.setOnClickListener { finish() }
        //search
        binding.searchViewSA.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean =true

            override fun onQueryTextChange(newText: String?): Boolean {
                MainActivity.musicListSearch = ArrayList()
                if(newText != null){
                    val userInput = newText.lowercase()
                    for (song in MainActivity.MusiListMA)
                        if(song.title.lowercase().contains(userInput))
                            MainActivity.musicListSearch.add(song)
                    MainActivity.search = true
                    adapter.updateMusicList(searchList = MainActivity.musicListSearch)
                }
                return true
            }
        })
    }
}