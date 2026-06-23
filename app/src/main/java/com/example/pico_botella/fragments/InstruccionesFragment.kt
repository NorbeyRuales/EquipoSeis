package com.example.pico_botella.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.example.pico_botella.MainActivity
import com.example.pico_botella.R
import com.example.pico_botella.databinding.FragmentInstruccionesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InstruccionesFragment : Fragment() {

    private var _binding: FragmentInstruccionesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInstruccionesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Criterio 1: Pausar audio al entrar si está en reproducción
        (activity as? MainActivity)?.let { mainActivity ->
            if (mainActivity.mediaPlayer.isPlaying) {
                mainActivity.mediaPlayer.pause()
            }
        }

        // Criterio 3: Toolbar con flecha atrás y restablecer audio
        binding.toolbarInstructions.setNavigationOnClickListener {
            restoreAudioAndGoBack()
        }

        // Criterio 8: Animación de triunfo
        val pulseAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.heartbeat)
        binding.ivTriumph.startAnimation(pulseAnim)
    }

    private fun restoreAudioAndGoBack() {
        (activity as? MainActivity)?.let { mainActivity ->
            if (mainActivity.isPlaying) {
                mainActivity.mediaPlayer.start()
            }
        }
        parentFragmentManager.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
