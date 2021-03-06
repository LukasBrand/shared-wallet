package com.lukasbrand.sharedwallet.ui.account.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukasbrand.sharedwallet.exhaustive
import com.lukasbrand.sharedwallet.data.repository.AuthenticationRepository
import com.lukasbrand.sharedwallet.types.Navigator
import com.lukasbrand.sharedwallet.types.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authenticationRepository: AuthenticationRepository) :
    ViewModel() {

    val email: MutableStateFlow<UiState<String>> = MutableStateFlow(UiState.Unset)

    val password: MutableStateFlow<UiState<String>> = MutableStateFlow(UiState.Unset)

    fun login() {
        _navigateToListExpenses.value = Navigator.Loading
        viewModelScope.launch {
            val email = when (val e = email.value) {
                is UiState.Set -> e.data
                UiState.Unset -> {
                    return@launch
                }
            }.exhaustive

            val password = when (val p = password.value) {
                is UiState.Set -> p.data
                UiState.Unset -> {
                    return@launch
                }
            }.exhaustive

            try {
                authenticationRepository.signInWithEmailAndPassword(email, password)
                _navigateToListExpenses.value = Navigator.Navigate(Unit)
            } catch (e: Exception) {
                _navigateToListExpenses.value = Navigator.Error(e)
            }
        }

    }

    private val _navigateToListExpenses: MutableStateFlow<Navigator<Unit>> =
        MutableStateFlow(Navigator.Stay)
    val navigateToListExpenses: StateFlow<Navigator<Unit>>
        get() = _navigateToListExpenses

    fun onLoggedInNavigated() {
        _navigateToListExpenses.value = Navigator.Stay
    }
}