package com.lukasbrand.sharedwallet.data.repository

import com.lukasbrand.sharedwallet.data.datasource.AuthenticationLocalDataSource
import com.lukasbrand.sharedwallet.data.datasource.AuthenticationRemoteDataSource
import com.lukasbrand.sharedwallet.exception.NotLoggedInException

class AuthenticationRepository(
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource,
    private val authenticationLocalDataSource: AuthenticationLocalDataSource
) {

    suspend fun createAccountWithEmailAndPassword(email: String, password: String): String {
        authenticationLocalDataSource.setUserCredentials(email, password)
        return authenticationRemoteDataSource.createAccountWithEmailAndPassword(email, password)
    }

    suspend fun signInWithEmailAndPassword(email: String, password: String): String {
        authenticationLocalDataSource.setUserCredentials(email, password)
        return authenticationRemoteDataSource.signInWithEmailAndPassword(email, password)
    }

    suspend fun signOut() {
        authenticationLocalDataSource.clearUserCredentials()
        authenticationRemoteDataSource.signOut()
    }

    suspend fun handleAuthentication(): String {
        return if (authenticationRemoteDataSource.getAuthStatus()) {
            authenticationRemoteDataSource.getUserId()
        } else {
            val userCredentials: Pair<String, String>? =
                authenticationLocalDataSource.getUserCredentials()
            if (userCredentials != null) {
                authenticationRemoteDataSource.signInWithEmailAndPassword(
                    userCredentials.first,
                    userCredentials.second
                )

            } else {
                throw NotLoggedInException("No User found")
            }
        }
    }
}
