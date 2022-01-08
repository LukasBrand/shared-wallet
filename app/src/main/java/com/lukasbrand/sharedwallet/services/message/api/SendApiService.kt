package com.lukasbrand.sharedwallet.services.message.api

import com.lukasbrand.sharedwallet.services.message.model.NotificationMessage
import com.squareup.okhttp.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface SendApiService {

    @POST("https://fcm.googleapis.com/fcm/send")
    suspend fun sendNotificationToUser(@Body notificationMessage: NotificationMessage)
}