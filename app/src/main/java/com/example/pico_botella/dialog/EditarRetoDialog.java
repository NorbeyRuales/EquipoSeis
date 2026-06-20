package com.example.pico_botella.dialog;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import com.example.pico_botella.R;
import com.example.pico_botella.model.Reto;

public class EditarRetoDialog extends DialogFragment {

    private final Reto reto;
    private final OnRetoSaveListener listener;

    public interface OnRetoSaveListener {
        void onSave(Reto reto);
    }

    public EditarRetoDialog(Reto reto, OnRetoSaveListener listener) {
        this.reto = reto;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_reto_form, null);

        TextView tvTitle = view.findViewById(R.id.tvDialogTitle);
        EditText etDescripcion = view.findViewById(R.id.etDescripcion);
        Button btnCancel = view.findViewById(R.id.btnCancel);
        Button btnSave = view.findViewById(R.id.btnSave);

        // Configuración inicial según HU 8.0
        tvTitle.setText("Editar reto"); // Criterio 2
        etDescripcion.setText(reto.getDescripcion()); // Criterio 3
        
        // Habilitar botón guardar inicialmente ya que hay datos
        btnSave.setEnabled(true);
        btnSave.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.orange)));

        // Criterio 7: No cerrar al tocar fuera
        setCancelable(false);

        btnCancel.setOnClickListener(v -> dismiss());

        // Validación dinámica (Criterio similar al de Agregar)
        etDescripcion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean hasText = s.toString().trim().length() > 0;
                btnSave.setEnabled(hasText);
                if (hasText) {
                    btnSave.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.orange)));
                } else {
                    btnSave.setBackgroundTintList(ColorStateList.valueOf(android.graphics.Color.GRAY));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnSave.setOnClickListener(v -> {
            String desc = etDescripcion.getText().toString().trim();
            if (!desc.isEmpty()) {
                reto.setDescripcion(desc);
                listener.onSave(reto);
                dismiss();
            }
        });

        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
