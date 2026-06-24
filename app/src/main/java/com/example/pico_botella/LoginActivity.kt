package com.example.pico_botella

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pico_botella.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        setupListeners()
    }

    override fun onStart() {
        super.onStart()
        // Criterio 1: Si el usuario ya está autenticado, saltar directamente a la pantalla principal
        if (auth.currentUser != null) {
            startMainActivity()
        }
    }

    private fun setupListeners() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateFields()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        binding.etEmail.addTextChangedListener(textWatcher)
        binding.etPassword.addTextChangedListener(textWatcher)

        binding.btnLogin.setOnClickListener {
            login()
        }

        binding.tvRegister.setOnClickListener {
            register()
        }
    }

    private fun login() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        binding.btnLogin.isEnabled = false
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startMainActivity()
                } else {
                    Toast.makeText(this, "Login incorrecto", Toast.LENGTH_SHORT).show()
                    validateFields()
                }
            }
    }

    private fun register() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        binding.tvRegister.isEnabled = false
        binding.tvRegister.isClickable = false
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Criterio 1: Al registrarse también se debe iniciar sesión y entrar a la app
                    startMainActivity()
                } else {
                    Toast.makeText(this, "Error en el registro", Toast.LENGTH_SHORT).show()
                    validateFields()
                }
            }
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun validateFields() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (password.isNotEmpty() && password.length < 6) {
            binding.tilPassword.error = "Mínimo 6 dígitos"
            binding.tilPassword.isErrorEnabled = true
        } else {
            binding.tilPassword.error = null
            binding.tilPassword.isErrorEnabled = false
        }

        val isEnabled = email.isNotEmpty() && password.length >= 6
        binding.btnLogin.isEnabled = isEnabled
        binding.tvRegister.isEnabled = isEnabled
        binding.tvRegister.isClickable = isEnabled

        if (isEnabled) {
            binding.btnLogin.setTextColor(Color.WHITE)
            binding.btnLogin.setTypeface(null, Typeface.BOLD)
            binding.tvRegister.setTextColor(Color.WHITE)
            binding.tvRegister.setTypeface(null, Typeface.BOLD)
        } else {
            binding.btnLogin.setTextColor(Color.parseColor("#88FFFFFF"))
            binding.btnLogin.setTypeface(null, Typeface.NORMAL)
            binding.tvRegister.setTextColor(Color.parseColor("#9EA1A1"))
            binding.tvRegister.setTypeface(null, Typeface.NORMAL)
        }
    }
}
