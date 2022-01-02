package com.lukasbrand.sharedwallet.ui.wallet.show

import androidx.lifecycle.*
import com.lukasbrand.sharedwallet.data.Expense
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShowExpenseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val expenseId: String = savedStateHandle["expenseId"]!!

    private val expense = MediatorLiveData<Expense>()

    fun getExpense() = expense

    init {
        //Get expense from database and use it as live data
        //expense.addSource()
    }

    private val _navigateToListExpenses = MutableLiveData<Boolean?>()

    val navigateToListExpenses: LiveData<Boolean?>
        get() = _navigateToListExpenses

    fun doneNavigating() {
        _navigateToListExpenses.value = null
    }

    fun onClose() {
        _navigateToListExpenses.value = true
    }

}