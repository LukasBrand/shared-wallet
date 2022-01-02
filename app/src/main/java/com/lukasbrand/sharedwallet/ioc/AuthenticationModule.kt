package com.lukasbrand.sharedwallet.ioc

import com.lukasbrand.sharedwallet.datasource.AuthenticationRemoteDataSource
import com.lukasbrand.sharedwallet.datasource.firestore.FirebaseAuthApi
import com.lukasbrand.sharedwallet.repository.AuthenticationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AuthenticationModule {

    @Provides
    @Singleton
    fun provideAuthenticationRepository(firebaseAuthApi: FirebaseAuthApi): AuthenticationRepository {
        val authenticationRemoteDataSource =
            AuthenticationRemoteDataSource(firebaseAuthApi, Dispatchers.IO)
        return AuthenticationRepository(authenticationRemoteDataSource)
    }
}