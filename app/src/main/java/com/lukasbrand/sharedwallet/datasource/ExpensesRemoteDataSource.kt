package com.lukasbrand.sharedwallet.datasource

import com.lukasbrand.sharedwallet.data.model.ExpenseApiModel
import com.lukasbrand.sharedwallet.datasource.firestore.FirestoreApi
import com.lukasbrand.sharedwallet.types.Result
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

    suspend fun removeExpense(expenseApiModel: ExpenseApiModel): Unit = withContext(ioDispatcher) {
        firestoreApi.removeExpense(expenseApiModel)
    }

    @ExperimentalCoroutinesApi
    suspend fun getExpenses(authId: String): Flow<List<ExpenseApiModel>> =
        withContext(ioDispatcher) {
            firestoreApi.getExpenses(authId)
        }

}