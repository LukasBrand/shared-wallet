package com.lukasbrand.sharedwallet.ui.wallet.create

import androidx.lifecycle.*
import com.google.type.LatLng
import com.lukasbrand.sharedwallet.data.Expense
import com.lukasbrand.sharedwallet.data.ExpenseParticipant
import com.lukasbrand.sharedwallet.data.Result
import com.lukasbrand.sharedwallet.data.User
import com.lukasbrand.sharedwallet.exhaustive
import com.lukasbrand.sharedwallet.repository.AuthenticationRepository
import com.lukasbrand.sharedwallet.repository.ExpensesRepository
import com.lukasbrand.sharedwallet.repository.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class CreateExpenseViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val expensesRepository: ExpensesRepository,
    private val usersRepository: UsersRepository
) : ViewModel() {

    private val _expense: MutableStateFlow<Result<Expense>> = MutableStateFlow(Result.Loading)
    val expense: StateFlow<Result<Expense>> = _expense

    private val _eLocationQuery: MutableStateFlow<String> = MutableStateFlow("")
    val eLocationQuery: StateFlow<String>
        get() = _eLocationQuery

    fun setELocationQuery(locationQuery: String) {
        _eLocationQuery.value = locationQuery
    }


    private val _eParticipantEmail: MutableStateFlow<String> = MutableStateFlow("")
    val eParticipantEmail: StateFlow<String>
        get() = _eParticipantEmail

    fun setEParticipantEmail(email: String) {
        _eParticipantEmail.value = email
    }

    @ExperimentalCoroutinesApi
    val ePotentialParticipant: StateFlow<Result<ExpenseParticipant>>
        get() = _eParticipantEmail.mapLatest { email ->
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

    /*
    val eName: StateFlow<Result<String>>
        get() = _expense.map { expenseResult ->
            when (expenseResult) {
                is Result.Success -> Result.Success(expenseResult.data.name)
                is Result.Error -> Result.Error(expenseResult.exception)
                Result.Loading -> Result.Loading
            }.exhaustive
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Result.Loading
        )
     */

    fun setEName(name: String) {
        when (val value: Result<Expense> = _expense.value) {
            is Result.Success -> _expense.value = Result.Success(value.data.copy(name = name))
            else -> {}
        }
    }


    /*
    val eLocation: StateFlow<Result<LatLng>>
        get() = _expense.map { expenseResult ->
            when (expenseResult) {
                is Result.Success -> Result.Success(expenseResult.data.location)
                is Result.Error -> Result.Error(expenseResult.exception)
                Result.Loading -> Result.Loading
            }.exhaustive
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Result.Loading
        )
     */

    fun setELocation(latLng: LatLng) {
        when (val value: Result<Expense> = _expense.value) {
            is Result.Success -> _expense.value = Result.Success(value.data.copy(location = latLng))
            else -> {}
        }
    }

    /*
    val eCreationDate: StateFlow<Result<LocalDateTime>>
        get() = _expense.map { expenseResult ->
            when (expenseResult) {
                is Result.Success -> Result.Success(expenseResult.data.creationDate)
                is Result.Error -> Result.Error(expenseResult.exception)
                Result.Loading -> Result.Loading
            }.exhaustive
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Result.Loading
        )
    */

    fun setECreationDate(date: LocalDateTime) {
        when (val value: Result<Expense> = _expense.value) {
            is Result.Success -> _expense.value = Result.Success(
                value.data.copy(creationDate = date)
            )
            else -> {}
        }
    }

    fun setEExpenseAmount(price: BigDecimal) {
        when (val value: Result<Expense> = _expense.value) {
            is Result.Success -> _expense.value = Result.Success(
                value.data.copy(expenseAmount = price)
            )
            else -> {}
        }
    }

    @ExperimentalCoroutinesApi
    fun addEParticipant() {
        when (val expense: Result<Expense> = _expense.value) {
            is Result.Success -> {
                when (val participant: Result<ExpenseParticipant> = ePotentialParticipant.value) {
                    is Result.Success -> {
                        val mutableParticipants = expense.data.participants.toMutableList()
                        mutableParticipants.add(participant.data)
                        _expense.value = Result.Success(
                            expense.data.copy(participants = mutableParticipants)
                        )
                    }
                    else -> {/* Potential participant does not exist or loads */}
                }
            }
            else -> {/* Expense does not exist or loads */}
        }
    }

    @ExperimentalCoroutinesApi
    fun removeEParticipant(participantId: String) {
        when (val expense: Result<Expense> = _expense.value) {
            is Result.Success -> {
                val mutableParticipants = expense.data.participants.toMutableList()
                mutableParticipants.removeIf { participant -> participant.user.id == participantId }
                _expense.value = Result.Success(
                    expense.data.copy(participants = mutableParticipants)
                )
            }
            else -> {/* Expense does not exist or loads */}
        }
    }


    val name: MutableLiveData<String> = MutableLiveData("")

    private val _locationQuery: MutableLiveData<String> = MutableLiveData()
    val locationQuery: LiveData<String>
        get() = _locationQuery

    fun setLocationQuery(locationName: String) {
        _locationQuery.value = locationName
    }


    private val _location: MutableLiveData<LatLng> = MutableLiveData()
    val location: LiveData<LatLng>
        get() = _location

    fun setLocation(latLng: LatLng) {
        _location.value = latLng
    }


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


    private val _priceAsString: MutableLiveData<String> = MutableLiveData("")
    val priceAsString: LiveData<String>
        get() = _priceAsString

    fun setPrice(price: String) {
        _priceAsString.value = price
    }

    private val owner: MutableLiveData<User> = MutableLiveData()

    init {
        viewModelScope.launch {
            val userId = authenticationRepository.handleAuthentication().getOrThrow()
            val queriedOwner = usersRepository.getUser(userId)

            owner.value = queriedOwner
            _participants.value = listOf(ExpenseParticipant(queriedOwner, 100, false, true))
        }
    }

    private val _participants: MutableLiveData<List<ExpenseParticipant>> = MutableLiveData()
    val participants: LiveData<List<ExpenseParticipant>>
        get() = _participants

    fun addParticipant() {
        val participants = _participants.value
        participants?.let { list ->
            val mutableList = list.toMutableList()
            mutableList.add(ExpenseParticipant(user, 0, false, false))
            _participants.value = mutableList
        }
    }

    fun removeParticipant(participantId: String) {
        val participants = _participants.value
        participants?.let { list ->
            val mutableList = list.toMutableList()
            mutableList.removeIf { participant -> participant.user.id == participantId }
            _participants.value = mutableList
        }
    }


    fun createExpense() {
        val expense = Expense(
            "ignored", //This value will be replaced by the automatically generated document value
            name.value!!,
            owner.value!!,
            location.value!!,
            _creationDate.value!!.atStartOfDay(),
            _dueDate.value!!.atStartOfDay(),
            BigDecimal(priceAsString.value!!),
            participants.value!!
        )
        viewModelScope.launch {
            expensesRepository.addExpense(expense)
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
            _user.value = when (val user = usersRepository.getUserFromEmail(email)) {
                is Result.Error -> TODO()
                Result.Loading -> TODO()
                is Result.Success -> user.data
            }
        }
    }

    fun participantHasPaid(participantId: String, paid: Boolean) {
        TODO("Not yet implemented")
    }

    fun participantPercentage(participantId: String, percent: Int) {
        TODO("Not yet implemented")
    }
}