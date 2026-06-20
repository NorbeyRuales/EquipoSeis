package com.example.pico_botella.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pico_botella.MainActivity;
import com.example.pico_botella.R;
import com.example.pico_botella.adapter.RetosAdapter;
import com.example.pico_botella.dialog.AgregarRetoDialog;
import com.example.pico_botella.dialog.EditarRetoDialog;
import com.example.pico_botella.dialog.EliminarRetoDialog;
import com.example.pico_botella.model.Reto;
import com.example.pico_botella.viewmodel.RetosViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.Collections;

public class RetosFragment extends Fragment implements RetosAdapter.OnRetoClickListener {

    private RecyclerView rvRetos;
    private RetosAdapter adapter;
    private RetosViewModel viewModel;
    private FloatingActionButton fabAdd;
    private Toolbar toolbarRetos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_retos, container, false);

        rvRetos = view.findViewById(R.id.rvRetos);
        fabAdd = view.findViewById(R.id.fabAdd);
        toolbarRetos = view.findViewById(R.id.toolbarRetos);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(RetosViewModel.class);
        setupRecyclerView();
        observeViewModel();
        fabAdd.setOnClickListener(v -> abrirDialogoAgregar());

        // Criterio 1: Pausar audio al entrar
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            if (activity.mediaPlayer != null && activity.mediaPlayer.isPlaying()) {
                activity.mediaPlayer.pause();
            }
        }

        // Criterio 3: Toolbar con flecha atrás y restablecer audio
        toolbarRetos.setNavigationOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                MainActivity activity = (MainActivity) getActivity();
                if (activity.isPlaying()) {
                    activity.mediaPlayer.start();
                }
            }
            getParentFragmentManager().popBackStack();
        });
    }

    private void setupRecyclerView() {
        adapter = new RetosAdapter(Collections.emptyList(), this);
        rvRetos.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRetos.setAdapter(adapter);
    }

    private void observeViewModel() {
        viewModel.getRetos().observe(getViewLifecycleOwner(), retos -> {
            adapter.setRetos(retos);
            // Criterio 6: Asegurar que el scroll vaya al inicio para ver el nuevo item
            rvRetos.scrollToPosition(0);
        });
    }

    private void abrirDialogoAgregar() {
        AgregarRetoDialog dialog = new AgregarRetoDialog(reto -> viewModel.agregarReto(reto));
        dialog.show(getParentFragmentManager(), "AgregarRetoDialog");
    }

    @Override
    public void onEditClick(Reto reto) {
        EditarRetoDialog dialog = new EditarRetoDialog(reto, updatedReto -> viewModel.actualizarReto(updatedReto));
        dialog.show(getParentFragmentManager(), "EditarRetoDialog");
    }

    @Override
    public void onDeleteClick(Reto reto) {
        // Criterio 10: Lanzar cuadro de diálogo de eliminar reto
        EliminarRetoDialog dialog = new EliminarRetoDialog(reto, retoToDelete -> viewModel.eliminarReto(retoToDelete));
        dialog.show(getParentFragmentManager(), "EliminarRetoDialog");
    }
}
