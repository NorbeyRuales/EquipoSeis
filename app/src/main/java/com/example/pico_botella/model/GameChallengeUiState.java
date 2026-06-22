package com.example.pico_botella.model;

public class GameChallengeUiState {

    private final boolean loading;
    private final boolean empty;
    private final Reto reto;
    private final String pokemonImageUrl;
    private final String errorMessage;

    private GameChallengeUiState(
            boolean loading,
            boolean empty,
            Reto reto,
            String pokemonImageUrl,
            String errorMessage
    ) {
        this.loading = loading;
        this.empty = empty;
        this.reto = reto;
        this.pokemonImageUrl = pokemonImageUrl;
        this.errorMessage = errorMessage;
    }

    public static GameChallengeUiState loading() {
        return new GameChallengeUiState(true, false, null, null, null);
    }

    public static GameChallengeUiState empty() {
        return new GameChallengeUiState(false, true, null, null, null);
    }

    public static GameChallengeUiState content(Reto reto, String pokemonImageUrl, String errorMessage) {
        return new GameChallengeUiState(false, false, reto, pokemonImageUrl, errorMessage);
    }

    public boolean isLoading() {
        return loading;
    }

    public boolean isEmpty() {
        return empty;
    }

    public Reto getReto() {
        return reto;
    }

    public String getPokemonImageUrl() {
        return pokemonImageUrl;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
