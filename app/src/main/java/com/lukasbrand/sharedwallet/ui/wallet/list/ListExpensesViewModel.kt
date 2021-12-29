package com.lukasbrand.sharedwallet.ui.wallet.list

import android.app.Application
import androidx.lifecycle.*
import com.lukasbrand.sharedwallet.data.Expense
import com.lukasbrand.sharedwallet.data.User
import com.lukasbrand.sharedwallet.repository.ExpensesRepository
import com.lukasbrand.sharedwallet.repository.UsersRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map

class ListExpensesViewModel(
    private val expensesRepository: ExpensesRepository,
    private val usersRepository: UsersRepository,
) : ViewModel() {




    private val uiScope = viewModelScope

    val authId = ""

    @ExperimentalCoroutinesApi
    val expenses: LiveData<List<Expense>> = liveData {
        emitSource(expensesRepository.getExpenses(authId).map { listOfExpenseApiModel ->
            listOfExpenseApiModel.map { expenseApiModel ->
                val ownerUserApiModel = usersRepository.getUser(expenseApiModel.owner)
                val owner = User(
                    ownerUserApiModel.id,
                    ownerUserApiModel.name,
                    ownerUserApiModel.email,
                    ownerUserApiModel.image
                )
                val participants = expenseApiModel.participants.map { userId ->
                    val participant = usersRepository.getUser(userId)
                    User(
                        participant.id,
                        participant.name,
                        participant.email,
                        participant.image
                    )
                }

                Expense(
                    expenseApiModel.id!!,
                    expenseApiModel.name,
                    owner,
                    participants,
                    expenseApiModel.location,
                    expenseApiModel.creationDate,
                    expenseApiModel.dueDate,
                    expenseApiModel.expenseAmount,
                    expenseApiModel.participantExpensePercentage,
                    expenseApiModel.isPaid
                )
            }
        }.asLiveData())
    }


    //Clickable Expense
    private val _navigateToExpenseDetail = MutableLiveData<String?>()
    val navigateToExpenseDetail: LiveData<String?>
        get() = _navigateToExpenseDetail

    fun onExpenseItemClicked(expenseId: String) {
        _navigateToExpenseDetail.value = expenseId
    }

    fun onExpenseItemNavigated() {
        _navigateToExpenseDetail.value = null
    }
}