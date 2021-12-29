package com.lukasbrand.sharedwallet.repository

import com.lukasbrand.sharedwallet.datasource.AuthenticationRemoteDataSource

class AuthenticationRepository(private val authenticationRemoteDataSource: AuthenticationRemoteDataSource) {

    suspend fun createAccountWithEmailAndPassword(email: String, password: String): Result<String> =
        authenticationRemoteDataSource.createAccountWithEmailAndPassword(email, password)

    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<String> =
        authenticationRemoteDataSource.signInWithEmailAndPassword(email, password)

    suspend fun signOut(): Unit = authenticationRemoteDataSource.signOut()
}