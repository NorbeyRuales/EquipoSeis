package com.example.pico_botella.dialog;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import com.example.pico_botella.R;
import com.example.pico_botella.database.RetosDatabaseHelper;
import com.example.pico_botella.fragments.RetosFragment;
import com.example.pico_botella.model.Reto;

public class AgregarRetoDialog extends DialogFragment {

    private RetosFragment parentFragment;
    private RetosDatabaseHelper dbHelper;

    public AgregarRetoDialog(RetosFragment fragment) {
        this.parentFragment = fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_reto, null);

        dbHelper = new RetosDatabaseHelper(getContext());

        EditText etDescripcion = view.findViewById(R.id.etDescripcion);
        Button btnCancel = view.findViewById(R.id.btnCancel);
        Button btnSave = view.findViewById(R.id.btnSave);

        // Criterio 7: No cerrar al tocar fuera
        setCancelable(false);

        btnCancel.setOnClickListener(v -> dismiss());

        // Criterio 5: Habilitar/Deshabilitar botón Guardar
        etDescripcion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean hasText = s.toString().trim().length() > 0;
                btnSave.setEnabled(hasText);
                
                // Cambiar color según estado
                if (hasText) {
                    btnSave.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.orange)));
                } else {
                    btnSave.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnSave.setOnClickListener(v -> {
            String desc = etDescripcion.getText().toString().trim();
            if (!desc.isEmpty()) {
                // Criterio 6: Guardar en SQLite y listar inmediatamente
                dbHelper.agregarReto(new Reto(desc));
                if (parentFragment != null) {
                    parentFragment.actualizarLista();
                }
                dismiss();
            }
        });

        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false); // Refuerzo Criterio 7
        return dialog;
    }
}
