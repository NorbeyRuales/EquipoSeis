package com.example.pico_botella.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.pico_botella.databinding.DialogRetoBinding
import com.example.pico_botella.viewmodel.GameViewModel

class RetoDialogFragment : DialogFragment() {

    private var _binding: DialogRetoBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: GameViewModel
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
        
        // Ajustar el diálogo para que ocupe el ancho deseado y sea transparente de fondo
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        
        isCancelable = false // No se cierra al tocar fuera

        viewModel = ViewModelProvider(requireActivity())[GameViewModel::class.java]

        observeChallenge()
        viewModel.cargarRetoConPokemonAleatorio()

        binding.btnClose.setOnClickListener {
            dismiss()
            onDismissListener?.invoke()
        }
    }

    private fun observeChallenge() {
        viewModel.challengeState.observe(viewLifecycleOwner) { state ->
            when {
                state.isLoading -> {
                    binding.tvRetoDescription.text = "Cargando reto..."
                    binding.pbLoading.visibility = View.VISIBLE
                }
                state.isEmpty -> {
                    binding.tvRetoDescription.text = "¡No hay retos guardados! Agrega algunos en el menú +."
                    binding.pbLoading.visibility = View.GONE
                    binding.ivPokemon.setImageResource(android.R.drawable.ic_dialog_alert)
                }
                state.reto != null -> {
                    binding.tvRetoDescription.text = state.reto.descripcion
                    binding.pbLoading.visibility = View.GONE

                    if (state.pokemonImageUrl != null) {
                        Glide.with(this@RetoDialogFragment)
                            .load(state.pokemonImageUrl)
                            .into(binding.ivPokemon)
                    } else {
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
