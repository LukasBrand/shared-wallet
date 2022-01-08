package com.lukasbrand.sharedwallet.ioc

import android.content.Context
import com.lukasbrand.sharedwallet.data.datasource.AuthenticationLocalDataSource
import com.lukasbrand.sharedwallet.data.datasource.AuthenticationRemoteDataSource
import com.lukasbrand.sharedwallet.data.datasource.firestore.FirebaseAuthApi
import com.lukasbrand.sharedwallet.data.repository.AuthenticationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AuthenticationModule {

    @Provides
    @Singleton
    fun provideAuthenticationRepository(
        @ApplicationContext context: Context,
        firebaseAuthApi: FirebaseAuthApi
    ): AuthenticationRepository {
        val authenticationRemoteDataSource =
            AuthenticationRemoteDataSource(firebaseAuthApi, Dispatchers.IO)
        val authenticationLocalDataSource =
            AuthenticationLocalDataSource(context, Dispatchers.IO)
        return AuthenticationRepository(
            authenticationRemoteDataSource,
            authenticationLocalDataSource
        )
    }
}