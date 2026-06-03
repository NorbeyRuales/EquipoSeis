package com.example.pico_botella.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.pico_botella.database.AppDatabase
import com.example.pico_botella.databinding.DialogRetoBinding
import com.example.pico_botella.model.Reto
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import org.json.JSONObject
import java.net.URL
import kotlin.random.Random

class RetoDialogFragment : DialogFragment() {

    private var _binding: DialogRetoBinding? = null
    private val binding get() = _binding!!
    private var onDismissListener: (() -> Unit)? = null

    fun setOnDismissListener(listener: () -> Unit) {
        onDismissListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogRetoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        
        isCancelable = false

        setupReto()
        fetchPokemonImage()

        binding.btnClose.setOnClickListener {
            dismiss()
            onDismissListener?.invoke()
        }
    }

    private fun setupReto() {
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext(), lifecycleScope)
            val retos = db.retoDao().getAllRetos().first()
            
            withContext(Dispatchers.Main) {
                if (retos.isNotEmpty()) {
                    val retoAleatorio = retos[Random.nextInt(retos.size)]
                    binding.tvRetoDescription.text = retoAleatorio.descripcion
                } else {
                    binding.tvRetoDescription.text = "¡No hay retos guardados! Agrega algunos en el menú +."
                }
            }
        }
    }

    private fun fetchPokemonImage() {
        binding.pbLoading.visibility = View.VISIBLE
        
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = URL("https://raw.githubusercontent.com/Biuni/PokemonGO-Pokedex/master/pokedex.json").readText()
                val jsonObject = JSONObject(response)
                val pokemonArray = jsonObject.getJSONArray("pokemon")
                val randomPokemon = pokemonArray.getJSONObject(Random.nextInt(pokemonArray.length()))
                var imageUrl = randomPokemon.getString("img")
                
                if (imageUrl.startsWith("http://")) {
                    imageUrl = imageUrl.replace("http://", "https://")
                }

                withContext(Dispatchers.Main) {
                    if (_binding != null) {
                        Glide.with(this@RetoDialogFragment)
                            .load(imageUrl)
                            .into(binding.ivPokemon)
                        binding.pbLoading.visibility = View.GONE
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    if (_binding != null) {
                        binding.pbLoading.visibility = View.GONE
                        binding.ivPokemon.setImageResource(android.R.drawable.ic_dialog_alert)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "RetoDialogFragment"
        fun newInstance() = RetoDialogFragment()
    }
}
