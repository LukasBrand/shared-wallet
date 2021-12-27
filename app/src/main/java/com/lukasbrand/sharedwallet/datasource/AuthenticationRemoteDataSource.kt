package com.lukasbrand.sharedwallet.datasource

import com.lukasbrand.sharedwallet.data.User

class AuthenticationRemoteDataSource {



    fun login(username: String, password: String): Result<User> {
        TODO()
    }

    fun logout() {
        // TODO: revoke authentication
    }
}