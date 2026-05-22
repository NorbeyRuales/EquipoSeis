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
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.pico_botella.databinding.ActivityMainBinding
import com.example.pico_botella.dialog.RetoDialogFragment
import com.example.pico_botella.fragments.InstruccionesFragment
import com.example.pico_botella.fragments.RetosFragment
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var mediaPlayer: MediaPlayer // Cambiado a public para acceso desde Fragment
    private lateinit var heartbeatAnim: Animation
    private var spinMediaPlayer: MediaPlayer? = null

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

        // --- INSTRUCCIONES DEL JUEGO (HU 5.0) ---
        binding.toolbar.btnInstructions.setOnClickListener {
            val fragment = InstruccionesFragment()
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

        // --- CALIFICAR APLICACIÓN (HU 4.0) ---
        binding.toolbar.btnStar.setOnClickListener {
            rateApp()
        }

        // --- COMPARTIR APLICACIÓN (HU 10.0) ---
        binding.toolbar.btnShare.setOnClickListener {
            shareApp()
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
                girarBotella()
            }
        }
    }

    /**
     * Implementación de la HU 11.0: Girar la botella.
     */
    private fun girarBotella() {
        isGameRunning = true
        binding.btnStartGame.isEnabled = false
        
        // Pausar audio de fondo mientras se ejecuta el giro
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }

        // Reproducir sonido de botella girando (si existe el recurso)
        try {
            val resId = resources.getIdentifier("giro_botella", "raw", packageName)
            if (resId != 0) {
                spinMediaPlayer = MediaPlayer.create(this, resId)
                spinMediaPlayer?.start()
            }
        } catch (e: Exception) {
            // No se hace nada si falla o no existe el recurso de audio
        }

        // Duración aleatoria entre 3 y 5 segundos
        val spinDuration = Random.nextLong(3000, 5001)
        
        // Rotación final aleatoria. Empezando desde la posición actual.
        val extraRotation = Random.nextInt(1080, 2160).toFloat()
        val targetRotation = binding.ivBottle.rotation + extraRotation

        binding.ivBottle.animate()
            .rotation(targetRotation)
            .setDuration(spinDuration)
            .setInterpolator(DecelerateInterpolator())
            .withEndAction {
                stopSpinSound()
                startPostSpinCountdown()
            }
            .start()
    }

    private fun stopSpinSound() {
        spinMediaPlayer?.stop()
        spinMediaPlayer?.release()
        spinMediaPlayer = null
    }

    /**
     * Mostrar contador (3 a 0) al detenerse la botella.
     */
    private fun startPostSpinCountdown() {
        binding.tvCountdown.visibility = View.VISIBLE
        val handler = Handler(Looper.getMainLooper())
        
        var count = 3
        val runnable = object : Runnable {
            override fun run() {
                if (count >= 0) {
                    binding.tvCountdown.text = count.toString()
                    count--
                    handler.postDelayed(this, 1000)
                } else {
                    binding.tvCountdown.visibility = View.GONE
                    openHU12Integration()
                }
            }
        }
        handler.post(runnable)
    }

    /**
     * Implementación de la HU 12: Mostrar Reto Aleatorio.
     */
    private fun openHU12Integration() {
        val dialog = RetoDialogFragment.newInstance()
        dialog.setOnDismissListener {
            resetGameState()
        }
        dialog.show(supportFragmentManager, RetoDialogFragment.TAG)
    }

    /**
     * Restablece el estado para permitir una nueva partida.
     */
    private fun resetGameState() {
        isGameRunning = false
        binding.btnStartGame.isEnabled = true
        binding.btnStartGame.startAnimation(heartbeatAnim)
        
        // El audio de fondo vuelve a su estado anterior
        if (isPlaying) {
            mediaPlayer.start()
        }
    }

    /**
     * Implementación de la HU 10.0: Compartir aplicación.
     */
    private fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            val shareMessage = "App Pico Botella\n\n" +
                    "Solo los valientes lo juegan !!\n\n" +
                    "https://play.google.com/store/apps/details?id=com.nequi.MobileApp&hl=es_419&gl=es"
            putExtra(Intent.EXTRA_TEXT, shareMessage)
        }
        startActivity(Intent.createChooser(shareIntent, "Compartir usando:"))
    }

    /**
     * Implementación de la HU 4.0: Calificar la aplicación.
     */
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

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
        stopSpinSound()
    }
}
