package com.example.audioplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.audioplayer.databinding.ActivityFeedbackBinding
import com.example.audioplayer.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPinkNav)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title="Settings"
    }
}