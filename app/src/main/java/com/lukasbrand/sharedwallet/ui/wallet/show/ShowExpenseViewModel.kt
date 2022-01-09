package com.lukasbrand.sharedwallet.ui.wallet.show

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.lukasbrand.sharedwallet.data.Expense
import com.lukasbrand.sharedwallet.data.ExpenseParticipant
import com.lukasbrand.sharedwallet.data.repository.AuthenticationRepository
import com.lukasbrand.sharedwallet.data.repository.ExpensesRepository
import com.lukasbrand.sharedwallet.data.repository.UsersRepository
import com.lukasbrand.sharedwallet.services.message.MessageSendService
import com.lukasbrand.sharedwallet.types.Navigator
import com.lukasbrand.sharedwallet.utils.convertTimestampToFormatted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class ShowExpenseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authenticationRepository: AuthenticationRepository,
    private val usersRepository: UsersRepository,
    private val expensesRepository: ExpensesRepository,
    private val messageSendService: MessageSendService
) : ViewModel() {

    @ExperimentalCoroutinesApi
    val expense: StateFlow<Expense> = flow {
        val initialExpense: Expense = savedStateHandle["expense"]!!
        val expense =
            expensesRepository.getExpense(initialExpense.id).mapLatest { expenseApiModel ->
                if (expenseApiModel != null) {
                    val owner = usersRepository.getUser(expenseApiModel.owner)
                    val participants = expenseApiModel.participants.mapIndexed { index, userId ->
                        val participant = usersRepository.getUser(userId)
                        ExpenseParticipant(
                            participant,
                            expenseApiModel.participantExpensePercentage[index],
                            expenseApiModel.hasPaid[index],
                            participant.id == owner.id
                        )
                    }
                    Expense(
                        expenseApiModel.id!!,
                        expenseApiModel.name,
                        owner,
                        LatLng(
                            expenseApiModel.location.latitude,
                            expenseApiModel.location.longitude
                        ),
                        expenseApiModel.creationDate.toDate().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime(),
                        expenseApiModel.dueDate.toDate().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime(),
                        BigDecimal(expenseApiModel.expenseAmount),
                        participants
                    )
                } else {
                    initialExpense
                }
            }
        emitAll(expense)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = savedStateHandle["expense"]!!
    )

    @ExperimentalCoroutinesApi
    val isOwner: StateFlow<Boolean> = expense.mapLatest {
        val userId = authenticationRepository.handleAuthentication()
        it.owner.id == userId
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    @ExperimentalCoroutinesApi
    val name: StateFlow<String> = expense.mapLatest {
        it.name
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    @ExperimentalCoroutinesApi
    val location: StateFlow<LatLng> = expense.mapLatest {
        it.location
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LatLng(0.0, 0.0)
    )

    @ExperimentalCoroutinesApi
    val creationDate: StateFlow<String> = expense.mapLatest {
        convertTimestampToFormatted(it.creationDate).toString()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    @ExperimentalCoroutinesApi
    val dueDate: StateFlow<String> = expense.mapLatest {
        convertTimestampToFormatted(it.dueDate).toString()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    @ExperimentalCoroutinesApi
    val expenseAmount: StateFlow<String> = expense.mapLatest {
        it.expenseAmount.divide(BigDecimal(participants.value.size))
            .setScale(2, BigDecimal.ROUND_HALF_EVEN)
            .toPlainString() + " / " + it.expenseAmount.toPlainString()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    @ExperimentalCoroutinesApi
    val participants: StateFlow<List<ExpenseParticipant>> = expense.mapLatest {
        it.participants
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = listOf()
    )


    @ExperimentalCoroutinesApi
    fun onExpensePaidClicked() {
        viewModelScope.launch {
            val userId = authenticationRepository.handleAuthentication()
            val user = usersRepository.getUser(userId)
            val participants = expense.value.participants.toMutableList()

            participants.replaceAll {
                if (it.user.id == userId) {
                    it.copy(hasPaid = !it.hasPaid)
                } else {
                    it
                }
            }
            expensesRepository.modifyExpense(expense.value.copy(participants = participants))

            //Inform the owner about the update except it is himself
            if (expense.value.owner.id != userId) {
                val ownerNotificationToken = expense.value.owner.notificationToken
                messageSendService.sendNotificationToUser(
                    ownerNotificationToken,
                    "${user.name} has paid for ${expense.value.name}!",
                    "Your participant ${user.name} has paid the expense ${expense.value.name}"
                )
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun onExpenseRemoveClicked() {
        viewModelScope.launch {
            val expenseName = expense.value.name
            val ownerName = expense.value.owner.name
            val notificationTokens = expense.value.participants.mapNotNull {
                if (expense.value.owner.id != it.user.id) {
                    it.user.notificationToken
                } else {
                    null
                }
            }
            expensesRepository.removeExpense(expense.value.id)

            //Inform the owner about the update except it is himself
            for (notificationToken in notificationTokens) {
                messageSendService.sendNotificationToUser(
                    notificationToken,
                    "Expense $expenseName removed!",
                    "$ownerName has removed $expenseName"
                )
            }
            _navigateToListExpenses.value = Navigator.Navigate(Unit)
        }
    }


    private val _navigateToExpenseUpdate: MutableStateFlow<Navigator<Expense>> =
        MutableStateFlow(Navigator.Stay)
    val navigateToExpenseUpdate: StateFlow<Navigator<Expense>>
        get() = _navigateToExpenseUpdate

    @ExperimentalCoroutinesApi
    fun onExpenseUpdateClicked() {
        _navigateToExpenseUpdate.value = Navigator.Navigate(expense.value)
    }

    fun onExpenseUpdateNavigated() {
        _navigateToExpenseUpdate.value = Navigator.Stay
    }


    private val _navigateToListExpenses: MutableStateFlow<Navigator<Unit>> =
        MutableStateFlow(Navigator.Stay)
    val navigateToListExpenses: StateFlow<Navigator<Unit>>
        get() = _navigateToListExpenses

    fun onListExpensesNavigated() {
        _navigateToListExpenses.value = Navigator.Stay
    }
}