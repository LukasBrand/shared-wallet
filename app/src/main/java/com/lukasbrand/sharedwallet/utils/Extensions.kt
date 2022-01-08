package com.lukasbrand.sharedwallet

import android.content.res.Resources
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * This is used to get a compiler error if a when branching is not completely covered
 * i.e:
 *  when(result){
 *     is Result.Success -> { }
 *     is Result.Error -> { }
 *  }.exhaustive
 */
val <T> T.exhaustive: T
    get() = this