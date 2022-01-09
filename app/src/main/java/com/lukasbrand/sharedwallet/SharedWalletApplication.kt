package com.lukasbrand.sharedwallet

import android.app.Application
import com.google.android.libraries.places.api.Places
import com.lukasbrand.sharedwallet.services.message.MessageSendService
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class SharedWalletApplication : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        delayedInit()
        Places.initialize(this, BuildConfig.PLACES_API_KEY)
    }

    private fun delayedInit() {
        applicationScope.launch {
            //Delayed start logic goes here
        }
    }

}