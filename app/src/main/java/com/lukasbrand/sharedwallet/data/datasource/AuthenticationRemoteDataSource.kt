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

    suspend fun updateEmailAddress(oldEmail: String, oldPassword: String, newEmail: String): Unit =
        withContext(ioDispatcher) {
            firestoreAuthApi.updateEmailAddress(oldEmail, oldPassword, newEmail)
        }

    suspend fun updatePassword(oldEmail: String, oldPassword: String, newPassword: String): Unit =
        withContext(ioDispatcher) {
            firestoreAuthApi.updatePassword(oldEmail, oldPassword, newPassword)
        }

    suspend fun deleteUser(email: String, password: String): Unit =
        withContext(ioDispatcher) {
            firestoreAuthApi.deleteUser(email, password)
        }
}