package com.lukasbrand.sharedwallet.ui.wallet.show

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ShowExpenseViewModelFactory(private val expenseId: String) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowExpenseViewModel::class.java)) {
            return ShowExpenseViewModel(expenseId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}