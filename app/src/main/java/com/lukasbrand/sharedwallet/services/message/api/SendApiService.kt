package com.lukasbrand.sharedwallet.services.message.api

import com.lukasbrand.sharedwallet.services.message.model.NotificationMessage
import retrofit2.http.Body
import retrofit2.http.POST

interface SendApiService {

    @POST(" ")
    suspend fun sendNotificationToUser(@Body notificationMessage: NotificationMessage)
}