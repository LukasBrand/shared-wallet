package com.lukasbrand.sharedwallet.ui.account.show

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukasbrand.sharedwallet.data.User
import com.lukasbrand.sharedwallet.data.repository.AuthenticationRepository
import com.lukasbrand.sharedwallet.data.repository.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ShowAccountViewModel @Inject constructor(
    authenticationRepository: AuthenticationRepository,
    usersRepository: UsersRepository
) : ViewModel() {


    private val user: StateFlow<User> = flow {
        val userId = authenticationRepository.handleAuthentication()
        val user = usersRepository.getUser(userId)

        emit(user)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = User("", "", "", "", null)
    )


    @ExperimentalCoroutinesApi
    val username: StateFlow<String> = user.mapLatest {
        it.name
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    @ExperimentalCoroutinesApi
    val email: StateFlow<String> = user.mapLatest {
        it.email
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    @ExperimentalCoroutinesApi
    val image: StateFlow<Bitmap?> = user.mapLatest {
        it.image
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    @ExperimentalCoroutinesApi
    val initials: StateFlow<String> = user.mapLatest {
        if (it.name.isNotEmpty()) {
            it.name[0].toString()
        } else {
            ""
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

}