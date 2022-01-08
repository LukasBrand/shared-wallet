package com.lukasbrand.sharedwallet.usecase

import com.lukasbrand.sharedwallet.data.repository.ExpensesRepository
import com.lukasbrand.sharedwallet.data.repository.UsersRepository
import javax.inject.Inject

class GetExpensesUseCase @Inject constructor(
    private val expensesRepository: ExpensesRepository,
    private val usersRepository: UsersRepository
) {
    /* This might very well not be necessary as each expense can query its users and is aware of their ids


    suspend operator fun invoke(): Flow<Expense> = withContext(defaultDispatcher) {
        expensesRepository.getExpenses().map { expenseApiModel: ExpenseApiModel ->
            usersRepository.getUsers(*expenseApiModel.participants.toTypedArray()).

            Expense(expenseApiModel.id, expenseApiModel.name, )

        }
    }*/
}