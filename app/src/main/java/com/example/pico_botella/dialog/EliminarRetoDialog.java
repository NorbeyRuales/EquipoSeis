package com.example.pico_botella.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.example.pico_botella.R;
import com.example.pico_botella.database.RetosDatabaseHelper;
import com.example.pico_botella.fragments.RetosFragment;
import com.example.pico_botella.model.Reto;

public class EliminarRetoDialog extends DialogFragment {

    private Reto reto;
    private RetosFragment parentFragment;
    private RetosDatabaseHelper dbHelper;

    public EliminarRetoDialog(Reto reto, RetosFragment fragment) {
        this.reto = reto;
        this.parentFragment = fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_eliminar, null);

        dbHelper = new RetosDatabaseHelper(getContext());

        TextView tvDescripcion = view.findViewById(R.id.tvDescripcionReto);
        Button btnNo = view.findViewById(R.id.btnNo);
        Button btnSi = view.findViewById(R.id.btnSi);

        // Criterio 3: Mostrar la descripción del reto a eliminar
        if (reto != null) {
            tvDescripcion.setText(reto.getDescripcion());
        }

        // Criterio 6: No permitir cerrar al tocar fuera
        setCancelable(false);

        // Criterio 4: Botón "NO" cierra el diálogo
        btnNo.setOnClickListener(v -> dismiss());

        // Criterio 5: Botón "SI" elimina de SQLite y actualiza lista
        btnSi.setOnClickListener(v -> {
            if (reto != null) {
                dbHelper.eliminarReto(reto.getId());
                if (parentFragment != null) {
                    parentFragment.actualizarLista();
                }
            }
            dismiss();
        });

        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
