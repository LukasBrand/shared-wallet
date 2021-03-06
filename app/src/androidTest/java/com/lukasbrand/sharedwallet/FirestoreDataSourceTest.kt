package com.lukasbrand.sharedwallet

import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.gms.maps.model.LatLng
import com.lukasbrand.sharedwallet.data.Expense
import com.lukasbrand.sharedwallet.data.User
import com.lukasbrand.sharedwallet.data.datasource.ExpensesRemoteDataSource
import com.lukasbrand.sharedwallet.data.datasource.UsersRemoteDataSource
import com.lukasbrand.sharedwallet.data.datasource.firestore.FirestoreApi
import kotlinx.coroutines.Dispatchers
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class FirestoreDataSourceTest {

    private lateinit var expensesRemoteDataSource: ExpensesRemoteDataSource
    private lateinit var usersRemoteDataSource: UsersRemoteDataSource

    @BeforeEach
    fun createDatabase() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val firestoreApi = FirestoreApi.getInstance()

        expensesRemoteDataSource = ExpensesRemoteDataSource(firestoreApi, Dispatchers.IO)
        usersRemoteDataSource = UsersRemoteDataSource(firestoreApi, Dispatchers.IO)
    }

    @AfterEach
    fun closeDatabase() {

    }

    @Test
    fun insertAndGetExpense() {
        val ownerParticipant = User("1", "TestOwner", "test@mail.de", "", null)
        val expense = Expense(
            "1",
            "TestExpense",
            ownerParticipant,
            LatLng(0.0, 0.0),
            LocalDateTime.now(),
            LocalDateTime.now(),
            BigDecimal(""),
            listOf()
        )

        //expensesDao.addExpense(expense)


    }

}