package com.lukasbrand.sharedwallet.ui.account.create

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukasbrand.sharedwallet.data.User
import com.lukasbrand.sharedwallet.data.repository.AuthenticationRepository
import com.lukasbrand.sharedwallet.data.repository.UsersRepository
import com.lukasbrand.sharedwallet.services.message.MessageSendService
import com.lukasbrand.sharedwallet.types.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateAccountViewModel @Inject constructor(
    private val messageSendService: MessageSendService,
    private val authenticationRepository: AuthenticationRepository,
    private val usersRepository: UsersRepository
) : ViewModel() {

    val name: MutableStateFlow<String> = MutableStateFlow("")
    val email: MutableStateFlow<String> = MutableStateFlow("")
    val password: MutableStateFlow<String> = MutableStateFlow("")
    val picture: MutableStateFlow<Bitmap?> = MutableStateFlow(null)

    fun createAccount() {
        viewModelScope.launch {
            val userId = authenticationRepository.createAccountWithEmailAndPassword(
                email.value,
                password.value
            )

            val notificationToken = messageSendService.getToken()

            val user = User(userId, name.value, email.value, notificationToken, picture.value)
            usersRepository.addUser(user)
            _createAccountCompleted.value = Navigator.Navigate(Unit)
        }
    }

    private val _createAccountCompleted: MutableStateFlow<Navigator<Unit>> =
        MutableStateFlow(Navigator.Stay)
    val createAccountCompleted: StateFlow<Navigator<Unit>> = _createAccountCompleted

    fun onNavigatedAfterAccountCreateCompleted() {
        _createAccountCompleted.value = Navigator.Stay
    }
}


