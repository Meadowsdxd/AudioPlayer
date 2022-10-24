package com.example.audioplayer

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.audioplayer.databinding.ActivityMainBinding
import com.example.audioplayer.databinding.ActivityPlaylistDetailsBinding
import com.example.audioplayer.databinding.PlaylistViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PlaylistDetails : AppCompatActivity() {
    lateinit var binding: ActivityPlaylistDetailsBinding
    lateinit var adapter: MusicAdapter
    companion object{
        var currentPlayListPos: Int = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)

        binding = ActivityPlaylistDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        currentPlayListPos=intent.extras?.get("index") as Int
        binding.playlistDetailsRV.setItemViewCacheSize(10)
        binding.playlistDetailsRV.setHasFixedSize(true)
        binding.playlistDetailsRV.layoutManager=LinearLayoutManager(this)
        adapter = MusicAdapter(this, PlaylistActivity.musicPlaylist.ref[currentPlayListPos].playlist, playlistDetails = true)
        binding.playlistDetailsRV.adapter = adapter
        binding.backBTNPD.setOnClickListener { finish() }
        binding.shuffleDatailsBTN.setOnClickListener {
            val intent= Intent(this,activity_player::class.java)
            intent.putExtra("index",0)
            intent.putExtra("class","PlaylistShuffle")
            startActivity(intent)
    }
    binding.addPd.setOnClickListener {
        startActivity(Intent(this,SelectionActivity::class.java))
    }
    binding.removeAllPD.setOnClickListener {
        val builder=MaterialAlertDialogBuilder(this)
        builder.setTitle("Remove")
            .setMessage("Do you want to remove all song from playlist?")
            .setPositiveButton("Yes"){dialog,_ ->
                PlaylistActivity.musicPlaylist.ref[currentPlayListPos].playlist.clear()
               adapter.refreshPlayList()
                dialog.dismiss()
            }
            .setNegativeButton("No"){dialog,_ ->
                dialog.dismiss()
            }
        val customDialog= builder.create()
        customDialog.show()
        customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
        customDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED)

    }}

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        binding.playlistNamePD.text=PlaylistActivity.musicPlaylist.ref[currentPlayListPos].name
        binding.moreInfoPD.text="Total ${adapter.itemCount} Songs.\n\n"+
                                "Created on:\n ${PlaylistActivity.musicPlaylist.ref[currentPlayListPos].createdQn}\n\n"+
                                " -- ${PlaylistActivity.musicPlaylist.ref[currentPlayListPos].createdBy}"
    if(adapter.itemCount>0)
    {
        Glide.with(this).load(PlaylistActivity.musicPlaylist.ref[currentPlayListPos].playlist[0].artURI).apply(
            RequestOptions()
            .placeholder(R.drawable.ic_music).centerCrop())
            .into(binding.playlistImagePD)
        binding.shuffleDatailsBTN.visibility= View.VISIBLE
    }
        adapter.notifyDataSetChanged()
    }
}