package com.example.pico_botella.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Reto(
    @get:Exclude var id: String = "",
    var descripcion: String = ""
)
