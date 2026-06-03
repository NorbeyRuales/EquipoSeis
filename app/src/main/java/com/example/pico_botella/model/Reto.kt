package com.example.pico_botella.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "retos")
data class Reto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var descripcion: String
)
