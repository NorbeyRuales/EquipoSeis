package com.example.pico_botella.repository

import com.example.pico_botella.database.RetoDao
import com.example.pico_botella.model.Reto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlin.random.Random

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

    suspend fun obtenerRetoAleatorio(): Reto? {
        val retos = retoDao.getAllRetos().first()
        if (retos.isEmpty()) {
            return null
        }

        return retos[Random.nextInt(retos.size)]
    }
}
