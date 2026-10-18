package com.tailor.mobile.data.source.remote

import android.content.Context
import com.tailor.mobile.BuildConfig
import com.tailor.mobile.data.source.local.TokenManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    @Volatile
    private var apiService: ApiService? = null

    fun getApiService(context: Context): ApiService {
        return apiService ?: synchronized(this) {
            apiService ?: buildRetrofit(context).create(ApiService::class.java).also {
                apiService = it
            }
        }
    }

    private fun buildRetrofit(context: Context): Retrofit {
        val tokenManager = TokenManager(context.applicationContext)

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenManager))
            .addInterceptor(loggingInterceptor)
            .retryOnConnectionFailure(true)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
