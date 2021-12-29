package com.lukasbrand.sharedwallet.ui.wallet.show

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lukasbrand.sharedwallet.data.Expense

class ShowExpenseViewModel(private val expenseId: String) : ViewModel() {

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