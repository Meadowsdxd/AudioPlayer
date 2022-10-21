package com.example.audioplayer

import android.media.MediaMetadataRetriever
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

data class Music(val id:String,val title:String,val album:String,val artist:String,val duration:Long=0,val path:String,val artURI:String)

    fun formatDuration(duration: Long):String{
        val minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
        val seconds = (TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS) -
                minutes*TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES))
        return String.format("%02d:%02d", minutes, seconds)

}
fun getImgArt(path:String): ByteArray? {
val retriviever=MediaMetadataRetriever()
    retriviever.setDataSource(path)
    return retriviever.embeddedPicture
}
fun exitApplication(){
    if(activity_player.musicService != null){
        activity_player.musicService!!.audioManager.abandonAudioFocus(activity_player.musicService)
        activity_player.musicService!!.stopForeground(true)
        activity_player.musicService!!.mediaPlayer!!.release()
        activity_player.musicService = null}
    exitProcess(1)
}
 fun setSongPosition(increment: Boolean){

     if(!activity_player.repeat){
         if(increment){
             if(activity_player.musicListPA.size-1== activity_player.songPosition) activity_player.songPosition =0 else ++activity_player.songPosition
         }else{  if(0== activity_player.songPosition) activity_player.songPosition = activity_player.musicListPA.size-1 else --activity_player.songPosition
         }
     }}
     fun favoriteChecker(id:String): Int{
         activity_player.isFavorite=false

        Favorite.favoriteSongs.forEachIndexed { index, music ->
            if(id==music.id){
                activity_player.isFavorite=true
                return index
            }
        }
         return -1
     }
