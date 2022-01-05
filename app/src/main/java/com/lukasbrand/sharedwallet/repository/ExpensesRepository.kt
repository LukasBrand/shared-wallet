package com.lukasbrand.sharedwallet.repository

import com.lukasbrand.sharedwallet.data.Expense
import com.lukasbrand.sharedwallet.data.model.ExpenseApiModel
import com.lukasbrand.sharedwallet.datasource.ExpensesRemoteDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
class ExpensesRepository(private val expensesRemoteDataSource: ExpensesRemoteDataSource) {

    suspend fun addExpense(expense: Expense) {
        val expenseApiModel = ExpenseApiModel(expense)
        expensesRemoteDataSource.addExpense(expenseApiModel)
    }

    suspend fun modifyExpense(expense: Expense) {
        val expenseApiModel = ExpenseApiModel(expense)
        expensesRemoteDataSource.modifyExpense(expenseApiModel)
    }

    suspend fun removeExpense(expense: Expense) {
        val expenseApiModel = ExpenseApiModel(expense)
        expensesRemoteDataSource.removeExpense(expenseApiModel)
    }

    @ExperimentalCoroutinesApi
    suspend fun getExpenses(authId: String): Flow<List<ExpenseApiModel>> =
        expensesRemoteDataSource.getExpenses(authId)

}

