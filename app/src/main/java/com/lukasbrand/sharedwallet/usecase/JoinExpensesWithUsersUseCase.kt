package com.lukasbrand.sharedwallet.usecase

import com.lukasbrand.sharedwallet.data.Expense
import com.lukasbrand.sharedwallet.data.model.ExpenseApiModel
import com.lukasbrand.sharedwallet.repository.ExpensesRepository
import com.lukasbrand.sharedwallet.repository.UsersRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class JoinExpensesWithUsersUseCase(
    private val expensesRepository: ExpensesRepository,
    private val usersRepository: UsersRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    /* This might very well not be necessary as each expense can query its users and is aware of their ids


    suspend operator fun invoke(): Flow<Expense> = withContext(defaultDispatcher) {
        expensesRepository.getExpenses().map { expenseApiModel: ExpenseApiModel ->
            usersRepository.getUsers(*expenseApiModel.participants.toTypedArray()).

            Expense(expenseApiModel.id, expenseApiModel.name, )

        }
    }*/
}