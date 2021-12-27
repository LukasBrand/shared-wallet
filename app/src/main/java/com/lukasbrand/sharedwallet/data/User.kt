package com.lukasbrand.sharedwallet.data

import android.graphics.Bitmap
import android.provider.ContactsContract

data class User(
    val id: String,
    val name: String,
    val email: String,
    val image: Bitmap?
)