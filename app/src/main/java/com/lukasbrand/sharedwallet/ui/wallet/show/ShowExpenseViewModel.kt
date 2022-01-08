package com.lukasbrand.sharedwallet.ui.wallet.show

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.lukasbrand.sharedwallet.data.Expense
import com.lukasbrand.sharedwallet.data.ExpenseParticipant
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
    usersRepository: UsersRepository,
    expensesRepository: ExpensesRepository,
    savedStateHandle: SavedStateHandle,
    private val messageSendService: MessageSendService
) : ViewModel() {

    @ExperimentalCoroutinesApi
    val expense: StateFlow<Expense> = flow {
        val initialExpense: Expense = savedStateHandle["expense"]!!
        val expense =
            expensesRepository.getExpense(initialExpense.id).mapLatest { expenseApiModel ->
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
                    LatLng(expenseApiModel.location.latitude, expenseApiModel.location.longitude),
                    expenseApiModel.creationDate.toDate().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime(),
                    expenseApiModel.dueDate.toDate().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime(),
                    BigDecimal(expenseApiModel.expenseAmount),
                    participants
                )
            }
        emitAll(expense)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = savedStateHandle["expense"]!!
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
        it.expenseAmount.toPlainString()
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


    //Clickable Expense
    private val _navigateToExpenseUpdate: MutableStateFlow<Navigator<Expense>> =
        MutableStateFlow(Navigator.Stay)
    val navigateToExpenseUpdate: StateFlow<Navigator<Expense>>
        get() = _navigateToExpenseUpdate

    @ExperimentalCoroutinesApi
    fun onExpenseUpdateClicked() {
        println(expense.value)
        _navigateToExpenseUpdate.value = Navigator.Navigate(expense.value)
    }

    fun onExpenseUpdateNavigated() {
        _navigateToExpenseUpdate.value = Navigator.Stay
    }


    @ExperimentalCoroutinesApi
    fun onExpensePaidClicked() {
        viewModelScope.launch {
            val ownerNotificationToken = expense.value.owner.notificationToken

            messageSendService.sendNotificationToUser(
                ownerNotificationToken,
                "Someone has paid!",
                "User this and that has paid"
            )
        }
    }

}