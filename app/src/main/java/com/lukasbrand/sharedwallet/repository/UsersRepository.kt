package com.lukasbrand.sharedwallet.repository

import com.lukasbrand.sharedwallet.data.Result
import com.lukasbrand.sharedwallet.data.User
import com.lukasbrand.sharedwallet.data.model.UserApiModel
import com.lukasbrand.sharedwallet.datasource.UsersRemoteDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UsersRepository(private val usersRemoteDataSource: UsersRemoteDataSource) {

    suspend fun addUser(user: User) {
        val userApiModel: UserApiModel = user.run { UserApiModel(id, name, email, image) }
        usersRemoteDataSource.addUser(userApiModel)
    }

    suspend fun modifyUser(user: User) {
        val userApiModel: UserApiModel = user.run { UserApiModel(id, name, email, image) }
        usersRemoteDataSource.modifyUser(userApiModel)
    }

    suspend fun removeUser(user: User) {
        val userApiModel: UserApiModel = user.run { UserApiModel(id, name, email, image) }
        usersRemoteDataSource.removeUser(userApiModel)
    }

    suspend fun getUserFromEmail(email: String): Result<User> =
        usersRemoteDataSource.getUserIdFromEmail(email).let { userApiModel ->
            if (userApiModel == null) {
                Result.Error(NullPointerException())
            } else {
                val user =
                    User(userApiModel.id, userApiModel.name, userApiModel.email, userApiModel.image)
                Result.Success(user)
            }
        }


    suspend fun getUser(userId: String): User =
        usersRemoteDataSource.getUser(userId).run {
            User(id, name, email, image)
        }

    @ExperimentalCoroutinesApi
    suspend fun getUsers(vararg userIds: String): Flow<List<User>> =
        usersRemoteDataSource.getUsers(*userIds).map { list ->
            list.map { userApiModel ->
                userApiModel.run {
                    User(id, name, email, image)
                }
            }
        }
}