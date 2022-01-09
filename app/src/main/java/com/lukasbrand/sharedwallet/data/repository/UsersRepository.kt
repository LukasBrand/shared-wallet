package com.lukasbrand.sharedwallet.data.repository

import com.lukasbrand.sharedwallet.data.User
import com.lukasbrand.sharedwallet.data.datasource.UsersRemoteDataSource
import com.lukasbrand.sharedwallet.data.model.UserApiModel
import com.lukasbrand.sharedwallet.types.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UsersRepository(private val usersRemoteDataSource: UsersRemoteDataSource) {

    suspend fun addUser(user: User) {
        val userApiModel: UserApiModel =
            user.run { UserApiModel(id, name, email, notificationToken, image) }
        usersRemoteDataSource.addUser(userApiModel)
    }

    suspend fun modifyUser(user: User) {
        val userApiModel: UserApiModel =
            user.run { UserApiModel(id, name, email, notificationToken, image) }
        usersRemoteDataSource.modifyUser(userApiModel)
    }

    suspend fun removeUser(userId: String) {
        usersRemoteDataSource.removeUser(userId)
    }

    suspend fun getUserFromEmail(email: String): Result<User> =
        usersRemoteDataSource.getUserIdFromEmail(email).let { userApiModel ->
            if (userApiModel == null) {
                Result.Error(NullPointerException())
            } else {
                val user =
                    User(
                        userApiModel.id,
                        userApiModel.name,
                        userApiModel.email,
                        userApiModel.notificationToken,
                        userApiModel.image
                    )
                Result.Success(user)
            }
        }


    suspend fun getUser(userId: String): User =
        usersRemoteDataSource.getUser(userId).run {
            User(id, name, email, notificationToken, image)
        }

    suspend fun getUserAsFlow(userId: String): Flow<User> =
        usersRemoteDataSource.getUserAsFlow(userId).map { userApiModel ->
            userApiModel.run {
                User(id, name, email, notificationToken, image)
            }
        }


    @ExperimentalCoroutinesApi
    suspend fun getUsers(vararg userIds: String): Flow<List<User>> =
        usersRemoteDataSource.getUsers(*userIds).map { list ->
            list.map { userApiModel ->
                userApiModel.run {
                    User(id, name, email, notificationToken, image)
                }
            }
        }
}