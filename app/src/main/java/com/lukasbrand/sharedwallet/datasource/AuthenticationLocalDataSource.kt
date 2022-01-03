package com.lukasbrand.sharedwallet.datasource

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class AuthenticationLocalDataSource(
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher
) {

    companion object {
        val usernameKey = stringPreferencesKey("username")
        val passwordKey = stringPreferencesKey("password")
    }

    suspend fun getUserCredentials(): Pair<String, String>? = withContext(ioDispatcher) {
        context.dataStore.data.map { preferences ->
            val usernameValue: String? = preferences[usernameKey]
            val passwordValue: String? = preferences[passwordKey]

            if (usernameValue == null || passwordValue == null) {
                return@map null
            } else {
                Pair(usernameValue, passwordValue)
            }
        }.firstOrNull()
    }

    suspend fun setUserCredentials(email: String, password: String) = withContext(ioDispatcher) {
        context.dataStore.edit { preferences ->
            preferences[usernameKey] = email
            preferences[passwordKey] = password
        }
    }
}

