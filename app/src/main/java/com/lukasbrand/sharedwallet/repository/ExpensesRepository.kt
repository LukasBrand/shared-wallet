package com.lukasbrand.sharedwallet.repository

import com.lukasbrand.sharedwallet.data.Expense
import com.lukasbrand.sharedwallet.data.model.ExpenseApiModel
import com.lukasbrand.sharedwallet.datasource.ExpensesRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ExpensesRepository(private val expensesRemoteDataSource: ExpensesRemoteDataSource) {

    suspend fun addExpense(expenseApiModel: ExpenseApiModel): Unit =
        expensesRemoteDataSource.addExpense(expenseApiModel)

    suspend fun modifyExpense(expenseApiModel: ExpenseApiModel): Unit =
        expensesRemoteDataSource.modifyExpense(expenseApiModel)

    suspend fun removeExpense(expenseApiModel: ExpenseApiModel): Unit =
        expensesRemoteDataSource.removeExpense(expenseApiModel)

    suspend fun getExpenses(): Flow<List<ExpenseApiModel>> =
        expensesRemoteDataSource.getExpenses()

}