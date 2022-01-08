package com.lukasbrand.sharedwallet.services.message.model

import com.google.gson.annotations.SerializedName

data class NotificationMessage(
    @SerializedName("to") val userToken: String,
    @SerializedName("notification") val notification: Notification
)
