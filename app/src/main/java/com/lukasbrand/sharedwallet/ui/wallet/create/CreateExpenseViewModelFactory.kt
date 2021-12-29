package com.lukasbrand.sharedwallet.ui.wallet.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lukasbrand.sharedwallet.repository.ExpensesRepository
import com.lukasbrand.sharedwallet.repository.UsersRepository
import java.lang.IllegalArgumentException

class CreateExpenseViewModelFactory(
    private val expensesRepository: ExpensesRepository,
    private val usersRepository: UsersRepository
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateExpenseViewModel::class.java)) {
            return CreateExpenseViewModel(expensesRepository, usersRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}