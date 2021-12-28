package com.lukasbrand.sharedwallet.ui.account.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lukasbrand.sharedwallet.repository.AuthenticationRepository

class LoginViewModelFactory(private val authenticationRepository: AuthenticationRepository) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(authenticationRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}