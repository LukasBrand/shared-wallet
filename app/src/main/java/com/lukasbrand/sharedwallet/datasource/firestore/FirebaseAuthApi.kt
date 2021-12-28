package com.lukasbrand.sharedwallet.datasource.firestore

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseAuthApi(private val firebaseAuth: FirebaseAuth) {

    companion object {

        @Volatile
        private var INSTANCE: FirebaseAuthApi? = null

        fun getInstance(): FirebaseAuthApi {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = FirebaseAuthApi(FirebaseAuth.getInstance())
                }

                return instance
            }
        }
    }

    suspend fun createAccountWithEmailAndPassword(
        email: String,
        password: String
    ): Result<String> = suspendCancellableCoroutine { continuation ->
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                continuation.resume(Result.success(result.user!!.uid))
            }.addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
    }

    suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Result<String> = suspendCancellableCoroutine { continuation ->
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                continuation.resume(Result.success(result.user!!.uid))
            }.addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
    }

    suspend fun signOut(): Unit = suspendCancellableCoroutine {
        firebaseAuth.signOut()
        it.resume(Unit)
    }
}