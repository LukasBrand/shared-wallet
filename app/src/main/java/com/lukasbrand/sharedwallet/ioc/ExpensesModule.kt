package com.lukasbrand.sharedwallet.ioc

import com.lukasbrand.sharedwallet.data.datasource.ExpensesRemoteDataSource
import com.lukasbrand.sharedwallet.data.datasource.firestore.FirestoreApi
import com.lukasbrand.sharedwallet.data.repository.ExpensesRepository
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