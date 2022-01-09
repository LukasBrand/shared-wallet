package com.lukasbrand.sharedwallet.ui.account.delete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukasbrand.sharedwallet.data.User
import com.lukasbrand.sharedwallet.data.repository.AuthenticationRepository
import com.lukasbrand.sharedwallet.data.repository.UsersRepository
import com.lukasbrand.sharedwallet.exhaustive
import com.lukasbrand.sharedwallet.types.Navigator
import com.lukasbrand.sharedwallet.types.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteAccountViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val usersRepository: UsersRepository
) : ViewModel() {

    val email: MutableStateFlow<UiState<String>> = MutableStateFlow(UiState.Unset)

    val password: MutableStateFlow<UiState<String>> = MutableStateFlow(UiState.Unset)

    fun deleteAccount() {
        viewModelScope.launch {
            val userId = authenticationRepository.handleAuthentication()
            val user = usersRepository.getUser(userId)
            val oldEmail = user.email
            val email = when (val e = email.value) {
                is UiState.Set -> e.data
                UiState.Unset -> {
                    _navigateToTitle.value = Navigator.Error(Exception("Email must be set."))
                    return@launch
                }
            }.exhaustive
            val password = when (val p = password.value) {
                is UiState.Set -> p.data
                UiState.Unset -> {
                    _navigateToTitle.value = Navigator.Error(Exception("Password must be set."))
                    return@launch
                }
            }.exhaustive

            if (oldEmail != email) {
                _navigateToTitle.value =
                    Navigator.Error(Exception("Email must be the same as login mail."))
            }

                usersRepository.removeUser(userId)
                authenticationRepository.deleteUser(email, password)

            _navigateToTitle.value = Navigator.Navigate(Unit)
        }
    }


    private val _navigateToTitle: MutableStateFlow<Navigator<Unit>> =
        MutableStateFlow(Navigator.Stay)
    val navigateToTitle: StateFlow<Navigator<Unit>>
        get() = _navigateToTitle

    fun onAccountDeleteNavigated() {
        _navigateToTitle.value = Navigator.Stay
    }
}