package com.lukasbrand.sharedwallet.utils

import android.content.res.Resources
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


fun convertTimestampToFormatted(time: LocalDateTime): CharSequence {
    val formatter = DateTimeFormatter.ofPattern("HH:mm dd-MMMM-yyyy")
    return time.format(formatter)
}

fun convertTimestampToFormatted(time: LocalDateTime, resources: Resources): CharSequence {
    val formatter = DateTimeFormatter.ofPattern("HH:mm dd-MMMM-yyyy")
    return time.format(formatter)
}