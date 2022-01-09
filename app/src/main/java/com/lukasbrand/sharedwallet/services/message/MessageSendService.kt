package com.lukasbrand.sharedwallet.services.message

import com.lukasbrand.sharedwallet.services.message.api.SendApiService
import com.lukasbrand.sharedwallet.services.message.firebase.FirebaseMessageApi
import com.lukasbrand.sharedwallet.services.message.model.Notification
import com.lukasbrand.sharedwallet.services.message.model.NotificationMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MessageSendService @Inject constructor(
    private val firebaseMessageApi: FirebaseMessageApi,
    private val sendApiService: SendApiService
) {

    suspend fun getToken(): String = withContext(Dispatchers.Default) {
        firebaseMessageApi.getToken()
    }

    suspend fun sendNotificationToUser(userToken: String, title: String, message: String) =
        withContext(Dispatchers.IO) {
            sendApiService.sendNotificationToUser(
                NotificationMessage(
                    userToken,
                    Notification(title, message)
                )
            )
        }
}