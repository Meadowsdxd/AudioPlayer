package com.example.audioplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.audioplayer.databinding.ActivityMainBinding
import com.example.audioplayer.databinding.ActivityPlaylistBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PlaylistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaylistBinding
   private lateinit var adapter: PlaylistViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val temp=ArrayList<String>()
        temp.add("1 Songs")
        temp.add("2 Songs")
        temp.add("3 Songs")
        binding.playlistRV.setHasFixedSize(true)
        binding.playlistRV.setItemViewCacheSize(13)
        binding.playlistRV.layoutManager= GridLayoutManager(this@PlaylistActivity,2)
        adapter= PlaylistViewAdapter(this, temp)
        binding.playlistRV.adapter=adapter
        binding.backBTNPLA.setOnClickListener { finish() }
binding.addPlaylistBtn.setOnClickListener {
    customAlertDialog()
}
    }
    private fun customAlertDialog(){
        val customDialog= LayoutInflater.from(this@PlaylistActivity).inflate(R.layout.add_playlist_dialog,binding.root,false)
        val builder = MaterialAlertDialogBuilder(this)
        builder.setView(customDialog).setTitle("PlayList Details")
            .setPositiveButton("Add"){ dialog, _ ->
            dialog.dismiss()
            }.show()




    }
}