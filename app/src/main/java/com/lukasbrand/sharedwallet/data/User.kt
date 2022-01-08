package com.lukasbrand.sharedwallet.data

import android.graphics.Bitmap
import android.os.Parcelable
import android.provider.ContactsContract
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String,
    val name: String,
    val email: String,
    val notificationToken: String,
    val image: Bitmap?
) : Parcelable