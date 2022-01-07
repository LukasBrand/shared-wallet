package com.lukasbrand.sharedwallet.repository

import com.lukasbrand.sharedwallet.datasource.AuthenticationLocalDataSource
import com.lukasbrand.sharedwallet.datasource.AuthenticationRemoteDataSource
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

    suspend fun signOut(): Unit = authenticationRemoteDataSource.signOut()

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
                throw NotLoggedInException()
            }
        }
    }
}