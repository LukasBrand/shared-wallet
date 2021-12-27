package com.lukasbrand.sharedwallet.datasource.firestore

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.lukasbrand.sharedwallet.data.model.ExpenseApiModel
import com.lukasbrand.sharedwallet.data.model.UserApiModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirestoreApi(private val firebaseFirestore: FirebaseFirestore) {

    companion object {
        private var USER_COLLECTION = "users"


        @Volatile
        private var INSTANCE: FirestoreApi? = null

        fun getInstance(context: Context): FirestoreApi {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = FirestoreApi(FirebaseFirestore.getInstance())
                }

                return instance
            }
        }
    }


    fun addExpense(expenseApiModel: ExpenseApiModel) {
        TODO("Not yet implemented")
    }

    fun modifyExpense(expenseApiModel: ExpenseApiModel) {
        TODO("Not yet implemented")
    }

    fun removeExpense(expenseApiModel: ExpenseApiModel) {
        TODO("Not yet implemented")
    }

    fun getExpenses(): Flow<List<ExpenseApiModel>> {
        TODO("Not yet implemented")
    }

    fun addUser(userApiModel: UserApiModel) {
        TODO("Not yet implemented")
    }

    fun modifyUser(userApiModel: UserApiModel) {
        TODO("Not yet implemented")
    }

    fun removeUser(userApiModel: UserApiModel) {
        TODO("Not yet implemented")
    }

    @ExperimentalCoroutinesApi
    suspend fun getUsers(userIds: Array<out String>): Flow<List<UserApiModel>> = callbackFlow {
        val userQuery =
            firebaseFirestore.collection(USER_COLLECTION).whereIn("id", mutableListOf(userIds))

        val snapshotListener = userQuery.addSnapshotListener { value, error ->
            if (value != null) {
                val users: List<UserApiModel> = value.toObjects(UserApiModel::class.java)
                trySend(users)
            }
        }

        awaitClose {
            snapshotListener.remove()
        }
    }

    fun getUserIdFromEmail(email: String): String {
        TODO("Not yet implemented")
    }
}