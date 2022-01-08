package com.lukasbrand.sharedwallet.services.message.model

import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("title") val title: String,
    @SerializedName("body") val body: String
)