package com.lukasbrand.sharedwallet.ui.wallet.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.type.LatLng
import com.lukasbrand.sharedwallet.data.Expense
import com.lukasbrand.sharedwallet.data.ExpenseParticipant
import com.lukasbrand.sharedwallet.data.Navigator
import com.lukasbrand.sharedwallet.data.Result
import com.lukasbrand.sharedwallet.exhaustive
import com.lukasbrand.sharedwallet.repository.AuthenticationRepository
import com.lukasbrand.sharedwallet.repository.ExpensesRepository
import com.lukasbrand.sharedwallet.repository.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CreateExpenseViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val expensesRepository: ExpensesRepository,
    private val usersRepository: UsersRepository
) : ViewModel() {

    private val _expense: MutableStateFlow<Result<Expense>> = MutableStateFlow(Result.Loading)
    val expense: StateFlow<Result<Expense>> = _expense

    init {
        viewModelScope.launch {
            val userId = authenticationRepository.handleAuthentication().getOrThrow()
            val queriedOwner = usersRepository.getUser(userId)
            val participants = listOf(
                ExpenseParticipant(queriedOwner, 100, hasPaid = false, isOwner = true)
            )
            val initialExpense = Expense(owner = queriedOwner, participants = participants)
            _expense.value = Result.Success(initialExpense)
        }
    }


    private val _locationQuery: MutableStateFlow<String> = MutableStateFlow("")
    val locationQuery: StateFlow<String>
        get() = _locationQuery

    fun setLocationQuery(locationQuery: String) {
        _locationQuery.value = locationQuery
    }


    private val _participantEmail: MutableStateFlow<String> = MutableStateFlow("")
    val participantEmail: StateFlow<String>
        get() = _participantEmail

    fun setParticipantEmail(email: String) {
        _participantEmail.value = email
    }

    @ExperimentalCoroutinesApi
    val potentialParticipant: StateFlow<Result<ExpenseParticipant>>
        get() = _participantEmail.mapLatest { email ->
            usersRepository.getUserFromEmail(email)
        }.map { user ->
            when (user) {
                is Result.Success -> Result.Success(ExpenseParticipant(user.data, 0, false, false))
                is Result.Error -> Result.Error(user.exception)
                Result.Loading -> Result.Loading
            }.exhaustive
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Result.Loading
        )


    fun setName(name: String) {
        when (val value: Result<Expense> = _expense.value) {
            is Result.Success -> _expense.value = Result.Success(value.data.copy(name = name))
            else -> {}
        }
    }

    fun setLocation(latLng: LatLng) {
        when (val value: Result<Expense> = _expense.value) {
            is Result.Success -> _expense.value = Result.Success(value.data.copy(location = latLng))
            else -> {}
        }
    }

    fun setCreationDate(date: LocalDateTime) {
        when (val value: Result<Expense> = _expense.value) {
            is Result.Success -> _expense.value = Result.Success(
                value.data.copy(creationDate = date)
            )
            else -> {}
        }
    }

    fun setDueDate(date: LocalDateTime) {
        when (val value: Result<Expense> = _expense.value) {
            is Result.Success -> _expense.value = Result.Success(
                value.data.copy(dueDate = date)
            )
            else -> {}
        }
    }

    fun setExpenseAmount(price: BigDecimal) {
        when (val value: Result<Expense> = _expense.value) {
            is Result.Success -> _expense.value = Result.Success(
                value.data.copy(expenseAmount = price)
            )
            else -> {}
        }
    }

    @ExperimentalCoroutinesApi
    fun addParticipant() {
        when (val expense: Result<Expense> = _expense.value) {
            is Result.Success -> {
                when (val participant: Result<ExpenseParticipant> = potentialParticipant.value) {
                    is Result.Success -> {
                        val mutableParticipants = expense.data.participants.toMutableList()
                        mutableParticipants.add(participant.data)
                        _expense.value = Result.Success(
                            expense.data.copy(participants = mutableParticipants)
                        )
                    }
                    else -> {/* Potential participant does not exist or loads */
                    }
                }
            }
            else -> {/* Expense does not exist or loads */
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun removeParticipant(participantId: String) {
        when (val expense: Result<Expense> = _expense.value) {
            is Result.Success -> {
                val mutableParticipants = expense.data.participants.toMutableList()
                mutableParticipants.removeIf { participant -> participant.user.id == participantId }
                _expense.value = Result.Success(
                    expense.data.copy(participants = mutableParticipants)
                )
            }
            else -> {/* Expense does not exist or loads */
            }
        }
    }

    fun setParticipantHasPaid(participantId: String, paid: Boolean) {
        TODO("Not yet implemented")
    }

    fun setParticipantPercentage(participantId: String, percent: Int) {
        TODO("Not yet implemented")
    }

    fun createExpense() {
        _createExpenseCompleted.value = Navigator.Loading
        viewModelScope.launch {
            _expense.collectLatest { expenseResult: Result<Expense> ->
                when (expenseResult) {
                    is Result.Success -> {
                        try {
                            expensesRepository.addExpense(expenseResult.data)
                            _createExpenseCompleted.value = Navigator.Navigate(Unit)
                        } catch (exception: Exception) {
                            _createExpenseCompleted.value = Navigator.Error(exception)
                        }
                    }
                    is Result.Error -> _createExpenseCompleted.value =
                        Navigator.Error(expenseResult.exception)
                    Result.Loading -> {}
                }.exhaustive
            }
        }
    }


    private val _createExpenseCompleted: MutableStateFlow<Navigator<Unit>> =
        MutableStateFlow(Navigator.Stay)
    val createExpenseCompleted: StateFlow<Navigator<Unit>>
        get() = _createExpenseCompleted

    fun onNavigatedAfterExpenseCompleted() {
        _createExpenseCompleted.value = Navigator.Stay
    }
}