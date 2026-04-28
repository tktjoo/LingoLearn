package com.lingolearn.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.lingolearn.app.data.local.datastore.UserPreferences
import com.lingolearn.app.data.local.db.entity.VocabularyEntity
import com.lingolearn.app.domain.repository.VocabularyRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class VocabularyRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val userPreferences: UserPreferences
) : VocabularyRepository {

    private suspend fun getEmail(): String {
        return userPreferences.userEmailFlow.first().takeIf { it.isNotEmpty() } ?: "anonymous"
    }

    override fun getAllVocabulary(): Flow<List<VocabularyEntity>> = callbackFlow {
        val email = getEmail()
        val listener = firestore.collection("users")
            .document(email)
            .collection("vocabulary")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val list = snapshot.documents.mapNotNull { it.toObject(VocabularyEntity::class.java) }
                    trySend(list)
                } else {
                    trySend(emptyList())
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun insertVocabulary(vocabulary: VocabularyEntity): String {
        val id = UUID.randomUUID().toString()
        val newVocab = vocabulary.copy(id = id)
        firestore.collection("users")
            .document(getEmail())
            .collection("vocabulary")
            .document(id)
            .set(newVocab)
            .await()
        return id
    }

    override suspend fun updateVocabulary(vocabulary: VocabularyEntity) {
        firestore.collection("users")
            .document(getEmail())
            .collection("vocabulary")
            .document(vocabulary.id)
            .set(vocabulary)
            .await()
    }

    override suspend fun deleteVocabulary(vocabulary: VocabularyEntity) {
        firestore.collection("users")
            .document(getEmail())
            .collection("vocabulary")
            .document(vocabulary.id)
            .delete()
            .await()
    }
}
