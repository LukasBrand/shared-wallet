package com.lukasbrand.sharedwallet.ui.account.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lukasbrand.sharedwallet.repository.AuthenticationRepository

class CreateAccountViewModelFactory(private val authenticationRepository: AuthenticationRepository) :
    ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateAccountViewModel::class.java)) {
            return CreateAccountViewModel(authenticationRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}