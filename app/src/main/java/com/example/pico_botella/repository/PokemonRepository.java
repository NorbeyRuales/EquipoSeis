package com.example.pico_botella.repository;

import com.example.pico_botella.data.PokemonRemoteDataSource;

public class PokemonRepository {

    private final PokemonRemoteDataSource remoteDataSource;

    public PokemonRepository() {
        remoteDataSource = new PokemonRemoteDataSource();
    }

    public String obtenerImagenAleatoria() throws Exception {
        return remoteDataSource.obtenerImagenAleatoria();
    }
}
