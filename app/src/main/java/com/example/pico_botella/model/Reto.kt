package com.example.pico_botella.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

@IgnoreExtraProperties
data class Reto(
    @get:Exclude var id: String = "",
    var descripcion: String = "",
    @ServerTimestamp var createdAt: Date? = null
)
