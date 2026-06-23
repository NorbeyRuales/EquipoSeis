package com.example.pico_botella

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pico_botella.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var mediaPlayer: MediaPlayer
    var isPlaying = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupMusic()
    }

    private fun setupMusic() {
        mediaPlayer = MediaPlayer.create(this, R.raw.musicafondo)
        mediaPlayer.isLooping = true
        mediaPlayer.start()
    }

    fun toggleMusic() {
        if (isPlaying) {
            mediaPlayer.pause()
        } else {
            mediaPlayer.start()
        }
        isPlaying = !isPlaying
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }
}
