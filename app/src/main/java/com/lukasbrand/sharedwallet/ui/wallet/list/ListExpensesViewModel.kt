package com.lukasbrand.sharedwallet.ui.wallet.list

import androidx.lifecycle.*
import com.google.type.LatLng
import com.lukasbrand.sharedwallet.data.Expense
import com.lukasbrand.sharedwallet.data.ExpenseParticipant
import com.lukasbrand.sharedwallet.exception.NotLoggedInException
import com.lukasbrand.sharedwallet.repository.AuthenticationRepository
import com.lukasbrand.sharedwallet.repository.ExpensesRepository
import com.lukasbrand.sharedwallet.repository.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class ListExpensesViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val expensesRepository: ExpensesRepository,
    private val usersRepository: UsersRepository,
) : ViewModel() {

    val userId: LiveData<String?> = liveData {
        emit(
            try {
                authenticationRepository.handleAuthentication().getOrNull()
            } catch (e: NotLoggedInException) {
                null
            }
        )
    }

    @ExperimentalCoroutinesApi
    val expenses: LiveData<List<Expense>> = liveData {
        if (userId.value == null) return@liveData
        emitSource(expensesRepository.getExpenses(userId.value!!).map { listOfExpenseApiModel ->
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
                    expenseApiModel.expenseAmount,
                    participants
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