package com.lukasbrand.sharedwallet.ioc

import com.lukasbrand.sharedwallet.services.message.MessageSendService
import com.lukasbrand.sharedwallet.services.message.api.RetrofitBuilder
import com.lukasbrand.sharedwallet.services.message.api.SendApiService
import com.lukasbrand.sharedwallet.services.message.firebase.FirebaseMessageApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MessageModule {

    @Provides
    @Singleton
    fun provideMessageSendService(
        firebaseMessageApi: FirebaseMessageApi,
        sendApiService: SendApiService
    ): MessageSendService {
        return MessageSendService(firebaseMessageApi, sendApiService)
    }

    @Provides
    @Singleton
    fun provideMessageApiService(): SendApiService {
        return RetrofitBuilder.sendApiService
    }
}