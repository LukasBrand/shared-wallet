package com.lukasbrand.sharedwallet.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.lukasbrand.sharedwallet.data.model.ExpenseApiModel
import com.lukasbrand.sharedwallet.data.model.UserApiModel
import com.lukasbrand.sharedwallet.datasource.firestore.FirestoreApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UsersRemoteDataSource(
    private val firestoreApi: FirestoreApi,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun addUser(userApiModel: UserApiModel): Unit = withContext(ioDispatcher) {
        firestoreApi.addUser(userApiModel)
    }

    suspend fun modifyUser(userApiModel: UserApiModel): Unit = withContext(ioDispatcher) {
        firestoreApi.modifyUser(userApiModel)
    }

    suspend fun removeUser(userApiModel: UserApiModel): Unit = withContext(ioDispatcher) {
        firestoreApi.removeUser(userApiModel)
    }

    suspend fun getUserIdFromEmail(email: String): String = withContext(ioDispatcher) {
        firestoreApi.getUserIdFromEmail(email)
    }

    suspend fun getUsers(vararg userIds: String): Flow<List<UserApiModel>> =
        withContext(ioDispatcher) {
            firestoreApi.getUsers(userIds)
        }
}