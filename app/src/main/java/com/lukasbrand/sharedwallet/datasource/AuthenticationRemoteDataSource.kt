package com.lukasbrand.sharedwallet.datasource

import com.google.firebase.auth.FirebaseAuth
import com.lukasbrand.sharedwallet.datasource.firestore.FirebaseAuthApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AuthenticationRemoteDataSource(
    private val firestoreAuthApi: FirebaseAuthApi,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun createAccountWithEmailAndPassword(email: String, password: String): Result<String> =
        withContext(ioDispatcher) {
            firestoreAuthApi.createAccountWithEmailAndPassword(email, password)
        }

    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<String> =
        withContext(ioDispatcher) {
            firestoreAuthApi.signInWithEmailAndPassword(email, password)
        }

    suspend fun signOut(): Unit = withContext(ioDispatcher) {
        firestoreAuthApi.signOut()
    }
}