package com.example.audioplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.audioplayer.databinding.ActivityMainBinding
import com.example.audioplayer.databinding.ActivityPlaylistBinding
import com.example.audioplayer.databinding.AddPlaylistDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PlaylistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaylistBinding
   private lateinit var adapter: PlaylistViewAdapter
   companion object{
       var musicPlaylist:MusicPlayList= MusicPlayList()
   }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)
        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.playlistRV.setHasFixedSize(true)
        binding.playlistRV.setItemViewCacheSize(13)
        binding.playlistRV.layoutManager= GridLayoutManager(this@PlaylistActivity,2)
        adapter= PlaylistViewAdapter(this, playlistList = musicPlaylist.ref)
        binding.playlistRV.adapter=adapter
        binding.backBTNPLA.setOnClickListener { finish() }
binding.addPlaylistBtn.setOnClickListener {
    customAlertDialog()
}
    }
    private fun customAlertDialog(){
        val customDialog= LayoutInflater.from(this@PlaylistActivity).inflate(R.layout.add_playlist_dialog,binding.root,false)
        val binder=AddPlaylistDialogBinding.bind(customDialog)

        val builder = MaterialAlertDialogBuilder(this)
        builder.setView(customDialog).setTitle("PlayList Details")
            .setPositiveButton("Add"){ dialog, _ ->
              val playlistName= binder.playlistNamePD.text
              val createdBy=binder.yourlistNamePD.text
              if(playlistName!=null && createdBy!=null)
              if(playlistName.isNotEmpty() &&createdBy.isNotEmpty()){
                  addPlaylist(playlistName.toString(),createdBy.toString())
              }

            dialog.dismiss()
            }.show()
    }
    private fun addPlaylist(name:String,createdBy:String){
        var playlistExists=false
        for(i in musicPlaylist.ref){
            if(name.equals(i.name)){
                playlistExists=true
                break
            }
        }
        if(playlistExists)Toast.makeText(this,"Pl Exist!",Toast.LENGTH_SHORT).show()
        else{
            val tempPlayList=Playlist()
            tempPlayList.name=name
            tempPlayList.playlist= ArrayList()
            tempPlayList.createdBy=createdBy
            val calender = Calendar.getInstance().time
            val sdf=SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
            tempPlayList.createdQn=sdf.format(calender)
            musicPlaylist.ref.add(tempPlayList)
            adapter.refreshPlaylist()
        }
    }
}