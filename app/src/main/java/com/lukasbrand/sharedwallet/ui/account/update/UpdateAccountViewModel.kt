package com.lukasbrand.sharedwallet.ui.account.update

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukasbrand.sharedwallet.data.User
import com.lukasbrand.sharedwallet.data.repository.AuthenticationRepository
import com.lukasbrand.sharedwallet.data.repository.UsersRepository
import com.lukasbrand.sharedwallet.exhaustive
import com.lukasbrand.sharedwallet.types.Navigator
import com.lukasbrand.sharedwallet.types.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateAccountViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authenticationRepository: AuthenticationRepository,
    private val usersRepository: UsersRepository
) : ViewModel() {

    private val user: StateFlow<User> = flow {
        val userId = authenticationRepository.handleAuthentication()
        val user = usersRepository.getUser(userId)

        emit(user)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = savedStateHandle["user"]!!
    )

    val newUsername: MutableStateFlow<UiState<String>> = MutableStateFlow(UiState.Unset)

    val newEmail: MutableStateFlow<UiState<String>> = MutableStateFlow(UiState.Unset)

    //TODO
    @Suppress("MemberVisibilityCanBePrivate")
    val newImage: MutableStateFlow<UiState<Bitmap>> = MutableStateFlow(UiState.Unset)

    val newPassword: MutableStateFlow<UiState<String>> = MutableStateFlow(UiState.Unset)

    val oldPassword: MutableStateFlow<UiState<String>> = MutableStateFlow(UiState.Unset)

    @ExperimentalCoroutinesApi
    val initials: StateFlow<String> = newUsername.mapLatest { uiState ->
        when (uiState) {
            is UiState.Set -> {
                if (uiState.data.isNotEmpty()) {
                    uiState.data[0].toString()
                } else {
                    ""
                }
            }
            UiState.Unset -> {
                if (user.value.name.isNotEmpty()) {
                    user.value.name[0].toString()
                } else {
                    ""
                }
            }
        }.exhaustive
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    fun updateAccount() {
        var oldEmail = user.value.email
        val oldPassword = when (val oldPassword = oldPassword.value) {
            is UiState.Set -> {
                oldPassword.data
            }
            UiState.Unset -> {
                _navigateToShowAccount.value =
                    Navigator.Error(Exception("Old Password is missing."))
                return
            }
        }.exhaustive

        when (val newUsername = newUsername.value) {
            is UiState.Set -> {
                viewModelScope.launch {
                    usersRepository.modifyUser(user.value.copy(name = newUsername.data))
                }
            }
            UiState.Unset -> {
                Any()
            }
        }.exhaustive

        when (val newEmail = newEmail.value) {
            is UiState.Set -> {
                if (oldEmail != newEmail.data) {
                    viewModelScope.launch {
                        authenticationRepository.updateEmailAddress(
                            oldEmail,
                            oldPassword,
                            newEmail.data
                        )
                        usersRepository.modifyUser(user.value.copy(email = newEmail.data))
                    }
                    oldEmail = newEmail.data
                }
                Any()
            }
            UiState.Unset -> {
                Any()
            }
        }.exhaustive

        when (newImage.value) {
            is UiState.Set -> {
                TODO()
            }
            UiState.Unset -> {}
        }.exhaustive

        when (val newPassword = newPassword.value) {
            is UiState.Set -> {
                viewModelScope.launch {
                    authenticationRepository.updatePassword(oldEmail, oldPassword, newPassword.data)
                }
            }
            UiState.Unset -> {
                Any()
            }
        }.exhaustive


        _navigateToShowAccount.value = Navigator.Navigate(Unit)
    }


    private val _navigateToShowAccount: MutableStateFlow<Navigator<Unit>> =
        MutableStateFlow(Navigator.Stay)
    val navigateToShowAccount: StateFlow<Navigator<Unit>>
        get() = _navigateToShowAccount

    fun onAccountEditedNavigated() {
        _navigateToShowAccount.value = Navigator.Stay
    }
}