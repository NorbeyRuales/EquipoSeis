package com.example.pico_botella.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.pico_botella.databinding.DialogRetoBinding
import com.example.pico_botella.viewmodel.GameViewModel

class RetoDialogFragment : DialogFragment() {

    private var _binding: DialogRetoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GameViewModel by activityViewModels()
    private var onDismissListener: (() -> Unit)? = null

    fun setOnDismissListener(listener: () -> Unit) {
        onDismissListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
        }
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

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false

        observeChallenge()
        viewModel.cargarRetoConPokemonAleatorio()

        binding.btnClose.setOnClickListener {
            dismiss()
            onDismissListener?.invoke()
        }
    }

    override fun onStart() {
        super.onStart()
        val dialogWidth = (resources.displayMetrics.widthPixels * 0.92).toInt()
        dialog?.window?.apply {
            setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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
                        Glide.with(this)
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
