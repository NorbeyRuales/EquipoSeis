package com.example.pico_botella.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.example.pico_botella.R;
import com.example.pico_botella.database.RetosDatabaseHelper;
import com.example.pico_botella.fragments.RetosFragment;
import com.example.pico_botella.model.Reto;

public class EditarRetoDialog extends DialogFragment {

    private Reto reto;
    private RetosFragment parentFragment;
    private RetosDatabaseHelper dbHelper;

    public EditarRetoDialog(Reto reto, RetosFragment fragment) {
        this.reto = reto;
        this.parentFragment = fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_reto, null);

        dbHelper = new RetosDatabaseHelper(getContext());

        TextView tvTitle = view.findViewById(R.id.tvDialogTitle);
        EditText etDescripcion = view.findViewById(R.id.etDescripcion);
        Button btnCancel = view.findViewById(R.id.btnCancel);
        Button btnSave = view.findViewById(R.id.btnSave);

        tvTitle.setText("Editar Reto");
        etDescripcion.setText(reto.getDescripcion());

        btnCancel.setOnClickListener(v -> dismiss());

        btnSave.setOnClickListener(v -> {
            String desc = etDescripcion.getText().toString().trim();
            if (!desc.isEmpty()) {
                reto.setDescripcion(desc);
                dbHelper.actualizarReto(reto);
                if (parentFragment != null) {
                    parentFragment.actualizarLista();
                }
                dismiss();
            } else {
                Toast.makeText(getContext(), "La descripción no puede estar vacía", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setView(view);
        return builder.create();
    }
}
