package com.example.audioplayer

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.audioplayer.databinding.PlaylistViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class PlaylistViewAdapter (private val  context: Context, private var  playlistList:ArrayList<Playlist>): RecyclerView.Adapter<PlaylistViewAdapter.MyHolder>() {

    class MyHolder(binding: PlaylistViewBinding): RecyclerView.ViewHolder(binding.root) {
        val image=binding.playlistImg
        val name=binding.PlayListNamePL
        val root=binding.root
        val delete=binding.playlistDeleteBTNPL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewAdapter.MyHolder {
        return MyHolder(PlaylistViewBinding.inflate(LayoutInflater.from(context),parent,false))
    }


    override fun getItemCount(): Int {
        return playlistList.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.name.text=playlistList[position].name
            holder.name.isSelected=true
        holder.delete.setOnClickListener {
            val builder = MaterialAlertDialogBuilder(context)
            builder.setTitle(playlistList[position].name)
                .setMessage("Delete playlist?")
                .setPositiveButton("Yes"){ dialog, _ ->
                    PlaylistActivity.musicPlaylist.ref.removeAt(position)
                    refreshPlaylist()
                    dialog.dismiss()
                 }
                .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()

                }
val customDialog = builder.create()
            customDialog.show()
            customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
            customDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED)
        }
        holder.root.setOnClickListener {
        val intent=Intent(context,PlaylistDetails::class.java)
            intent.putExtra("index",position)
            ContextCompat.startActivity(context,intent,null)

        }
        if(PlaylistActivity.musicPlaylist.ref[position].playlist.size>0){
            Glide.with(context).load(PlaylistActivity.musicPlaylist.ref[position].playlist[0].artURI).apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_music).centerCrop())
                .into(holder.image)
        }
        }

    fun refreshPlaylist(){
        playlistList= ArrayList()
        playlistList.addAll(PlaylistActivity.musicPlaylist.ref)
        notifyDataSetChanged()
    }
}