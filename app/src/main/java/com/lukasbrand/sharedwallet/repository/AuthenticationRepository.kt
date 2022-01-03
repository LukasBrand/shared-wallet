package com.lukasbrand.sharedwallet.repository

import com.lukasbrand.sharedwallet.datasource.AuthenticationLocalDataSource
import com.lukasbrand.sharedwallet.datasource.AuthenticationRemoteDataSource
import com.lukasbrand.sharedwallet.exception.NotLoggedInException

class AuthenticationRepository(
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource,
    private val authenticationLocalDataSource: AuthenticationLocalDataSource
) {

    suspend fun createAccountWithEmailAndPassword(email: String, password: String): Result<String> {
        authenticationLocalDataSource.setUserCredentials(email, password)
        return authenticationRemoteDataSource.createAccountWithEmailAndPassword(email, password)
    }

    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<String> {
        authenticationLocalDataSource.setUserCredentials(email, password)
        return authenticationRemoteDataSource.signInWithEmailAndPassword(email, password)
    }

    suspend fun signOut(): Unit = authenticationRemoteDataSource.signOut()

    suspend fun handleAuthentication(): Result<String> {
        return if (authenticationRemoteDataSource.getAuthStatus()) {
            Result.success(authenticationRemoteDataSource.getUserId())
        } else {
            val userCredentials: Pair<String, String>? =
                authenticationLocalDataSource.getUserCredentials()
            if (userCredentials != null) {
                try {
                    authenticationRemoteDataSource.signInWithEmailAndPassword(
                        userCredentials.first,
                        userCredentials.second
                    )
                } catch (e: Exception) {
                    Result.failure(e)
                }
            } else {
                Result.failure(NotLoggedInException())
            }
        }
    }
}