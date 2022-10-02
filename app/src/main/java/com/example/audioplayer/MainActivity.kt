package com.example.audioplayer

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.audioplayer.databinding.ActivityMainBinding
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private  lateinit var toggle: ActionBarDrawerToggle
    private  lateinit var musicAdapter: MusicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()

        binding.shuffleBTN.setOnClickListener{
             startActivity( Intent(this@MainActivity,activity_player::class.java))
        }
        binding.favoriteBTN.setOnClickListener{
            startActivity(Intent(this@MainActivity,Favorite::class.java))
        }
        binding.playlistBTN.setOnClickListener{
            startActivity(Intent(this@MainActivity,PlaylistActivity::class.java))
        }

        binding.navView.setNavigationItemSelectedListener{
            when(it.itemId){
                R.id.Feedback_item->Toast.makeText(this@MainActivity,"Feed",Toast.LENGTH_SHORT).show()
                R.id.About_item->Toast.makeText(this@MainActivity,"About_item",Toast.LENGTH_SHORT).show()
                R.id.Settings_item->Toast.makeText(this@MainActivity,"Settings_item",Toast.LENGTH_SHORT).show()
                R.id.Exit_item-> exitProcess(1)

            }
            true

        }
    }
    //For requesting permission
    private fun requestRuntimePermission(){
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),13)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==13){if(grantResults.isNotEmpty()&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
        Toast.makeText(this,"Permission granted",Toast.LENGTH_SHORT).show()
        else
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),13)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       if(toggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }
    private  fun init(){
        setTheme(R.style.Theme_AudioPlayer)
        requestRuntimePermission()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val musicList= ArrayList<String>()
        musicList.add("1 song")
        musicList.add("2 song")
        musicList.add("3 song")
        musicList.add("4 song")
        toggle= ActionBarDrawerToggle(this,binding.root,R.string.open,R.string.close)
        binding.root.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.musicRecycler.setHasFixedSize(true)
        binding.musicRecycler.setItemViewCacheSize(13)
        binding.musicRecycler.layoutManager=LinearLayoutManager(this@MainActivity,)
        musicAdapter= MusicAdapter(this@MainActivity,musicList)
        binding.musicRecycler.adapter=musicAdapter
    }
}