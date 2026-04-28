package com.lingolearn.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.lingolearn.app.data.local.datastore.UserPreferences
import com.lingolearn.app.data.local.db.entity.StreakEntity
import com.lingolearn.app.domain.repository.StreakRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StreakRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val userPreferences: UserPreferences
) : StreakRepository {

    private suspend fun getEmail(): String {
        return userPreferences.userEmailFlow.first().takeIf { it.isNotEmpty() } ?: "anonymous"
    }

    override fun getStreak(): Flow<StreakEntity?> = callbackFlow {
        val email = getEmail()
        val listener = firestore.collection("users")
            .document(email)
            .collection("streaks")
            .document("current")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(null)
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val streak = snapshot.toObject(StreakEntity::class.java)
                    trySend(streak)
                } else {
                    trySend(null)
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun insertStreak(streak: StreakEntity) {
        firestore.collection("users")
            .document(getEmail())
            .collection("streaks")
            .document("current")
            .set(streak)
            .await()
    }

    override suspend fun updateStreak(streak: StreakEntity) {
        insertStreak(streak) // Firestore .set() overwrites it cleanly
    }
}
