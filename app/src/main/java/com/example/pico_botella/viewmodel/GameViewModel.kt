package com.example.pico_botella.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pico_botella.database.AppDatabase
import com.example.pico_botella.model.GameChallengeUiState
import com.example.pico_botella.repository.PokemonRepository
import com.example.pico_botella.repository.RetoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val retoRepository: RetoRepository
    private val pokemonRepository = PokemonRepository()
    private val _challengeState = MutableLiveData<GameChallengeUiState>()
    val challengeState: LiveData<GameChallengeUiState> = _challengeState

    init {
        val retoDao = AppDatabase.getDatabase(application, viewModelScope).retoDao()
        retoRepository = RetoRepository(retoDao)
    }

    fun cargarRetoConPokemonAleatorio() {
        _challengeState.value = GameChallengeUiState.loading()

        viewModelScope.launch {
            val reto = withContext(Dispatchers.IO) {
                retoRepository.obtenerRetoAleatorio()
            }

            if (reto == null) {
                _challengeState.value = GameChallengeUiState.empty()
                return@launch
            }

            var errorMessage: String? = null
            val pokemonImageUrl = withContext(Dispatchers.IO) {
                try {
                    pokemonRepository.obtenerImagenAleatoria()
                } catch (_: Exception) {
                    errorMessage = "No se pudo cargar la imagen del Pokemon."
                    null
                }
            }

            _challengeState.value = GameChallengeUiState.content(reto, pokemonImageUrl, errorMessage)
        }
    }
}
