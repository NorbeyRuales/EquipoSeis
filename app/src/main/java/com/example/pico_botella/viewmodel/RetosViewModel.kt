package com.example.pico_botella.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.pico_botella.database.AppDatabase
import com.example.pico_botella.model.Reto
import com.example.pico_botella.repository.RetoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RetosViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RetoRepository
    val allRetos: LiveData<List<Reto>>

    init {
        val retoDao = AppDatabase.getDatabase(application, viewModelScope).retoDao()
        repository = RetoRepository(retoDao)
        allRetos = repository.allRetos.asLiveData()
    }

    fun insert(reto: Reto) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(reto)
    }

    fun update(reto: Reto) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(reto)
    }

    fun delete(reto: Reto) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(reto)
    }
}
