package com.example.audioplayer

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.media.MediaSession2
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import java.lang.Exception


class MusicService: Service() {
    private  val myBinder =MyBinder()
    var mediaPlayer:MediaPlayer?=null
    private lateinit var mediaSession: MediaSessionCompat
    override fun onBind(p0: Intent?): IBinder? {
        mediaSession= MediaSessionCompat(baseContext,"My Music")
        return myBinder
    }

    inner class MyBinder : Binder() {
        fun  currentService(): MusicService {
            return this@MusicService
        }
    }
     fun createMediaPlayer() {
        try {
            if (activity_player.musicService!!.mediaPlayer == null) activity_player.musicService!!.mediaPlayer = MediaPlayer()
            activity_player.musicService!!.mediaPlayer!!.reset()
            activity_player.musicService!!.mediaPlayer!!.setDataSource(activity_player.musicListPA[activity_player.songPosition].path)
            activity_player.musicService!!.mediaPlayer!!.prepare()

            activity_player.binding.playPausePA.setIconResource(R.drawable.ic_pause)
            activity_player.musicService!!.showNotification(R.drawable.ic_pause)
        } catch (e: Exception) {return}

    }
    @SuppressLint("UnspecifiedImmutableFlag")
    fun showNotification(playPauseBtn: Int){
        val intent = Intent(baseContext, MainActivity::class.java)

        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        val contentIntent = PendingIntent.getActivity(this, 0, intent, flag)

        val prevIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(AppClass.BACK)
        val prevPendingIntent = PendingIntent.getBroadcast(baseContext, 0, prevIntent, flag)

        val playIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(AppClass.PLAY)
        val playPendingIntent = PendingIntent.getBroadcast(baseContext, 0, playIntent, flag)

        val nextIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(AppClass.NEXT)
        val nextPendingIntent = PendingIntent.getBroadcast(baseContext, 0, nextIntent, flag)

        val exitIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(AppClass.EXIT)
        val exitPendingIntent = PendingIntent.getBroadcast(baseContext, 0, exitIntent, flag)
        val imgArt= getImgArt(activity_player.musicListPA[activity_player.songPosition].path)
        val image=  if(imgArt!=null){
            BitmapFactory.decodeByteArray(imgArt, 0, imgArt.size)
        }else{ BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_foreground)}
        var notification = NotificationCompat.Builder(baseContext, AppClass.CHANNEL_ID)
            .setContentIntent(contentIntent)
            .setContentTitle(activity_player.musicListPA[activity_player.songPosition].title)
            .setSmallIcon(R.drawable.ic_music)
            .setContentText((activity_player.musicListPA[activity_player.songPosition].artist))
            .setLargeIcon(image)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.ic_back, "Previous",prevPendingIntent )
            .addAction(playPauseBtn, "Play", playPendingIntent)
            .addAction(R.drawable.ic_next, "Next", nextPendingIntent)
            .addAction(R.drawable.ic_exit, "Exit", exitPendingIntent)
            .build()
        startForeground(13, notification)
    }

}
