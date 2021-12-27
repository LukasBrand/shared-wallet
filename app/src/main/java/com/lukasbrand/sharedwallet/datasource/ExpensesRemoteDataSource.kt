package com.lukasbrand.sharedwallet.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.lukasbrand.sharedwallet.data.Expense
import com.lukasbrand.sharedwallet.data.model.ExpenseApiModel
import com.lukasbrand.sharedwallet.datasource.firestore.FirestoreApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
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

    suspend fun getExpenses(): Flow<List<ExpenseApiModel>> = withContext(ioDispatcher) {
        firestoreApi.getExpenses()
    }

}