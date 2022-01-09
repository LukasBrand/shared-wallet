package com.lukasbrand.sharedwallet.data.datasource

import com.lukasbrand.sharedwallet.data.model.ExpenseApiModel
import com.lukasbrand.sharedwallet.data.datasource.firestore.FirestoreApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ExpensesRemoteDataSource(
    private val firestoreApi: FirestoreApi,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun addExpense(expenseApiModel: ExpenseApiModel): Unit = withContext(ioDispatcher) {
        firestoreApi.addExpense(expenseApiModel)
    }

    suspend fun modifyExpense(expenseApiModel: ExpenseApiModel): Unit = withContext(ioDispatcher) {
        firestoreApi.modifyExpense(expenseApiModel)
    }

    suspend fun removeExpense(expenseId: String): Unit = withContext(ioDispatcher) {
        firestoreApi.removeExpense(expenseId)
    }

    @ExperimentalCoroutinesApi
    suspend fun getExpenses(authId: String): Flow<List<ExpenseApiModel>> =
        withContext(ioDispatcher) {
            firestoreApi.getExpenses(authId)
        }

    @ExperimentalCoroutinesApi
    suspend fun getExpense(expenseId: String): Flow<ExpenseApiModel?> = withContext(ioDispatcher) {
        firestoreApi.getExpense(expenseId)
    }

}