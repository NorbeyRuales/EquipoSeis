package com.example.pico_botella

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.pico_botella.databinding.ActivitySplashBinding
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        auth = FirebaseAuth.getInstance()

        Glide.with(this)
            .asGif()
            .load(R.drawable.splashanim)
            .into(binding.ivBottleSplash)

        Handler(Looper.getMainLooper()).postDelayed({
            checkUserSession()
        }, 5000)
    }

    private fun checkUserSession() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Usuario ya autenticado, saltar login
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            // No hay sesión activa, ir a login
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }
}
