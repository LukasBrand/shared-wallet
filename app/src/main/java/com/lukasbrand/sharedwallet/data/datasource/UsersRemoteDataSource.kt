package com.lukasbrand.sharedwallet.data.datasource

import com.lukasbrand.sharedwallet.data.model.UserApiModel
import com.lukasbrand.sharedwallet.data.datasource.firestore.FirestoreApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    suspend fun removeUser(userId: String): Unit = withContext(ioDispatcher) {
        firestoreApi.removeUser(userId)
    }

    suspend fun getUserIdFromEmail(email: String): UserApiModel? = withContext(ioDispatcher) {
        firestoreApi.getUserIdFromEmail(email)
    }

    @ExperimentalCoroutinesApi
    suspend fun getUsers(vararg userIds: String): Flow<List<UserApiModel>> =
        withContext(ioDispatcher) {
            firestoreApi.getUsers(userIds)
        }

    suspend fun getUser(userId: String): UserApiModel = withContext(ioDispatcher) {
        firestoreApi.getUser(userId)
    }

    @ExperimentalCoroutinesApi
    suspend fun getUserAsFlow(userId: String): Flow<UserApiModel> = withContext(ioDispatcher) {
        firestoreApi.getUserAsFlow(userId)
    }
}