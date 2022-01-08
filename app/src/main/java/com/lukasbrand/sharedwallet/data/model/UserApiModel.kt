package com.lukasbrand.sharedwallet.data.model

import android.graphics.Bitmap

data class UserApiModel(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val notificationToken: String = "",
    val image: Bitmap? = null
)

