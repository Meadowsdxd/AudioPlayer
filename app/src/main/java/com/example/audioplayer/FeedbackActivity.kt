package com.example.audioplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.audioplayer.databinding.ActivityFeedbackBinding
import com.example.audioplayer.databinding.ActivityPlayerBinding

class FeedbackActivity : AppCompatActivity() {

    lateinit var binding: ActivityFeedbackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPinkNav)
        binding = ActivityFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title="Feedback"
    }
}