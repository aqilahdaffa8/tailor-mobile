package com.tailor.mobile.data.repository

import android.content.Context
import com.google.gson.Gson
import com.tailor.mobile.data.source.local.TokenManager
import com.tailor.mobile.data.source.remote.ApiClient
import com.tailor.mobile.data.source.remote.ApiService
import com.tailor.mobile.data.source.remote.request.LoginRequest
import com.tailor.mobile.data.source.remote.response.ApiResponse
import com.tailor.mobile.data.source.remote.response.UserDto
import com.tailor.mobile.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class AuthRepository(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {
    companion object {
        fun create(context: Context): AuthRepository {
            val apiService = ApiClient.getApiService(context)
            val tokenManager = TokenManager(context.applicationContext)
            return AuthRepository(apiService, tokenManager)
        }
    }

    val isLoggedIn: Flow<Boolean> = tokenManager.tokenFlow.map { !it.isNullOrBlank() }

    fun login(email: String, password: String): Flow<Resource<UserDto>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiService.login(LoginRequest(email = email, password = password))
            if (response.isSuccessful) {
                val body = response.body()
                val token = body?.token
                val user = body?.user

                if (body?.success == true && token != null && user != null) {
                    tokenManager.saveAuth(
                        token = token,
                        userId = user.id,
                        userName = user.name,
                        userEmail = user.email
                    )
                    emit(Resource.Success(user))
                } else {
                    emit(Resource.Error(body?.message ?: "Login gagal. Silakan coba lagi."))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val parsedError = try {
                    Gson().fromJson(errorBody, ApiResponse::class.java)
                } catch (e: Exception) {
                    null
                }
                val errorMessage = parsedError?.message ?: "Terjadi kesalahan (Kode: ${response.code()})"
                emit(Resource.Error(errorMessage, parsedError?.errors))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Koneksi gagal: ${e.localizedMessage ?: "Tidak dapat terhubung ke server."}"))
        }
    }

    fun logout(): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            apiService.logout()
            tokenManager.clearAuth()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            // Even if network fails, clear local credentials
            tokenManager.clearAuth()
            emit(Resource.Success(Unit))
        }
    }

    fun getProfile(): Flow<Resource<UserDto>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiService.getProfile()
            if (response.isSuccessful && response.body()?.data != null) {
                emit(Resource.Success(response.body()!!.data!!))
            } else {
                emit(Resource.Error(response.body()?.message ?: "Gagal mengambil data profil."))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Koneksi gagal: ${e.localizedMessage}"))
        }
    }
}
