package com.example.pico_botella.dialog

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.pico_botella.R
import com.example.pico_botella.model.Reto
import com.example.pico_botella.viewmodel.RetosViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EliminarRetoDialog(private val reto: Reto) : DialogFragment() {

    private val viewModel: RetosViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_eliminar, null)

        val tvDescripcion = view.findViewById<TextView>(R.id.tvDescripcionReto)
        val btnNo = view.findViewById<Button>(R.id.btnNo)
        val btnSi = view.findViewById<Button>(R.id.btnSi)

        tvDescripcion.text = reto.descripcion

        isCancelable = false

        btnNo.setOnClickListener { dismiss() }

        btnSi.setOnClickListener {
            viewModel.delete(reto)
            dismiss()
        }

        builder.setView(view)
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }
}
