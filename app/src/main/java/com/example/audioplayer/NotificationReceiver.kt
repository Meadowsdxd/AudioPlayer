package com.example.audioplayer

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlin.system.exitProcess

class NotificationReceiver: BroadcastReceiver() {
    override fun onReceive(p0: Context?, intent: Intent?) {
        when(intent?.action){
            AppClass.BACK->{prevNextSong(increment = false, context = p0!!)}
            AppClass.NEXT->{prevNextSong(increment = true, context = p0!!)}
            AppClass.PLAY->{
                if(activity_player.isPlaying)pauseMusic()else playMusic()}
            AppClass.EXIT-> {
               activity_player.musicService!!.stopForeground(true)
               activity_player.musicService!!.mediaPlayer!!.release()
                activity_player.musicService=null
               exitProcess(1)
            }
        }
    }
    fun playMusic(){
        activity_player.isPlaying=true
        activity_player.musicService!!.mediaPlayer!!.start()
        activity_player.musicService!!.showNotification(R.drawable.ic_pause)
        activity_player.binding.playPausePA.setIconResource(R.drawable.ic_pause)

    }
    fun pauseMusic(){
        activity_player.isPlaying=false
        activity_player.musicService!!.mediaPlayer!!.pause()
        activity_player.musicService!!.showNotification(R.drawable.ic_play)
        activity_player.binding.playPausePA.setIconResource(R.drawable.ic_play)

    }
    private fun prevNextSong(increment:Boolean, context: Context){
        setSongPosition(increment=increment)
        activity_player.musicService!!.createMediaPlayer()
        Glide.with(context)
            .load(activity_player.musicListPA[activity_player.songPosition].artURI)
            .apply(RequestOptions()
            .placeholder(R.drawable.ic_music).centerCrop())
            .into(activity_player.binding.songImagePA)
        activity_player.binding.songNamePA.text= activity_player.musicListPA[activity_player.songPosition].title
        playMusic()
    }
}