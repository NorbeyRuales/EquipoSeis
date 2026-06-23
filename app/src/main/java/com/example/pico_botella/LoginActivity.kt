package com.example.pico_botella

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pico_botella.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // UI-only activity; no auth/validation implemented per requirements.
    }
}
