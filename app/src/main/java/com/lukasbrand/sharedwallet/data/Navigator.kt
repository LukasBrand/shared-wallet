package com.lukasbrand.sharedwallet.data

sealed class Navigator<out T : Any> {
    data class Navigate<out T : Any>(val data: T) : Navigator<T>()
    data class Error(val exception: Exception) : Navigator<Nothing>()
    object Loading : Navigator<Nothing>()
    object Stay: Navigator<Nothing>()
}


