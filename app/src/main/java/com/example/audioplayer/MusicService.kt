package com.example.audioplayer

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.media.MediaSession2
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat


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
    fun showNotification(){
        val intent = Intent(baseContext, MainActivity::class.java)

        val contentIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val notification = androidx.core.app.NotificationCompat.Builder(baseContext, AppClass.CHANNEL_ID)
            .setContentIntent(contentIntent)
            .setContentTitle(activity_player.musicListPA[activity_player.songPosition].title)
            .setContentText(activity_player.musicListPA[activity_player.songPosition].artist)
            .setSmallIcon(R.drawable.ic_music)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_foreground))
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken))
            .setPriority(androidx.core.app.NotificationCompat.PRIORITY_HIGH)
            .setVisibility(androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.ic_back, "Previous", null)
            .addAction(R.drawable.ic_pause, "Play", null)
            .addAction(R.drawable.ic_next, "Next", null)
            .addAction(R.drawable.ic_exit, "Exit", null)
            .build()
        startForeground(13, notification)
    }
}
