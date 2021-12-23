package com.lukasbrand.sharedwallet.data

import android.app.Person
import android.graphics.Bitmap
import android.media.Image

data class Participant(
    val accountId: Long,
    val name: String,
    val image: Bitmap
)
