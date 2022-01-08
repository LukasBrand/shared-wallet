package com.lukasbrand.sharedwallet.services

import android.widget.Toast
import androidx.compose.runtime.currentRecomposeScope
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.lukasbrand.sharedwallet.data.repository.UsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NotificationService : FirebaseMessagingService() {

    @Inject
    lateinit var usersRepository: UsersRepository

    override fun onMessageReceived(message: RemoteMessage) {
        println(message.toString())
    }

    override fun onNewToken(p0: String) {
        //TODO: Modify user token if updated on server
        //usersRepository.modifyUser()
    }
}