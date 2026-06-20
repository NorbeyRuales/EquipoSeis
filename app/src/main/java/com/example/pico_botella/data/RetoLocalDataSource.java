package com.example.pico_botella.data;

import android.content.Context;

import com.example.pico_botella.database.RetosDatabaseHelper;
import com.example.pico_botella.model.Reto;

import java.util.List;

public class RetoLocalDataSource {

    private final RetosDatabaseHelper dbHelper;

    public RetoLocalDataSource(Context context) {
        dbHelper = new RetosDatabaseHelper(context.getApplicationContext());
    }

    public List<Reto> obtenerRetos() {
        return dbHelper.obtenerRetos();
    }

    public long agregarReto(Reto reto) {
        return dbHelper.agregarReto(reto);
    }

    public int actualizarReto(Reto reto) {
        return dbHelper.actualizarReto(reto);
    }

    public void eliminarReto(int retoId) {
        dbHelper.eliminarReto(retoId);
    }
}
