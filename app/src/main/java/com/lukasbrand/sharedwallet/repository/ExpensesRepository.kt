package com.lukasbrand.sharedwallet.repository

import com.lukasbrand.sharedwallet.database.ExpensesDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExpensesRepository(private val expensesDao: ExpensesDao) {

    suspend fun refreshExpenses() {
        withContext(Dispatchers.Default) {

        }
    }
}