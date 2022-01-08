package com.lukasbrand.sharedwallet.ui.wallet.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.lukasbrand.sharedwallet.data.Expense
import com.lukasbrand.sharedwallet.data.ExpenseParticipant
import com.lukasbrand.sharedwallet.exhaustive
import com.lukasbrand.sharedwallet.data.repository.AuthenticationRepository
import com.lukasbrand.sharedwallet.data.repository.ExpensesRepository
import com.lukasbrand.sharedwallet.data.repository.UsersRepository
import com.lukasbrand.sharedwallet.types.Navigator
import com.lukasbrand.sharedwallet.types.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class ListExpensesViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val expensesRepository: ExpensesRepository,
    private val usersRepository: UsersRepository,
) : ViewModel() {

    val userId: StateFlow<Result<String>> = flow {
        try {
            val id = authenticationRepository.handleAuthentication()
            println("ID:" + id)
            emit(Result.Success(id))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000),
        initialValue = Result.Loading
    )

    @ExperimentalCoroutinesApi
    val expenses: StateFlow<List<Expense>> = userId.flatMapLatest { userIdResult ->
        println("HERE:" + userIdResult)
        val userId = when (userIdResult) {
            is Result.Success -> {
                userIdResult.data
            }
            is Result.Error -> ""
            Result.Loading -> ""
        }.exhaustive
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
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000),
        initialValue = listOf()
    )


    //Clickable Expense
    private val _navigateToExpenseDetail: MutableStateFlow<Navigator<Expense>> =
        MutableStateFlow(Navigator.Stay)
    val navigateToExpenseDetail: StateFlow<Navigator<Expense>>
        get() = _navigateToExpenseDetail

    fun onExpenseItemClicked(expense: Expense) {
        _navigateToExpenseDetail.value = Navigator.Navigate(expense)
    }

    fun onExpenseItemNavigated() {
        _navigateToExpenseDetail.value = Navigator.Stay
    }


    //Log out of account
    private val _navigateToTitle: MutableStateFlow<Navigator<Unit>> =
        MutableStateFlow(Navigator.Stay)
    val navigateToTitle: StateFlow<Navigator<Unit>>
        get() = _navigateToTitle

    fun onLogOut() {
        viewModelScope.launch {
            authenticationRepository.signOut()
        }
        _navigateToTitle.value = Navigator.Navigate(Unit)
    }

    fun onTitleNavigated() {
        _navigateToTitle.value = Navigator.Stay
    }
}