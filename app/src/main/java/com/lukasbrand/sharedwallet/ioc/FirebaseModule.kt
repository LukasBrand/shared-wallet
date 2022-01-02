package com.lukasbrand.sharedwallet.ioc

import com.lukasbrand.sharedwallet.datasource.firestore.FirebaseAuthApi
import com.lukasbrand.sharedwallet.datasource.firestore.FirestoreApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuthApi(): FirebaseAuthApi {
        return FirebaseAuthApi.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirestoreApi(): FirestoreApi {
        return FirestoreApi.getInstance()
    }
}