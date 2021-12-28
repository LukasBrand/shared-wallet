package com.lukasbrand.sharedwallet.datasource.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.lukasbrand.sharedwallet.data.model.ExpenseApiModel
import com.lukasbrand.sharedwallet.data.model.UserApiModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirestoreApi(private val firebaseFirestore: FirebaseFirestore) {

    companion object {
        private var USER_COLLECTION = "users"
        private var EXPENSE_COLLECTION = "expenses"

        @Volatile
        private var INSTANCE: FirestoreApi? = null

        fun getInstance(): FirestoreApi {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = FirestoreApi(FirebaseFirestore.getInstance())
                }

                return instance
            }
        }
    }

    suspend fun addExpense(expenseApiModel: ExpenseApiModel): Unit =
        suspendCancellableCoroutine { continuation ->
            firebaseFirestore.collection(EXPENSE_COLLECTION).add(expenseApiModel)
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }.addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

    suspend fun modifyExpense(expenseApiModel: ExpenseApiModel): Unit =
        suspendCancellableCoroutine { continuation ->
            val modifyExpenseDocRef =
                firebaseFirestore.collection(EXPENSE_COLLECTION).document(expenseApiModel.id)
            modifyExpenseDocRef.set(expenseApiModel)
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }.addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

    suspend fun removeExpense(expenseApiModel: ExpenseApiModel): Unit =
        suspendCancellableCoroutine { continuation ->
            val removeExpenseDocRef =
                firebaseFirestore.collection(EXPENSE_COLLECTION).document(expenseApiModel.id)
            removeExpenseDocRef.delete()
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }.addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

    //maybe needs to be suspended
    @ExperimentalCoroutinesApi
    fun getExpenses(authId: String): Flow<List<ExpenseApiModel>> = callbackFlow {
        val expensesQuery =
            firebaseFirestore.collection(EXPENSE_COLLECTION)
                .whereArrayContains("participants", authId)

        val snapshotListener = expensesQuery.addSnapshotListener { value, _ ->
            if (value != null) {
                val expenses: List<ExpenseApiModel> = value.toObjects(ExpenseApiModel::class.java)
                trySend(expenses)
            }
        }

        awaitClose {
            snapshotListener.remove()
        }
    }


    suspend fun addUser(userApiModel: UserApiModel): Unit =
        suspendCancellableCoroutine { continuation ->
            firebaseFirestore.collection(USER_COLLECTION).add(userApiModel)
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }.addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

    suspend fun modifyUser(userApiModel: UserApiModel): Unit =
        suspendCancellableCoroutine { continuation ->
            val modifyUserDocRef =
                firebaseFirestore.collection(USER_COLLECTION).document(userApiModel.id)
            modifyUserDocRef.set(userApiModel)
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }.addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

    suspend fun removeUser(userApiModel: UserApiModel): Unit =
        suspendCancellableCoroutine { continuation ->
            val removeUserDocRef =
                firebaseFirestore.collection(USER_COLLECTION).document(userApiModel.id)
            removeUserDocRef.delete()
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }.addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

    //maybe needs to be suspended
    @ExperimentalCoroutinesApi
    fun getUsers(userIds: Array<out String>): Flow<List<UserApiModel>> = callbackFlow {
        val userQuery =
            firebaseFirestore.collection(USER_COLLECTION).whereIn("id", mutableListOf(userIds))

        val snapshotListener = userQuery.addSnapshotListener { value, _ ->
            if (value != null) {
                val users: List<UserApiModel> = value.toObjects(UserApiModel::class.java)
                trySend(users)
            }
        }

        awaitClose {
            snapshotListener.remove()
        }
    }


    suspend fun getUserIdFromEmail(email: String): String =
        suspendCancellableCoroutine { continuation ->
            val getUserRef =
                firebaseFirestore.collection(USER_COLLECTION).whereEqualTo("email", email)
            getUserRef.get()
                .addOnSuccessListener { value ->
                    val userId = value?.toObjects(String::class.java)?.first()
                    if (userId != null) {
                        continuation.resume(userId)
                    }
                }.addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
}