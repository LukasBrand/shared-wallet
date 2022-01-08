package com.lukasbrand.sharedwallet.types

sealed class UiState<out T : Any> {
    data class Set<out T : Any>(val data: T) : UiState<T>()
    object Unset : UiState<Nothing>()
}


