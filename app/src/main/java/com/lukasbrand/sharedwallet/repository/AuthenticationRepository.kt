package com.lukasbrand.sharedwallet.repository

import com.lukasbrand.sharedwallet.datasource.AuthenticationRemoteDataSource

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class AuthenticationRepository(val authenticationRemoteDataSource: AuthenticationRemoteDataSource) {

    suspend fun createAccountWithEmailAndPassword(email: String, password: String): Result<String> =
        authenticationRemoteDataSource.createAccountWithEmailAndPassword(email, password)

    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<String> =
        authenticationRemoteDataSource.signInWithEmailAndPassword(email, password)

    suspend fun signOut(): Unit = authenticationRemoteDataSource.signOut()
}