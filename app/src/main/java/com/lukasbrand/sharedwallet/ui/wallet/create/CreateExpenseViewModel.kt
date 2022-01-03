package com.lukasbrand.sharedwallet.ui.wallet.create

import android.location.Location
import androidx.lifecycle.*
import com.lukasbrand.sharedwallet.data.ExpenseParticipant
import com.lukasbrand.sharedwallet.data.User
import com.lukasbrand.sharedwallet.data.model.ExpenseApiModel
import com.lukasbrand.sharedwallet.repository.AuthenticationRepository
import com.lukasbrand.sharedwallet.repository.ExpensesRepository
import com.lukasbrand.sharedwallet.repository.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateExpenseViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val expensesRepository: ExpensesRepository,
    private val usersRepository: UsersRepository
) : ViewModel() {

    private val owner = liveData {
        emit(authenticationRepository.handleAuthentication().getOrThrow())
    }

    val name: MutableLiveData<String> = MutableLiveData("")

    val location: MutableLiveData<Location> = MutableLiveData()

    private val _creationDate: MutableLiveData<LocalDate> = MutableLiveData()
    val creationDate: LiveData<String> = liveData {
        emit("Creation Date")
        emitSource(_creationDate.map {
            val formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy")
            it.format(formatter)
        })
    }

    fun setCreationDate(date: LocalDate) {
        _creationDate.value = date
    }

    private val _dueDate: MutableLiveData<LocalDate> = MutableLiveData()
    val dueDate: LiveData<String> = liveData {
        emit("Due Date")
        emitSource(_dueDate.map {
            val formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy")
            it.format(formatter)
        })
    }

    fun setDueDate(date: LocalDate) {
        _dueDate.value = date
    }

    val expenseAmountString: MutableLiveData<String> = MutableLiveData("0.00")

    fun createExpense() {
        viewModelScope.launch {
            val expenseApiModel = ExpenseApiModel(
                null,
                name.value!!,
                owner.value!!,
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


    private val _participants: MutableLiveData<MutableList<ExpenseParticipant>> =
        MutableLiveData(mutableListOf())

    val participants: LiveData<List<ExpenseParticipant>>
        get() = _participants.map { it as List<ExpenseParticipant> }

    fun addParticipant() {
        _participants.value!!.add(ExpenseParticipant(user, 0, false))
    }

    fun removeParticipant(participantId: String) {
        _participants.value!!.removeIf {
            it.user.id == participantId
        }
    }

    val email: MutableLiveData<String> = MutableLiveData()

    private val _user: MutableLiveData<User> = MutableLiveData(null)
    val user: User
        get() = _user.value!!
    val userExists: LiveData<Boolean>
        get() = liveData {
            emitSource(_user.map { user ->
                user != null
            })
        }

    fun searchForUser(email: String) {
        viewModelScope.launch {
            _user.value = usersRepository.getUserIdFromEmail(email)
        }
    }

    fun participantHasPaid(participantId: String, paid: Boolean) {
        TODO("Not yet implemented")
    }

    fun participantPercentage(participantId: String, percent: Int) {
        TODO("Not yet implemented")
    }
}