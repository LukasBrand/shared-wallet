package com.lukasbrand.sharedwallet.data.datasource

import com.lukasbrand.sharedwallet.data.datasource.firestore.FirebaseAuthApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AuthenticationRemoteDataSource(
    private val firestoreAuthApi: FirebaseAuthApi,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun createAccountWithEmailAndPassword(email: String, password: String): String =
        withContext(ioDispatcher) {
            firestoreAuthApi.createAccountWithEmailAndPassword(email, password)
        }

    suspend fun signInWithEmailAndPassword(email: String, password: String): String =
        withContext(ioDispatcher) {
            firestoreAuthApi.signInWithEmailAndPassword(email, password)
        }

    suspend fun signOut(): Unit = withContext(ioDispatcher) {
        firestoreAuthApi.signOut()
    }

    suspend fun getAuthStatus(): Boolean = withContext(ioDispatcher) {
        firestoreAuthApi.getAuthStatus()
    }

    suspend fun getUserId(): String = withContext(ioDispatcher) {
        firestoreAuthApi.getUserId()
    }
}