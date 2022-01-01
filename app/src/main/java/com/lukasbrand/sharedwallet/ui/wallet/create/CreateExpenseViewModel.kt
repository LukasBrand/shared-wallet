package com.lukasbrand.sharedwallet.ui.wallet.create

import android.location.Location
import androidx.lifecycle.*
import com.lukasbrand.sharedwallet.data.ExpenseParticipant
import com.lukasbrand.sharedwallet.data.User
import com.lukasbrand.sharedwallet.data.model.ExpenseApiModel
import com.lukasbrand.sharedwallet.repository.ExpensesRepository
import com.lukasbrand.sharedwallet.repository.UsersRepository
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class CreateExpenseViewModel(
    private val expensesRepository: ExpensesRepository,
    private val usersRepository: UsersRepository
) : ViewModel() {

    val owner = "" //TODO

    val name: MutableLiveData<String> = MutableLiveData("")

    val location: MutableLiveData<Location> = MutableLiveData()

    private val _creationDate: MutableLiveData<LocalDate> = MutableLiveData(LocalDate.now())
    val creationDate: LiveData<String>
        get() = _creationDate.map {
            val formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy")
            it.format(formatter)
        }

    fun setCreationDate(date: LocalDate) {
        _creationDate.value = date
    }

    private val _dueDate: MutableLiveData<LocalDate> = MutableLiveData(LocalDate.now())
    val dueDate: LiveData<String>
        get() = _dueDate.map {
            val formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy")
            it.format(formatter)
        }

    fun setDueDate(date: LocalDate) {
        _dueDate.value = date
    }

    val expenseAmountString: MutableLiveData<String> = MutableLiveData("0.00")

    val participants: MutableLiveData<MutableList<ExpenseParticipant>> = MutableLiveData()

    fun addParticipant(user: User) {
        participants.value?.add(ExpenseParticipant(user, 0, false))
    }

    fun createExpense() {
        viewModelScope.launch {
            val expenseApiModel = ExpenseApiModel(
                null,
                name.value!!,
                owner,
                location.value!!,
                Date(_creationDate.value!!.toEpochDay()),
                Date(_dueDate.value!!.toEpochDay()),
                BigDecimal(expenseAmountString.value!!),
                participants.value!!.map { expenseParticipant -> expenseParticipant.user.id },
                participants.value!!.map { expenseParticipant -> expenseParticipant.expensePercentage },
                participants.value!!.map { expenseParticipant -> expenseParticipant.hasPaid }
            )
            expensesRepository.addExpense(expenseApiModel)
        }
    }


}