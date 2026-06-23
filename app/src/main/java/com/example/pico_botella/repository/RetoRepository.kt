package com.example.pico_botella.repository

import com.example.pico_botella.model.Reto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetoRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val retosCollection = firestore.collection("retos")

    fun getAllRetos(): Flow<List<Reto>> = callbackFlow {
        val subscription = retosCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val retos = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Reto::class.java)?.apply { id = doc.id }
                }
                trySend(retos)
            }
        }
        awaitClose { subscription.remove() }
    }

    suspend fun insert(reto: Reto) {
        retosCollection.add(reto).await()
    }

    suspend fun update(reto: Reto) {
        retosCollection.document(reto.id).set(reto).await()
    }

    suspend fun delete(reto: Reto) {
        retosCollection.document(reto.id).delete().await()
    }
}
