package com.example.pico_botella.viewmodel

import androidx.lifecycle.*
import com.example.pico_botella.model.Reto
import com.example.pico_botella.repository.RetoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RetosViewModel @Inject constructor(
    private val repository: RetoRepository
) : ViewModel() {

    val allRetos: LiveData<List<Reto>> = repository.getAllRetos().asLiveData()

    fun insert(reto: Reto) = viewModelScope.launch {
        repository.insert(reto)
    }

    fun update(reto: Reto) = viewModelScope.launch {
        repository.update(reto)
    }

    fun delete(reto: Reto) = viewModelScope.launch {
        repository.delete(reto)
    }
}
