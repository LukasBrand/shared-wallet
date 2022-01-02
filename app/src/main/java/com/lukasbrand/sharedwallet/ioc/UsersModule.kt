package com.lukasbrand.sharedwallet.ioc

import com.lukasbrand.sharedwallet.datasource.UsersRemoteDataSource
import com.lukasbrand.sharedwallet.datasource.firestore.FirestoreApi
import com.lukasbrand.sharedwallet.repository.UsersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UsersModule {

    @Provides
    @Singleton
    fun provideUsersRepository(firestoreApi: FirestoreApi): UsersRepository {
        val usersRemoteDataSource = UsersRemoteDataSource(firestoreApi, Dispatchers.IO)
        return UsersRepository(usersRemoteDataSource)
    }
}