package com.example.pico_botella.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pico_botella.model.GameChallengeUiState
import com.example.pico_botella.repository.PokemonRepository
import com.example.pico_botella.repository.RetoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class GameViewModel @Inject constructor(
    private val retoRepository: RetoRepository
) : ViewModel() {

    private val pokemonRepository = PokemonRepository()
    private val _challengeState = MutableLiveData<GameChallengeUiState>()
    val challengeState: LiveData<GameChallengeUiState> = _challengeState

    fun cargarRetoConPokemonAleatorio() {
        _challengeState.value = GameChallengeUiState.loading()

        viewModelScope.launch {
            // Obtener todos los retos de Firestore y elegir uno al azar localmente
            // (Firestore no tiene un "obtener uno al azar" nativo eficiente para listas pequeñas)
            val retos = withContext(Dispatchers.IO) {
                retoRepository.getAllRetos().first()
            }

            if (retos.isEmpty()) {
                _challengeState.value = GameChallengeUiState.empty()
                return@launch
            }

            val retoAleatorio = retos[Random.nextInt(retos.size)]

            var errorMessage: String? = null
            val pokemonImageUrl = withContext(Dispatchers.IO) {
                try {
                    pokemonRepository.obtenerImagenAleatoria()
                } catch (_: Exception) {
                    errorMessage = "No se pudo cargar la imagen del Pokemon."
                    null
                }
            }

            _challengeState.value = GameChallengeUiState.content(retoAleatorio, pokemonImageUrl, errorMessage)
        }
    }
}
