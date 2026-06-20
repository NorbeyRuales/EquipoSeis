package com.example.pico_botella.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.pico_botella.model.Reto;
import com.example.pico_botella.repository.RetoRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RetosViewModel extends AndroidViewModel {

    private final RetoRepository repository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final MutableLiveData<List<Reto>> retos = new MutableLiveData<>();

    public RetosViewModel(@NonNull Application application) {
        super(application);
        repository = new RetoRepository(application);
        cargarRetos();
    }

    public LiveData<List<Reto>> getRetos() {
        return retos;
    }

    public void cargarRetos() {
        executor.execute(() -> retos.postValue(repository.obtenerRetos()));
    }

    public void agregarReto(Reto reto) {
        executor.execute(() -> {
            repository.agregarReto(reto);
            retos.postValue(repository.obtenerRetos());
        });
    }

    public void actualizarReto(Reto reto) {
        executor.execute(() -> {
            repository.actualizarReto(reto);
            retos.postValue(repository.obtenerRetos());
        });
    }

    public void eliminarReto(Reto reto) {
        executor.execute(() -> {
            repository.eliminarReto(reto);
            retos.postValue(repository.obtenerRetos());
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}
