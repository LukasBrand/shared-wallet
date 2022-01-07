package com.lukasbrand.sharedwallet.ui.wallet.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.type.LatLng
import com.lukasbrand.sharedwallet.data.Expense
import com.lukasbrand.sharedwallet.data.ExpenseParticipant
import com.lukasbrand.sharedwallet.repository.AuthenticationRepository
import com.lukasbrand.sharedwallet.repository.ExpensesRepository
import com.lukasbrand.sharedwallet.repository.UsersRepository
import com.lukasbrand.sharedwallet.types.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import java.math.BigDecimal
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class ListExpensesViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val expensesRepository: ExpensesRepository,
    private val usersRepository: UsersRepository,
) : ViewModel() {

    val userId: StateFlow<String> = flow<String> {
        val id = authenticationRepository.handleAuthentication()
        println("ID:" + id)
        emit(id)
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000),
        initialValue = ""
    )

    @ExperimentalCoroutinesApi
    val expenses: StateFlow<List<Expense>> = userId.flatMapLatest { userId ->
        println("HERE:" + userId)
        expensesRepository.getExpenses(userId)
    }.map { listOfExpenseApiModel ->
        println("HERE:" + listOfExpenseApiModel)
        listOfExpenseApiModel.map { expenseApiModel ->
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
                LatLng.newBuilder().setLatitude(expenseApiModel.location.latitude)
                    .setLongitude(expenseApiModel.location.longitude).build(),
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
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000),
        initialValue = listOf()
    )


    //Clickable Expense
    private val _navigateToExpenseDetail: MutableStateFlow<Navigator<String>> =
        MutableStateFlow(Navigator.Stay)
    val navigateToExpenseDetail: StateFlow<Navigator<String>>
        get() = _navigateToExpenseDetail

    fun onExpenseItemClicked(expenseId: String) {
        _navigateToExpenseDetail.value = Navigator.Navigate(expenseId)
    }

    fun onExpenseItemNavigated() {
        _navigateToExpenseDetail.value = Navigator.Stay
    }
}