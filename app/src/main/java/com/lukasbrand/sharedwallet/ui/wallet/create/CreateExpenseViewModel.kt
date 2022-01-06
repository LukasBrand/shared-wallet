package com.lukasbrand.sharedwallet.ui.wallet.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.type.LatLng
import com.lukasbrand.sharedwallet.data.*
import com.lukasbrand.sharedwallet.exception.ExpenseNotCompleteException
import com.lukasbrand.sharedwallet.exception.ParticipantAlreadyExistsException
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

    private val _expense: MutableStateFlow<Result<NewExpense>> = MutableStateFlow(Result.Loading)
    val expense: StateFlow<Result<NewExpense>> = _expense

    init {
        viewModelScope.launch {
            val userId = authenticationRepository.handleAuthentication().getOrThrow()
            val queriedOwner = usersRepository.getUser(userId)
            val participants = listOf(
                ExpenseParticipant(queriedOwner, 100, hasPaid = false, isOwner = true)
            )
            val initialExpense = NewExpense(owner = queriedOwner, participants = participants)
            _expense.value = Result.Success(initialExpense)
        }
    }


    private val _locationQuery: MutableStateFlow<String> = MutableStateFlow("")
    val locationQuery: StateFlow<String> = _locationQuery

    fun setLocationQuery(locationQuery: String) {
        _locationQuery.value = locationQuery
    }


    private val _participantEmail: MutableStateFlow<String> = MutableStateFlow("")
    val participantEmail: StateFlow<String> = _participantEmail

    fun setParticipantEmail(email: String) {
        _participantEmail.value = email
    }

    @ExperimentalCoroutinesApi
    val potentialParticipant: StateFlow<Result<ExpenseParticipant>> =
        _participantEmail.combine(expense) { email: String, expenseResult: Result<NewExpense> ->
            when (expenseResult) {
                is Result.Success -> {
                    return@combine if (expenseResult.data.participants.any { participant -> participant.user.email == email }) {
                        Result.Error(ParticipantAlreadyExistsException())
                    } else {
                        Result.Success(email)
                    }
                }
                is Result.Error -> Result.Error(expenseResult.exception)
                Result.Loading -> Result.Loading
            }.exhaustive
        }.transformLatest { emailResult: Result<String> ->
            emit(Result.Loading)
            emit(
                when (emailResult) {
                    is Result.Error -> Result.Error(emailResult.exception)
                    Result.Loading -> Result.Loading
                    is Result.Success -> usersRepository.getUserFromEmail(emailResult.data)
                }.exhaustive
            )
        }.map { user: Result<User> ->
            println(user.toString())
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


    val name: StateFlow<String> = _expense.map { expenseResult ->
        when (expenseResult) {
            is Result.Success -> expenseResult.data.name ?: ""
            is Result.Error -> ""
            Result.Loading -> ""
        }.exhaustive
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    fun setName(name: String) {
        when (val value: Result<NewExpense> = _expense.value) {
            is Result.Success -> _expense.value = Result.Success(value.data.copy(name = name))
            else -> {}
        }
    }

    fun setLocation(latLng: LatLng) {
        when (val value: Result<NewExpense> = _expense.value) {
            is Result.Success -> _expense.value = Result.Success(value.data.copy(location = latLng))
            else -> {}
        }
    }

    fun setCreationDate(date: LocalDateTime) {
        when (val value: Result<NewExpense> = _expense.value) {
            is Result.Success -> _expense.value = Result.Success(
                value.data.copy(creationDate = date)
            )
            else -> {}
        }
    }

    fun setDueDate(date: LocalDateTime) {
        when (val value: Result<NewExpense> = _expense.value) {
            is Result.Success -> _expense.value = Result.Success(
                value.data.copy(dueDate = date)
            )
            else -> {}
        }
    }

    val expenseAmount: StateFlow<String> = _expense.map { expenseResult ->
        when (expenseResult) {
            is Result.Success -> expenseResult.data.expenseAmount?.toPlainString() ?: ""
            is Result.Error -> ""
            Result.Loading -> ""
        }.exhaustive
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    fun setExpenseAmount(price: BigDecimal) {
        when (val value: Result<NewExpense> = _expense.value) {
            is Result.Success -> _expense.value = Result.Success(
                value.data.copy(expenseAmount = price)
            )
            else -> {}
        }
    }

    @ExperimentalCoroutinesApi
    fun addParticipant() {
        viewModelScope.launch {
            when (val expense: Result<NewExpense> = _expense.value) {
                is Result.Success -> {
                    when (val participant: Result<ExpenseParticipant> =
                        potentialParticipant.value) {
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
    }

    fun removeParticipant(participantId: String) {
        viewModelScope.launch {
            when (val expense: Result<NewExpense> = _expense.value) {
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
    }

    fun setParticipantHasPaid(participantId: String, paid: Boolean) {
        viewModelScope.launch {
            when (val expense: Result<NewExpense> = _expense.value) {
                is Result.Success -> {
                    val mutableParticipants = expense.data.participants.toMutableList()
                    mutableParticipants.map { participant ->
                        if (participant.user.id == participantId) {
                            participant.copy(hasPaid = paid)
                        } else {
                            participant
                        }
                    }
                    _expense.value = Result.Success(
                        expense.data.copy(participants = mutableParticipants)
                    )
                }
                else -> {/* Expense does not exist or loads */
                }
            }
        }
    }

    fun setParticipantPercentage(participantId: String, percent: Int) {
        viewModelScope.launch {
            when (val expense: Result<NewExpense> = _expense.value) {
                is Result.Success -> {
                    val mutableParticipants = expense.data.participants.toMutableList()
                    mutableParticipants.map { participant ->
                        if (participant.user.id == participantId) {
                            println("Tries to change percentage")
                            participant.copy(expensePercentage = percent)
                        } else {
                            participant
                        }
                    }
                    _expense.value =
                        Result.Success(expense.data.copy(participants = mutableParticipants))
                }
                else -> {/* Expense does not exist or loads */
                }
            }
        }
    }

    fun createExpense() {
        _createExpenseCompleted.value = Navigator.Loading
        viewModelScope.launch {
            _expense.collectLatest { expenseResult: Result<NewExpense> ->
                when (expenseResult) {
                    is Result.Success -> {
                        val newExpense = expenseResult.data
                        if (!newExpense.isComplete()) {
                            _createExpenseCompleted.value =
                                Navigator.Error(ExpenseNotCompleteException())
                        } else {
                            try {
                                expensesRepository.addExpense(newExpense)
                                _createExpenseCompleted.value = Navigator.Navigate(Unit)
                            } catch (exception: Exception) {
                                _createExpenseCompleted.value = Navigator.Error(exception)
                            }
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
    val createExpenseCompleted: StateFlow<Navigator<Unit>> = _createExpenseCompleted

    fun onNavigatedAfterExpenseCompleted() {
        _createExpenseCompleted.value = Navigator.Stay
    }
}