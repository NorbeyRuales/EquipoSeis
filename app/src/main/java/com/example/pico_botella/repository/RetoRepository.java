package com.example.pico_botella.repository;

import android.content.Context;

import com.example.pico_botella.data.RetoLocalDataSource;
import com.example.pico_botella.model.Reto;

import java.util.List;
import java.util.Random;

public class RetoRepository {

    private final RetoLocalDataSource localDataSource;
    private final Random random = new Random();

    public RetoRepository(Context context) {
        localDataSource = new RetoLocalDataSource(context);
    }

    public List<Reto> obtenerRetos() {
        return localDataSource.obtenerRetos();
    }

    public Reto obtenerRetoAleatorio() {
        List<Reto> retos = localDataSource.obtenerRetos();
        if (retos.isEmpty()) {
            return null;
        }

        return retos.get(random.nextInt(retos.size()));
    }

    public long agregarReto(Reto reto) {
        return localDataSource.agregarReto(reto);
    }

    public int actualizarReto(Reto reto) {
        return localDataSource.actualizarReto(reto);
    }

    public void eliminarReto(Reto reto) {
        localDataSource.eliminarReto(reto.getId());
    }
}
