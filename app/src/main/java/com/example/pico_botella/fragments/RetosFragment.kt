package com.example.pico_botella.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pico_botella.MainActivity
import com.example.pico_botella.adapter.RetosAdapter
import com.example.pico_botella.databinding.FragmentRetosBinding
import com.example.pico_botella.dialog.AgregarRetoDialog
import com.example.pico_botella.dialog.EditarRetoDialog
import com.example.pico_botella.dialog.EliminarRetoDialog
import com.example.pico_botella.model.Reto
import com.example.pico_botella.viewmodel.RetosViewModel

class RetosFragment : Fragment(), RetosAdapter.OnRetoClickListener {

    private var _binding: FragmentRetosBinding? = null
    private val binding get() = _binding!!
    
    // Al usar by viewModels(), Kotlin genera automáticamente un getViewModel() interno
    val viewModel: RetosViewModel by viewModels()
    private lateinit var adapter: RetosAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRetosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupToolbar()

        binding.fabAdd.setOnClickListener {
            val dialog = AgregarRetoDialog(this)
            dialog.show(parentFragmentManager, "AgregarRetoDialog")
        }

        viewModel.allRetos.observe(viewLifecycleOwner) { retos ->
            adapter.setRetos(retos)
        }

        // Criterio 1: Pausar audio al entrar
        (activity as? MainActivity)?.let {
            if (it.mediaPlayer.isPlaying) {
                it.mediaPlayer.pause()
            }
        }
    }

    private fun setupToolbar() {
        binding.toolbarRetos.setNavigationOnClickListener {
            // Criterio 3: Restablecer audio al salir
            (activity as? MainActivity)?.let {
                if (it.isPlaying) {
                    it.mediaPlayer.start()
                }
            }
            findNavController().popBackStack()
        }
    }

    private fun setupRecyclerView() {
        adapter = RetosAdapter(emptyList(), this)
        binding.rvRetos.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@RetosFragment.adapter
        }
    }

    override fun onEditClick(reto: Reto) {
        val dialog = EditarRetoDialog(reto, this)
        dialog.show(parentFragmentManager, "EditarRetoDialog")
    }

    override fun onDeleteClick(reto: Reto) {
        val dialog = EliminarRetoDialog(reto, this)
        dialog.show(parentFragmentManager, "EliminarRetoDialog")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
