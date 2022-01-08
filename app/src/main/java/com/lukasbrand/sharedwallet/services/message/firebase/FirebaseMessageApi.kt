package com.lukasbrand.sharedwallet.services.message.firebase

import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseMessageApi(private val firebaseMessaging: FirebaseMessaging) {

    companion object {
        @Volatile
        private var INSTANCE: FirebaseMessageApi? = null

        fun getInstance(): FirebaseMessageApi {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = FirebaseMessageApi(FirebaseMessaging.getInstance())
                }

                return instance
            }
        }
    }

    suspend fun getToken(): String = suspendCancellableCoroutine { continuation ->
        firebaseMessaging.token.addOnSuccessListener { messagingToken ->
            continuation.resume(messagingToken)
        }.addOnFailureListener { exception ->
            continuation.resumeWithException(exception)
        }.addOnCanceledListener {
            continuation.cancel()
        }
    }
}