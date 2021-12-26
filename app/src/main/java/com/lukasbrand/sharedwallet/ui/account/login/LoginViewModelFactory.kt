package com.lukasbrand.sharedwallet.ui.account.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lukasbrand.sharedwallet.database.LoginDatabase
import com.lukasbrand.sharedwallet.repository.LoginRepository

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class LoginViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                loginRepository = LoginRepository(
                    database = LoginDatabase()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}