package com.lukasbrand.sharedwallet.ioc

import com.lukasbrand.sharedwallet.datasource.AuthenticationRemoteDataSource
import com.lukasbrand.sharedwallet.datasource.ExpensesRemoteDataSource
import com.lukasbrand.sharedwallet.datasource.UsersRemoteDataSource
import com.lukasbrand.sharedwallet.datasource.firestore.FirebaseAuthApi
import com.lukasbrand.sharedwallet.datasource.firestore.FirestoreApi
import com.lukasbrand.sharedwallet.repository.AuthenticationRepository
import com.lukasbrand.sharedwallet.repository.ExpensesRepository
import com.lukasbrand.sharedwallet.repository.UsersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ExpensesModule {

    @Provides
    @Singleton
    fun provideExpensesRepository(firestoreApi: FirestoreApi): ExpensesRepository {
        val expensesRemoteDataSource = ExpensesRemoteDataSource(firestoreApi, Dispatchers.IO)
        return ExpensesRepository(expensesRemoteDataSource)
    }
}