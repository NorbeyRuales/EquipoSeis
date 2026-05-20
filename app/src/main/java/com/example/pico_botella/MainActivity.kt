package com.example.pico_botella

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.pico_botella.databinding.ActivityMainBinding
import com.example.pico_botella.fragments.RetosFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var mediaPlayer: MediaPlayer // Cambiado a public (internal by default in Kotlin) para acceso desde Fragment
    private lateinit var heartbeatAnim: Animation

    var isPlaying = true // Cambiado a public para acceso desde Fragment
    private var isGameRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mediaPlayer = MediaPlayer.create(this, R.raw.musicafondo)
        mediaPlayer.isLooping = true
        mediaPlayer.start()

        binding.toolbar.btnAudio.setOnClickListener {
            toggleMusic()
        }

        binding.toolbar.btnInstructions.setOnClickListener {
            showRulesDialog()
        }

        // CALIFICAR APLICACIÓN
        binding.toolbar.btnStar.setOnClickListener {
            rateApp()
        }

        // --- CONEXIÓN BOTÓN "+" ---
        binding.toolbar.btnAddChallenge.setOnClickListener {
            val fragment = RetosFragment()
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out,
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
                )
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        startHeartbeatAnimation()

        binding.btnStartGame.setOnClickListener {
            if (!isGameRunning) {
                binding.btnStartGame.clearAnimation() // 🛑 se detiene al presionar
                startCountdown()
            }
        }
    }

    /*Calificar la aplicación.*/
    private fun rateApp() {
        val packageName = "com.nequi.MobileApp"
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName"))
            startActivity(intent)
        }
    }

    private fun startHeartbeatAnimation() {
        heartbeatAnim = AnimationUtils.loadAnimation(this, R.anim.heartbeat)
        binding.btnStartGame.startAnimation(heartbeatAnim)
    }

    private fun startCountdown() {
        isGameRunning = true

        binding.tvCountdown.visibility = View.VISIBLE

        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({ binding.tvCountdown.text = "3" }, 0)
        handler.postDelayed({ binding.tvCountdown.text = "2" }, 1000)
        handler.postDelayed({ binding.tvCountdown.text = "1" }, 2000)

        handler.postDelayed({
            binding.tvCountdown.visibility = View.GONE
            startGame()
        }, 3000)
    }

    private fun startGame() {
        isGameRunning = false
    }

    private fun toggleMusic() {
        if (isPlaying) {
            mediaPlayer.pause()
            binding.toolbar.btnAudio.setImageResource(
                android.R.drawable.ic_lock_silent_mode
            )
        } else {
            mediaPlayer.start()
            binding.toolbar.btnAudio.setImageResource(
                android.R.drawable.ic_lock_silent_mode_off
            )
        }
        isPlaying = !isPlaying
    }

    private fun showRulesDialog() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Reglas del Pico Botella")

        builder.setMessage(
            "🎯 Cómo jugar:\n\n" +
                    "1. Girar la botella\n" +
                    "2. Elegir jugador\n" +
                    "3. Cumplir reto\n\n" +
                    "🔥 Diviértete"
        )

        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (::mediaPlayer.isInitialized) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }
}
