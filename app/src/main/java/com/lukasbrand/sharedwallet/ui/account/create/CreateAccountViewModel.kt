package com.lukasbrand.sharedwallet.ui.account.create

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukasbrand.sharedwallet.data.User
import com.lukasbrand.sharedwallet.repository.AuthenticationRepository
import kotlinx.coroutines.launch

class CreateAccountViewModel(private val authenticationRepository: AuthenticationRepository) :
    ViewModel() {

    private val _onAccountCreated: MutableLiveData<User> = MutableLiveData()
    val onAccountCreated: LiveData<User>
        get() = _onAccountCreated

    val name: MutableLiveData<String> = MutableLiveData("")
    val email: MutableLiveData<String> = MutableLiveData("")
    val password: MutableLiveData<String> = MutableLiveData("")
    val picture: MutableLiveData<Bitmap> = MutableLiveData()

    fun createAccount() {
        viewModelScope.launch {
            authenticationRepository.createAccountWithEmailAndPassword(
                email.value!!,
                password.value!!
            ).onSuccess { userId ->
                _onAccountCreated.value = User(userId, name.value!!, email.value!!, picture.value)
            }
        }
    }
}

