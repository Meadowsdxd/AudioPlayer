package com.example.audioplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.audioplayer.databinding.ActivityPlayerBinding
import com.example.audioplayer.databinding.ActivitySelectionBinding

class SelectionActivity : AppCompatActivity() {
    lateinit var binding: ActivitySelectionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)
        binding = ActivitySelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}