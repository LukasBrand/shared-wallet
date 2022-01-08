package com.lukasbrand.sharedwallet.ui.account.create

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukasbrand.sharedwallet.data.User
import com.lukasbrand.sharedwallet.data.repository.AuthenticationRepository
import com.lukasbrand.sharedwallet.data.repository.UsersRepository
import com.lukasbrand.sharedwallet.services.message.MessageSendService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateAccountViewModel @Inject constructor(
    private val messageSendService: MessageSendService,
    private val authenticationRepository: AuthenticationRepository,
    private val usersRepository: UsersRepository
) : ViewModel() {

    private val _onAccountCreated: MutableLiveData<User> = MutableLiveData()
    val onAccountCreated: LiveData<User>
        get() = _onAccountCreated

    val name: MutableLiveData<String> = MutableLiveData("")
    val email: MutableLiveData<String> = MutableLiveData("")
    val password: MutableLiveData<String> = MutableLiveData("")
    val picture: MutableLiveData<Bitmap> = MutableLiveData()

    fun createAccount() {
        viewModelScope.launch {
            val userId =
                authenticationRepository.createAccountWithEmailAndPassword(
                    email.value!!,
                    password.value!!
                )

            val notificationToken = messageSendService.getToken()

            val user = User(userId, name.value!!, email.value!!, notificationToken, picture.value)
            usersRepository.addUser(user)
            _onAccountCreated.value = user
        }
    }
}


