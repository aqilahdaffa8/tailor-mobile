package com.tailor.mobile.data.source.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "tailor_auth_prefs")

class TokenManager(private val context: Context) {

    companion object {
        private val KEY_TOKEN = stringPreferencesKey("sanctum_token")
        private val KEY_USER_ID = intPreferencesKey("user_id")
        private val KEY_USER_NAME = stringPreferencesKey("user_name")
        private val KEY_USER_EMAIL = stringPreferencesKey("user_email")
    }

    val tokenFlow: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[KEY_TOKEN]
    }

    suspend fun getToken(): String? {
        return tokenFlow.firstOrNull()
    }

    suspend fun saveAuth(token: String, userId: Int, userName: String, userEmail: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_TOKEN] = token
            preferences[KEY_USER_ID] = userId
            preferences[KEY_USER_NAME] = userName
            preferences[KEY_USER_EMAIL] = userEmail
        }
    }

    suspend fun clearAuth() {
        context.dataStore.edit { preferences ->
            preferences.remove(KEY_TOKEN)
            preferences.remove(KEY_USER_ID)
            preferences.remove(KEY_USER_NAME)
            preferences.remove(KEY_USER_EMAIL)
        }
    }

    val userNameFlow: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[KEY_USER_NAME]
    }

    val userEmailFlow: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[KEY_USER_EMAIL]
    }
}
