package com.lukasbrand.sharedwallet.ui.wallet.create

import android.app.DatePickerDialog
import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukasbrand.sharedwallet.data.User
import com.lukasbrand.sharedwallet.data.model.ExpenseApiModel
import com.lukasbrand.sharedwallet.repository.ExpensesRepository
import com.lukasbrand.sharedwallet.repository.UsersRepository
import kotlinx.coroutines.launch
import java.util.*

class CreateExpenseViewModel(
    private val expensesRepository: ExpensesRepository,
    private val usersRepository: UsersRepository
) : ViewModel() {

    val owner = "" //TODO
    val isPaid: List<Boolean> = listOf()

    val name: MutableLiveData<String> = MutableLiveData("")
    val participants: MutableLiveData<List<User>> = MutableLiveData(listOf())
    val location: MutableLiveData<Location> = MutableLiveData()
    val creationDate: MutableLiveData<Date> = MutableLiveData(Date(System.currentTimeMillis()))
    val dueDate: MutableLiveData<Date> = MutableLiveData(Date(System.currentTimeMillis()))
    val expenseAmount: MutableLiveData<Currency> = MutableLiveData()
    val participantExpensePercentage: MutableLiveData<List<Double>> = MutableLiveData()

    fun createExpense() {
        viewModelScope.launch {
            val expenseApiModel = ExpenseApiModel(
                null,
                name.value!!,
                owner,
                participants.value!!.map { user -> user.id },
                location.value!!,
                creationDate.value!!,
                dueDate.value!!,
                expenseAmount.value!!,
                participantExpensePercentage.value!!,
                isPaid
            )
            expensesRepository.addExpense(expenseApiModel)
        }
    }


}