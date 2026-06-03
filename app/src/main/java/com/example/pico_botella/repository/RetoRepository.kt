package com.example.pico_botella.repository

import com.example.pico_botella.database.RetoDao
import com.example.pico_botella.model.Reto
import kotlinx.coroutines.flow.Flow

class RetoRepository(private val retoDao: RetoDao) {

    val allRetos: Flow<List<Reto>> = retoDao.getAllRetos()

    suspend fun insert(reto: Reto) {
        retoDao.insertReto(reto)
    }

    suspend fun update(reto: Reto) {
        retoDao.updateReto(reto)
    }

    suspend fun delete(reto: Reto) {
        retoDao.deleteReto(reto)
    }
}
