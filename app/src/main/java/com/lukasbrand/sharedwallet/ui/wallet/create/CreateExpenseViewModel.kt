package com.lukasbrand.sharedwallet.ui.wallet.create

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.type.LatLng
import com.lukasbrand.sharedwallet.data.ExpenseParticipant
import com.lukasbrand.sharedwallet.data.NewExpense
import com.lukasbrand.sharedwallet.data.User
import com.lukasbrand.sharedwallet.data.repository.AuthenticationRepository
import com.lukasbrand.sharedwallet.data.repository.ExpensesRepository
import com.lukasbrand.sharedwallet.data.repository.UsersRepository
import com.lukasbrand.sharedwallet.exception.ExpenseNotCompleteException
import com.lukasbrand.sharedwallet.exception.ParticipantAlreadyExistsException
import com.lukasbrand.sharedwallet.exhaustive
import com.lukasbrand.sharedwallet.services.message.MessageSendService
import com.lukasbrand.sharedwallet.types.Navigator
import com.lukasbrand.sharedwallet.types.Result
import com.lukasbrand.sharedwallet.types.UiState
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
    private val usersRepository: UsersRepository,
    private val messageSendService: MessageSendService
) : ViewModel() {

    init {
        viewModelScope.launch {
            val userId = authenticationRepository.handleAuthentication()
            val queriedOwner = usersRepository.getUser(userId)
            val participants = listOf(
                ExpenseParticipant(queriedOwner, 100, hasPaid = false, isOwner = true)
            )

            _owner.value = UiState.Set(queriedOwner)
            _participants.value = UiState.Set(participants)
        }
    }

    //two way binding. Therefore only a public mutable state flow
    val name: MutableStateFlow<UiState<String>> = MutableStateFlow(UiState.Unset)

    private val _owner: MutableStateFlow<UiState<User>> = MutableStateFlow(UiState.Unset)

    private val _location: MutableStateFlow<UiState<LatLng>> = MutableStateFlow(UiState.Unset)
    val location: StateFlow<UiState<LatLng>> = _location

    fun setLocation(latLng: LatLng) {
        _location.value = UiState.Set(latLng)
    }


    private val _creationDate: MutableStateFlow<UiState<LocalDateTime>> =
        MutableStateFlow(UiState.Unset)
    val creationDate: StateFlow<UiState<LocalDateTime>> = _creationDate

    val creationDateTime: MutableStateFlow<Boolean> = MutableStateFlow(false)

    fun setCreationDate(date: LocalDateTime) {
        _creationDate.value = UiState.Set(date)
        creationDateTime.value = true
    }

    fun setCreationDateTime(date: LocalDateTime) {
        _creationDate.value = UiState.Set(date)
        creationDateTime.value = false
    }


    private val _dueDate: MutableStateFlow<UiState<LocalDateTime>> =
        MutableStateFlow(UiState.Unset)
    val dueDate: StateFlow<UiState<LocalDateTime>> = _dueDate

    val dueDateTime: MutableStateFlow<Boolean> = MutableStateFlow(false);

    fun setDueDate(date: LocalDateTime) {
        _dueDate.value = UiState.Set(date)
        dueDateTime.value = true
    }

    fun setDueDateTime(date: LocalDateTime) {
        _dueDate.value = UiState.Set(date)
        dueDateTime.value = false
    }

    //two way binding. Therefore only a public mutable state flow
    val expenseAmount: MutableStateFlow<UiState<String>> = MutableStateFlow(UiState.Unset)

    private val _participants: MutableStateFlow<UiState<List<ExpenseParticipant>>> =
        MutableStateFlow(UiState.Unset)
    val participants: StateFlow<UiState<List<ExpenseParticipant>>> = _participants


    private val _locationQuery: MutableStateFlow<UiState<String>> = MutableStateFlow(UiState.Unset)
    val locationQuery: StateFlow<UiState<String>> = _locationQuery

    fun setLocationQuery(locationQuery: String) {
        _locationQuery.value = UiState.Set(locationQuery)
    }

    //two way binding. Therefore only a public mutable state flow
    val participantEmail: MutableStateFlow<UiState<String>> = MutableStateFlow(UiState.Unset)


    @ExperimentalCoroutinesApi
    val potentialParticipant: StateFlow<Result<ExpenseParticipant>> =
        _participants.combine(participantEmail) { participants: UiState<List<ExpenseParticipant>>, email: UiState<String> ->
            when (participants) {
                is UiState.Set -> {
                    when (email) {
                        is UiState.Set -> {
                            if (participants.data.any { participant -> participant.user.email == email.data }) {
                                Result.Error(ParticipantAlreadyExistsException())
                            } else {
                                Result.Success(email.data)
                            }
                        }
                        UiState.Unset -> Result.Error(IllegalStateException())
                    }.exhaustive
                }
                UiState.Unset -> Result.Error(IllegalStateException())
            }.exhaustive
        }.transformLatest { emailResult: Result<String> ->
            emit(Result.Loading)
            emit(
                when (emailResult) {
                    is Result.Success -> usersRepository.getUserFromEmail(emailResult.data)
                    is Result.Error -> Result.Error(emailResult.exception)
                    Result.Loading -> Result.Loading
                }.exhaustive
            )
        }.map { user: Result<User> ->
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

    @ExperimentalCoroutinesApi
    fun addParticipant() {
        viewModelScope.launch {
            when (val participants: UiState<List<ExpenseParticipant>> = _participants.value) {
                is UiState.Set -> {
                    when (val participant: Result<ExpenseParticipant> =
                        potentialParticipant.value) {
                        is Result.Success -> {
                            val mutableParticipants = participants.data.toMutableList()
                            mutableParticipants.add(participant.data)
                            _participants.value = UiState.Set(mutableParticipants)
                        }
                        else -> {/* Potential participant does not exist or loads */
                        }
                    }
                }
                UiState.Unset -> {}
            }.exhaustive
        }
    }

    fun removeParticipant(participantId: String) {
        viewModelScope.launch {
            when (val participants: UiState<List<ExpenseParticipant>> = _participants.value) {
                is UiState.Set -> {
                    val mutableParticipants = participants.data.toMutableList()
                    mutableParticipants.removeIf { participant -> participant.user.id == participantId }
                    _participants.value = UiState.Set(mutableParticipants)
                }
                UiState.Unset -> {}
            }.exhaustive
        }
    }

    fun setParticipantHasPaid(participantId: String, paid: Boolean) {
        viewModelScope.launch {
            when (val participants: UiState<List<ExpenseParticipant>> = _participants.value) {
                is UiState.Set -> {
                    val mutableParticipants = participants.data.toMutableList()
                    mutableParticipants.map { participant ->
                        if (participant.user.id == participantId) {
                            participant.copy(hasPaid = paid)
                        } else {
                            participant
                        }
                    }
                    _participants.value = UiState.Set(mutableParticipants)
                }
                UiState.Unset -> {}
            }.exhaustive
        }
    }

    fun setParticipantPercentage(participantId: String, percent: Int) {
        viewModelScope.launch {
            when (val participants: UiState<List<ExpenseParticipant>> = _participants.value) {
                is UiState.Set -> {
                    val mutableParticipants = participants.data.toMutableList()
                    mutableParticipants.map { participant ->
                        if (participant.user.id == participantId) {
                            participant.copy(expensePercentage = percent)
                        } else {
                            participant
                        }
                    }
                    _participants.value = UiState.Set(mutableParticipants)
                }
                UiState.Unset -> {}
            }.exhaustive
        }
    }

    fun createExpense() {
        _createExpenseCompleted.value = Navigator.Loading
        viewModelScope.launch {
            val name = when (val o = name.value) {
                is UiState.Set<String> -> o.data
                UiState.Unset -> {
                    _createExpenseCompleted.value =
                        Navigator.Error(ExpenseNotCompleteException("Missing name"))
                    return@launch
                }
            }.exhaustive

            val owner = when (val o = _owner.value) {
                is UiState.Set<User> -> o.data
                UiState.Unset -> {
                    _createExpenseCompleted.value =
                        Navigator.Error(ExpenseNotCompleteException("Missing owner"))
                    return@launch
                }
            }.exhaustive

            val location = when (val l = _location.value) {
                is UiState.Set<LatLng> -> l.data
                UiState.Unset -> {
                    _createExpenseCompleted.value =
                        Navigator.Error(ExpenseNotCompleteException("Missing location"))
                    return@launch
                }
            }.exhaustive

            val creationDate = when (val c = _creationDate.value) {
                is UiState.Set<LocalDateTime> -> c.data
                UiState.Unset -> {
                    _createExpenseCompleted.value =
                        Navigator.Error(ExpenseNotCompleteException("Missing creation date"))
                    return@launch
                }
            }.exhaustive

            val dueDate = when (val d = _dueDate.value) {
                is UiState.Set<LocalDateTime> -> d.data
                UiState.Unset -> {
                    _createExpenseCompleted.value =
                        Navigator.Error(ExpenseNotCompleteException("Missing due date"))
                    return@launch
                }
            }.exhaustive

            val expenseAmount = when (val a = expenseAmount.value) {
                is UiState.Set -> {
                    if (a.data.isNotBlank() && a.data.isDigitsOnly() || a.data.contains('.')) {
                        BigDecimal(a.data)
                    } else {
                        _createExpenseCompleted.value =
                            Navigator.Error(ExpenseNotCompleteException("Expense amount must be a number or .,"))
                        return@launch
                    }
                }
                UiState.Unset -> {
                    _createExpenseCompleted.value =
                        Navigator.Error(ExpenseNotCompleteException("Missing expense amount"))
                    return@launch
                }
            }.exhaustive

            val participants = when (val p = _participants.value) {
                is UiState.Set<List<ExpenseParticipant>> -> p.data
                UiState.Unset -> {
                    _createExpenseCompleted.value =
                        Navigator.Error(ExpenseNotCompleteException("Missing participants"))
                    return@launch
                }
            }.exhaustive

            //Dirty code to give every participant the same part of the expense amount
            val participantsCount = participants.size
            val percent: Int = 100 / participantsCount


            val participantsWithExpenseAmounts = participants.toMutableList().map {
                it.copy(expensePercentage = percent)
            }

            val newExpense = NewExpense(
                name = name,
                owner = owner,
                location = location,
                creationDate = creationDate,
                dueDate = dueDate,
                expenseAmount = expenseAmount,
                participants = participantsWithExpenseAmounts
            )

            try {
                expensesRepository.addExpense(newExpense)
                _createExpenseCompleted.value = Navigator.Navigate(Unit)
            } catch (exception: Exception) {
                _createExpenseCompleted.value = Navigator.Error(exception)
            }

            //Inform all participants except owner about the new expense
            for (participant in participants) {
                if (participant.user.id != owner.id) {
                    val ownerNotificationToken = participant.user.notificationToken
                    messageSendService.sendNotificationToUser(
                        ownerNotificationToken,
                        "New Expense created!",
                        "${owner.name} has created a new expense for $name and you are invited!"
                    )
                }
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