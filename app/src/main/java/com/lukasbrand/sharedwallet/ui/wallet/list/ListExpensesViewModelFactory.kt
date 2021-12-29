package com.lukasbrand.sharedwallet.ui.wallet.list

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lukasbrand.sharedwallet.datasource.ExpensesRemoteDataSource
import com.lukasbrand.sharedwallet.repository.ExpensesRepository
import com.lukasbrand.sharedwallet.repository.UsersRepository
import com.lukasbrand.sharedwallet.ui.wallet.show.ShowExpenseViewModel
import java.lang.IllegalArgumentException

class ListExpensesViewModelFactory(
    private val expensesRepository: ExpensesRepository,
    private val usersRepository: UsersRepository
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListExpensesViewModel::class.java)) {
            return ListExpensesViewModel(expensesRepository, usersRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}