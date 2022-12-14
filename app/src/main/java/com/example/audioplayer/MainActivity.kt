package com.example.audioplayer

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.audioplayer.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File
import java.lang.reflect.Type
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private  lateinit var toggle: ActionBarDrawerToggle
    private  lateinit var musicAdapter: MusicAdapter
    companion object {
        lateinit  var MusiListMA:ArrayList<Music>
        lateinit var  musicListSearch : ArrayList<Music>
        var search:Boolean=false
    }


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
if (requestRuntimePermission()){ init()
    Favorite.favoriteSongs= ArrayList()
    //for retrieving favourites data using shared preferences
    val editor = getSharedPreferences("FAVORITES", MODE_PRIVATE)
    val jsonString =editor.getString("FavoriteSongs",null)
    val typeToken = object : TypeToken<ArrayList<Music>>(){}.type

    if(jsonString != null){
        val data: ArrayList<Music> = GsonBuilder().create().fromJson(jsonString, typeToken)
        Favorite.favoriteSongs.addAll(data)
    }else{Toast.makeText(this,"empty",Toast.LENGTH_SHORT).show()}
    PlaylistActivity.musicPlaylist= MusicPlayList()
    //for retrieving favourites data using shared preferences
    val editorPlaylist = getSharedPreferences("FAVORITES", MODE_PRIVATE)
    val jsonStringPlayList =editorPlaylist.getString("MusicPlaylist",null)
    if(jsonStringPlayList != null){
        val dataPlalist: MusicPlayList = GsonBuilder().create().fromJson(jsonStringPlayList, MusicPlayList::class.java)
        PlaylistActivity.musicPlaylist = (dataPlalist)
    }else{Toast.makeText(this,"empty",Toast.LENGTH_SHORT).show()}

}


        binding.shuffleBTN.setOnClickListener{
            val intent=Intent(this@MainActivity,activity_player::class.java)
            intent.putExtra("index",0)
            intent.putExtra("class","MainActivity")
            startActivity(intent)
        }
        binding.favoriteBTN.setOnClickListener{
            startActivity(Intent(this@MainActivity,Favorite::class.java))
        }
        binding.playlistBTN.setOnClickListener{
            startActivity(Intent(this@MainActivity,PlaylistActivity::class.java))
        }

        binding.navView.setNavigationItemSelectedListener{
            when(it.itemId){
                R.id.About_item->startActivity(Intent(this@MainActivity,AboutActivity::class.java))
                R.id.Exit_item-> {
                    val builder = MaterialAlertDialogBuilder(this)
                    builder.setTitle("Exit")
                        .setMessage("Do you want to close app?")
                        .setPositiveButton("Yes"){ _, _ ->
                            exitApplication()
                        }
                        .setNegativeButton("No"){dialog, _ ->
                            dialog.dismiss()
                        }
                    val customDialog = builder.create()
                    customDialog.show()

                    setDialogBtnBackground(this, customDialog)
                }

            }
            true

        }
    }
    //For requesting permission
    private fun requestRuntimePermission():Boolean{
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),13)
            return false
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==13){if(grantResults.isNotEmpty()&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"Permission granted",Toast.LENGTH_SHORT).show()
            MusiListMA=getAllAudio()
        }

        else
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),13)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       if(toggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }
    @RequiresApi(Build.VERSION_CODES.R)
    private  fun init(){
        search=false
        setTheme(R.style.Theme_AudioPlayer)
        requestRuntimePermission()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MusiListMA=getAllAudio()
        toggle= ActionBarDrawerToggle(this,binding.root,R.string.open,R.string.close)
        binding.root.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.musicRecycler.setHasFixedSize(true)
        binding.musicRecycler.setItemViewCacheSize(13)
        binding.musicRecycler.layoutManager=LinearLayoutManager(this@MainActivity,)
        musicAdapter= MusicAdapter(this@MainActivity, MusiListMA)
        binding.musicRecycler.adapter=musicAdapter
        binding.totalSongs.text="Total Songs : "+musicAdapter.itemCount
    }
    @SuppressLint("Recycle", "Range")
    @RequiresApi(Build.VERSION_CODES.R)
    private fun getAllAudio(): ArrayList<Music>{
        val tempList = ArrayList<Music>()
        val selection = MediaStore.Audio.Media.IS_MUSIC +  " != 0"
        val projection = arrayOf(MediaStore.Audio.Media._ID,MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,MediaStore.Audio.Media.DURATION,MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID)
        val cursor = this.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,selection,null,null)
        if(cursor != null){
            if(cursor.moveToFirst())
                do {
                    val titleC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))?:"Unknown"
                    val idC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))?:"Unknown"
                    val albumC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))?:"Unknown"
                    val artistC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))?:"Unknown"
                    val pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val durationC = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                    val albumIdC = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)).toString()
                    val uri= Uri.parse( "content://media/external/audio/albumart")
                    val artUriC=Uri.withAppendedPath(uri,albumIdC).toString()
                    val music = Music(id = idC, title = titleC, album = albumC, artist = artistC, path = pathC, duration = durationC, artURI = artUriC)
                    val file = File(music.path)
                    if(file.exists())
                        tempList.add(music)
                }while (cursor.moveToNext())
            cursor.close()
        }
        return tempList
    }

    override fun onDestroy() {
        super.onDestroy()
        if(!activity_player.isPlaying && activity_player.musicService != null){
            exitApplication()
        }

        }



    override fun onResume() {
        super.onResume()
        //for storing favourites data using shared preferences
        val editor = getSharedPreferences("FAVORITES", MODE_PRIVATE).edit()
        val jsonString = GsonBuilder().create().toJson(Favorite.favoriteSongs)
        editor.putString("FavoriteSongs", jsonString)
        val jsonStringPlayList = GsonBuilder().create().toJson(PlaylistActivity.musicPlaylist)
        editor.putString("MusicPlaylist", jsonStringPlayList)
        editor.apply()
        if(jsonString == null)Toast.makeText(this,"empty",Toast.LENGTH_SHORT).show()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_view,menu)
        val searchView=menu?.findItem(R.id.searchView)?.actionView as androidx.appcompat.widget.SearchView
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean =true

            override fun onQueryTextChange(newText: String?): Boolean {
                musicListSearch= ArrayList()
            if(newText!=null){
                val userInput=newText.lowercase()
                for(song in MusiListMA)
                    if(song.title.lowercase().contains(userInput))
                        musicListSearch.add(song)
                search=true
                musicAdapter.updateMusicList(searchList = musicListSearch)
            }
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)

    }
    }
