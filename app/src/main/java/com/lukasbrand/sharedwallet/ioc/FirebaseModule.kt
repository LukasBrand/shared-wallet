package com.lukasbrand.sharedwallet.ioc

import com.lukasbrand.sharedwallet.data.datasource.firestore.FirebaseAuthApi
import com.lukasbrand.sharedwallet.data.datasource.firestore.FirestoreApi
import com.lukasbrand.sharedwallet.services.message.firebase.FirebaseMessageApi
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

    @Provides
    @Singleton
    fun provideFirestoreMessageApi(): FirebaseMessageApi {
        return FirebaseMessageApi.getInstance()
    }
}