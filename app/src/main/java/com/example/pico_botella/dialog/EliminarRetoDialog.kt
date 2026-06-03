package com.example.pico_botella.dialog

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.pico_botella.R
import com.example.pico_botella.fragments.RetosFragment
import com.example.pico_botella.model.Reto

class EliminarRetoDialog(private val reto: Reto, private val parentFragment: RetosFragment) : DialogFragment() {

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
            parentFragment.viewModel.delete(reto)
            dismiss()
        }

        builder.setView(view)
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }
}
