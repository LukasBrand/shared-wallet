package com.lukasbrand.sharedwallet.ui.wallet.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ListExpensesViewModel : ViewModel() {


    private val _navigateToExpenseDetail = MutableLiveData<Long?>()
    val navigateToExpenseDetail
        get() = _navigateToExpenseDetail

    fun onExpenseItemClicked(expenseId: Long) {
        _navigateToExpenseDetail.value = expenseId
    }

    fun onExpenseItemNavigated() {
        _navigateToExpenseDetail.value = null
    }
}