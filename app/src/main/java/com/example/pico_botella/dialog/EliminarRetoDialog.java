package com.example.pico_botella.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_eliminar, null);

        dbHelper = new RetosDatabaseHelper(getContext());

        Button btnCancel = view.findViewById(R.id.btnCancelarEliminar);
        Button btnConfirm = view.findViewById(R.id.btnConfirmarEliminar);

        btnCancel.setOnClickListener(v -> dismiss());

        btnConfirm.setOnClickListener(v -> {
            dbHelper.eliminarReto(reto.getId());
            if (parentFragment != null) {
                parentFragment.actualizarLista();
            }
            dismiss();
        });

        builder.setView(view);
        return builder.create();
    }
}
