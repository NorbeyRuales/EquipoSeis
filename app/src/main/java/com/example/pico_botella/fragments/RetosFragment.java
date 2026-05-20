package com.example.pico_botella.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pico_botella.R;
import com.example.pico_botella.adapter.RetosAdapter;
import com.example.pico_botella.database.RetosDatabaseHelper;
import com.example.pico_botella.dialog.AgregarRetoDialog;
import com.example.pico_botella.dialog.EditarRetoDialog;
import com.example.pico_botella.model.Reto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class RetosFragment extends Fragment implements RetosAdapter.OnRetoClickListener {

    private RecyclerView rvRetos;
    private RetosAdapter adapter;
    private RetosDatabaseHelper dbHelper;
    private FloatingActionButton fabAdd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_retos, container, false);

        rvRetos = view.findViewById(R.id.rvRetos);
        fabAdd = view.findViewById(R.id.fabAdd);
        dbHelper = new RetosDatabaseHelper(getContext());

        setupRecyclerView();

        fabAdd.setOnClickListener(v -> abrirDialogoAgregar());

        return view;
    }

    private void setupRecyclerView() {
        List<Reto> listaRetos = dbHelper.obtenerRetos();
        adapter = new RetosAdapter(listaRetos, this);
        rvRetos.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRetos.setAdapter(adapter);
    }

    public void actualizarLista() {
        if (adapter != null && dbHelper != null) {
            adapter.setRetos(dbHelper.obtenerRetos());
        }
    }

    private void abrirDialogoAgregar() {
        AgregarRetoDialog dialog = new AgregarRetoDialog(this);
        dialog.show(getParentFragmentManager(), "AgregarRetoDialog");
    }

    @Override
    public void onEditClick(Reto reto) {
        EditarRetoDialog dialog = new EditarRetoDialog(reto, this);
        dialog.show(getParentFragmentManager(), "EditarRetoDialog");
    }

    @Override
    public void onDeleteClick(Reto reto) {
        dbHelper.eliminarReto(reto.getId());
        actualizarLista();
    }
}
