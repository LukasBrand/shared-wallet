package com.lukasbrand.sharedwallet

/**
 * This is used to get a compiler error if a when branch is not completely covered
 * i.e:
 *  when(result){
 *     is Result.Success -> { }
 *     is Result.Error -> { }
 *  }.exhaustive
 */
val <T> T.exhaustive: T
    get() = this