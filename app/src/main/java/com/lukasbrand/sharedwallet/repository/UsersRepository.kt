package com.lukasbrand.sharedwallet.repository

import com.lukasbrand.sharedwallet.data.model.UserApiModel
import com.lukasbrand.sharedwallet.datasource.UsersRemoteDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UsersRepository(private val usersRemoteDataSource: UsersRemoteDataSource) {

    suspend fun addUser(userApiModel: UserApiModel): Unit =
        usersRemoteDataSource.addUser(userApiModel)

    suspend fun modifyUser(userApiModel: UserApiModel): Unit =
        usersRemoteDataSource.modifyUser(userApiModel)

    suspend fun removeUser(userApiModel: UserApiModel): Unit =
        usersRemoteDataSource.removeUser(userApiModel)

    suspend fun getUserIdFromEmail(email: String): String =
        usersRemoteDataSource.getUserIdFromEmail(email)

    suspend fun getUser(userId: String): UserApiModel =
        usersRemoteDataSource.getUser(userId)

    @ExperimentalCoroutinesApi
    suspend fun getUsers(vararg userIds: String): Flow<List<UserApiModel>> =
        usersRemoteDataSource.getUsers(*userIds)
}