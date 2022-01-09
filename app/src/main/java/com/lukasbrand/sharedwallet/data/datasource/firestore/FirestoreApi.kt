package com.lukasbrand.sharedwallet.data.datasource.firestore

import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
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
            val document = firebaseFirestore.collection(EXPENSE_COLLECTION).document()
            document.set(expenseApiModel.copy(id = document.id))
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }.addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

    suspend fun modifyExpense(expenseApiModel: ExpenseApiModel): Unit =
        suspendCancellableCoroutine { continuation ->
            val modifyExpenseDocRef =
                firebaseFirestore.collection(EXPENSE_COLLECTION).document(expenseApiModel.id!!)
            modifyExpenseDocRef.set(expenseApiModel)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        continuation.resume(Unit)
                    }
                }
                /*.addOnSuccessListener {
                    continuation.resume(Unit)
                }*/.addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

    suspend fun removeExpense(expenseApiModel: ExpenseApiModel): Unit =
        suspendCancellableCoroutine { continuation ->
            val removeExpenseDocRef =
                firebaseFirestore.collection(EXPENSE_COLLECTION).document(expenseApiModel.id!!)
            removeExpenseDocRef.delete()
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }.addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

    @ExperimentalCoroutinesApi
    suspend fun getExpenses(authId: String): Flow<List<ExpenseApiModel>> = callbackFlow {
        val expensesQuery =
            firebaseFirestore.collection(EXPENSE_COLLECTION)
                .whereArrayContains("participants", authId)

        val snapshotListener =
            expensesQuery.addSnapshotListener { querySnapshot: QuerySnapshot?, firebaseException: FirebaseFirestoreException? ->
                if (querySnapshot != null) {
                    val expenses: List<ExpenseApiModel> = querySnapshot.documents
                        .mapNotNull { documentSnapshot ->
                            documentSnapshot.toObject(ExpenseApiModel::class.java)
                                ?.copy(id = documentSnapshot.id)
                        }
                    trySend(expenses)
                } else if (firebaseException != null) {
                    close(firebaseException)
                }
            }

        awaitClose {
            snapshotListener.remove()
        }
    }

    @ExperimentalCoroutinesApi
    suspend fun getExpense(expenseId: String): Flow<ExpenseApiModel> = callbackFlow {
        val expensesQuery = firebaseFirestore.collection(EXPENSE_COLLECTION)
            .whereEqualTo(FieldPath.documentId(), expenseId)

        val snapshotListener =
            expensesQuery.addSnapshotListener { querySnapshot: QuerySnapshot?, firebaseException: FirebaseFirestoreException? ->
                if (querySnapshot != null) {
                    val expense: ExpenseApiModel = querySnapshot.documents
                        .mapNotNull { documentSnapshot ->
                            documentSnapshot.toObject(ExpenseApiModel::class.java)
                                ?.copy(id = documentSnapshot.id)
                        }.first()
                    trySend(expense)
                } else if (firebaseException != null) {
                    close(firebaseException)
                }
            }

        awaitClose {
            snapshotListener.remove()
        }
    }


    suspend fun addUser(userApiModel: UserApiModel): Unit =
        suspendCancellableCoroutine { continuation ->
            firebaseFirestore.collection(USER_COLLECTION).document(userApiModel.id)
                .set(userApiModel)
                /*.addOnSuccessListener {
                    continuation.resume(Unit)
                }*/
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        continuation.resume(Unit)
                    }
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

    suspend fun modifyUser(userApiModel: UserApiModel): Unit =
        suspendCancellableCoroutine { continuation ->
            val modifyUserDocRef =
                firebaseFirestore.collection(USER_COLLECTION).document(userApiModel.id)
            modifyUserDocRef.set(userApiModel)
                /*.addOnSuccessListener {
                    continuation.resume(Unit)
                }*/
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        continuation.resume(Unit)
                    }
                }.addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

    suspend fun removeUser(userApiModel: UserApiModel): Unit =
        suspendCancellableCoroutine { continuation ->
            val removeUserDocRef =
                firebaseFirestore.collection(USER_COLLECTION).document(userApiModel.id)
            removeUserDocRef.delete()
                /*.addOnSuccessListener {
                    continuation.resume(Unit)
                }*/
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        continuation.resume(Unit)
                    }
                }.addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

    @ExperimentalCoroutinesApi
    suspend fun getUsers(userIds: Array<out String>): Flow<List<UserApiModel>> = callbackFlow {
        val userQuery =
            firebaseFirestore.collection(USER_COLLECTION)
                .whereIn(FieldPath.documentId(), mutableListOf(userIds))

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


    suspend fun getUserIdFromEmail(email: String): UserApiModel? =
        suspendCancellableCoroutine { continuation ->
            val getUserRef =
                firebaseFirestore.collection(USER_COLLECTION).whereEqualTo("email", email)
            getUserRef.get()
                .addOnSuccessListener { value ->
                    val user = value.toObjects(UserApiModel::class.java).firstOrNull()
                    continuation.resume(user)
                }.addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

    suspend fun getUser(userId: String): UserApiModel =
        suspendCancellableCoroutine { continuation ->
            val getUserRef =
                firebaseFirestore.collection(USER_COLLECTION)
                    .whereEqualTo(FieldPath.documentId(), userId)
            getUserRef.get()
                .addOnSuccessListener { value ->
                    val user = value.toObjects(UserApiModel::class.java).first()
                    continuation.resume(user)
                }.addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
            FieldPath.documentId()
        }

    @ExperimentalCoroutinesApi
    suspend fun getUserAsFlow(userId: String): Flow<UserApiModel> = callbackFlow {
        val userQuery =
            firebaseFirestore.collection(USER_COLLECTION)
                .whereEqualTo(FieldPath.documentId(), userId)

        val snapshotListener = userQuery.addSnapshotListener { value, _ ->
            if (value != null) {
                val user: UserApiModel = value.toObjects(UserApiModel::class.java).first()
                trySend(user)
            }
        }

        awaitClose {
            snapshotListener.remove()
        }
    }
}