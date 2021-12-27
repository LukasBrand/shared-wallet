package com.lukasbrand.sharedwallet.data.model

import android.graphics.Bitmap

data class UserApiModel(
    val id: String,
    val name: String,
    val email: String,
    val image: Bitmap?
)
