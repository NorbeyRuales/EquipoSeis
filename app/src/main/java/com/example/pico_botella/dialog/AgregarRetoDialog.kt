package com.example.pico_botella.dialog

import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.pico_botella.R
import com.example.pico_botella.fragments.RetosFragment
import com.example.pico_botella.model.Reto

class AgregarRetoDialog(private val parentFragment: RetosFragment) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_reto_form, null)

        val etDescripcion = view.findViewById<EditText>(R.id.etDescripcion)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        val btnSave = view.findViewById<Button>(R.id.btnSave)

        isCancelable = false

        btnCancel.setOnClickListener { dismiss() }

        etDescripcion.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val hasText = s.toString().trim().isNotEmpty()
                btnSave.isEnabled = hasText
                if (hasText) {
                    btnSave.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.orange))
                } else {
                    btnSave.backgroundTintList = ColorStateList.valueOf(Color.GRAY)
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        btnSave.setOnClickListener {
            val desc = etDescripcion.text.toString().trim()
            if (desc.isNotEmpty()) {
                parentFragment.viewModel.insert(Reto(descripcion = desc))
                dismiss()
            }
        }

        builder.setView(view)
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }
}
