package com.tailor.mobile.data.source.remote

import com.tailor.mobile.data.source.remote.request.LoginRequest
import com.tailor.mobile.data.source.remote.response.ApiResponse
import com.tailor.mobile.data.source.remote.response.BahanMenipisDto
import com.tailor.mobile.data.source.remote.response.DashboardSummaryDto
import com.tailor.mobile.data.source.remote.response.PesananRingkasDto
import com.tailor.mobile.data.source.remote.response.ReminderDto
import com.tailor.mobile.data.source.remote.response.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<ApiResponse<Unit>>

    @POST("logout")
    suspend fun logout(): Response<ApiResponse<Unit>>

    @GET("me")
    suspend fun getProfile(): Response<ApiResponse<UserDto>>

    // Dashboard Endpoints
    @GET("dashboard/summary")
    suspend fun getDashboardSummary(): Response<ApiResponse<DashboardSummaryDto>>

    @GET("dashboard/pesanan-aktif")
    suspend fun getPesananAktif(
        @Query("limit") limit: Int = 10
    ): Response<ApiResponse<List<PesananRingkasDto>>>

    @GET("dashboard/pesanan-mendekati-deadline")
    suspend fun getPesananMendekatiDeadline(
        @Query("limit") limit: Int = 10
    ): Response<ApiResponse<List<PesananRingkasDto>>>

    @GET("dashboard/pesanan-terlambat")
    suspend fun getPesananTerlambat(
        @Query("limit") limit: Int = 10
    ): Response<ApiResponse<List<PesananRingkasDto>>>

    @GET("dashboard/stok-menipis")
    suspend fun getStokMenipis(
        @Query("limit") limit: Int = 10
    ): Response<ApiResponse<List<BahanMenipisDto>>>

    // Reminder Endpoints
    @GET("reminders")
    suspend fun getReminders(): Response<ApiResponse<List<ReminderDto>>>

    @PUT("reminders/{reminder}/dismiss")
    suspend fun dismissReminder(
        @Path("reminder") reminderId: Int
    ): Response<ApiResponse<ReminderDto>>
}
