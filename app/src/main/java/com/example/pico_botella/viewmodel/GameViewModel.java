package com.example.pico_botella.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.pico_botella.model.GameChallengeUiState;
import com.example.pico_botella.model.Reto;
import com.example.pico_botella.repository.PokemonRepository;
import com.example.pico_botella.repository.RetoRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameViewModel extends AndroidViewModel {

    private final RetoRepository retoRepository;
    private final PokemonRepository pokemonRepository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final MutableLiveData<GameChallengeUiState> challengeState = new MutableLiveData<>();

    public GameViewModel(@NonNull Application application) {
        super(application);
        retoRepository = new RetoRepository(application);
        pokemonRepository = new PokemonRepository();
    }

    public LiveData<GameChallengeUiState> getChallengeState() {
        return challengeState;
    }

    public void cargarRetoConPokemonAleatorio() {
        challengeState.postValue(GameChallengeUiState.loading());

        executor.execute(() -> {
            Reto reto = retoRepository.obtenerRetoAleatorio();
            if (reto == null) {
                challengeState.postValue(GameChallengeUiState.empty());
                return;
            }

            String pokemonImageUrl = null;
            String errorMessage = null;

            try {
                pokemonImageUrl = pokemonRepository.obtenerImagenAleatoria();
            } catch (Exception exception) {
                errorMessage = "No se pudo cargar la imagen del Pokémon.";
            }

            challengeState.postValue(GameChallengeUiState.content(reto, pokemonImageUrl, errorMessage));
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}
