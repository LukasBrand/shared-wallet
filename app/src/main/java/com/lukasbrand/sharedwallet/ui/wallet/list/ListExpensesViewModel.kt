package com.lukasbrand.sharedwallet.ui.wallet.list

import android.app.Application
import androidx.lifecycle.*
import com.lukasbrand.sharedwallet.data.Expense
import com.lukasbrand.sharedwallet.repository.ExpensesRepository

class ListExpensesViewModel(
    private val repository: ExpensesRepository,
    private val application: Application
) : ViewModel() {

    private val uiScope = viewModelScope

    //val expenses: LiveData<List<Expense>> = repository.fetchExpenses().asLiveData()


    //Clickable Expense
    private val _navigateToExpenseDetail = MutableLiveData<Long?>()
    val navigateToExpenseDetail: LiveData<Long?>
        get() = _navigateToExpenseDetail

    fun onExpenseItemClicked(expenseId: Long) {
        _navigateToExpenseDetail.value = expenseId
    }

    fun onExpenseItemNavigated() {
        _navigateToExpenseDetail.value = null
    }
}