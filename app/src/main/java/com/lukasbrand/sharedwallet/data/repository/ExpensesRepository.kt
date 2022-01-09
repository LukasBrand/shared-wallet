package com.lukasbrand.sharedwallet.data.repository

import com.lukasbrand.sharedwallet.data.Expense
import com.lukasbrand.sharedwallet.data.NewExpense
import com.lukasbrand.sharedwallet.data.model.ExpenseApiModel
import com.lukasbrand.sharedwallet.data.datasource.ExpensesRemoteDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

class ExpensesRepository(private val expensesRemoteDataSource: ExpensesRemoteDataSource) {

    suspend fun addExpense(expense: NewExpense) {
        val expenseApiModel = ExpenseApiModel(expense)
        expensesRemoteDataSource.addExpense(expenseApiModel)
    }

    suspend fun modifyExpense(expense: Expense) {
        val expenseApiModel = ExpenseApiModel(expense)
        expensesRemoteDataSource.modifyExpense(expenseApiModel)
    }

    suspend fun removeExpense(expenseId: String) {
        expensesRemoteDataSource.removeExpense(expenseId)
    }

    @ExperimentalCoroutinesApi
    suspend fun getExpenses(authId: String): Flow<List<ExpenseApiModel>> =
        expensesRemoteDataSource.getExpenses(authId)

    @ExperimentalCoroutinesApi
    suspend fun getExpense(expenseId: String): Flow<ExpenseApiModel?> =
        expensesRemoteDataSource.getExpense(expenseId)


}

