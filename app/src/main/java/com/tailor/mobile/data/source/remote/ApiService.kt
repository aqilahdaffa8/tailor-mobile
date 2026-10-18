package com.tailor.mobile.data.source.remote

import com.tailor.mobile.data.source.remote.request.JenisPakaianRequest
import com.tailor.mobile.data.source.remote.request.LoginRequest
import com.tailor.mobile.data.source.remote.request.PelangganRequest
import com.tailor.mobile.data.source.remote.request.PengukuranRequest
import com.tailor.mobile.data.source.remote.request.PolaDetailRequest
import com.tailor.mobile.data.source.remote.response.ApiResponse
import com.tailor.mobile.data.source.remote.response.BahanMenipisDto
import com.tailor.mobile.data.source.remote.response.DashboardSummaryDto
import com.tailor.mobile.data.source.remote.response.JenisPakaianDto
import com.tailor.mobile.data.source.remote.response.KategoriPolaDto
import com.tailor.mobile.data.source.remote.response.PelangganDto
import com.tailor.mobile.data.source.remote.response.PengukuranDto
import com.tailor.mobile.data.source.remote.response.PesananDto
import com.tailor.mobile.data.source.remote.response.PesananRingkasDto
import com.tailor.mobile.data.source.remote.response.PolaDetailDto
import com.tailor.mobile.data.source.remote.response.ReminderDto
import com.tailor.mobile.data.source.remote.response.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
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

    // Pelanggan Endpoints
    @GET("pelanggan")
    suspend fun getPelangganList(
        @Query("search") search: String? = null,
        @Query("per_page") perPage: Int = 30
    ): Response<ApiResponse<List<PelangganDto>>>

    @POST("pelanggan")
    suspend fun createPelanggan(
        @Body request: PelangganRequest
    ): Response<ApiResponse<PelangganDto>>

    @GET("pelanggan/{id}")
    suspend fun getPelangganDetail(
        @Path("id") id: Int
    ): Response<ApiResponse<PelangganDto>>

    @PUT("pelanggan/{id}")
    suspend fun updatePelanggan(
        @Path("id") id: Int,
        @Body request: PelangganRequest
    ): Response<ApiResponse<PelangganDto>>

    @DELETE("pelanggan/{id}")
    suspend fun deletePelanggan(
        @Path("id") id: Int
    ): Response<ApiResponse<Unit>>

    // Pengukuran Endpoints
    @GET("pelanggan/{id}/pengukuran")
    suspend fun getPengukuranByPelanggan(
        @Path("id") pelangganId: Int
    ): Response<ApiResponse<List<PengukuranDto>>>

    @GET("pelanggan/{id}/pengukuran/latest")
    suspend fun getLatestPengukuran(
        @Path("id") pelangganId: Int
    ): Response<ApiResponse<PengukuranDto>>

    @POST("pengukuran")
    suspend fun createPengukuran(
        @Body request: PengukuranRequest
    ): Response<ApiResponse<PengukuranDto>>

    @PUT("pengukuran/{id}")
    suspend fun updatePengukuran(
        @Path("id") id: Int,
        @Body request: PengukuranRequest
    ): Response<ApiResponse<PengukuranDto>>

    @DELETE("pengukuran/{id}")
    suspend fun deletePengukuran(
        @Path("id") id: Int
    ): Response<ApiResponse<Unit>>

    // Library Pola Endpoints
    @GET("pola/kategori")
    suspend fun getKategoriPolaList(
        @Query("search") search: String? = null
    ): Response<ApiResponse<List<KategoriPolaDto>>>

    @GET("pola/kategori/{id}/jenis-pakaian")
    suspend fun getJenisPakaianByKategori(
        @Path("id") kategoriId: Int
    ): Response<ApiResponse<List<JenisPakaianDto>>>

    @POST("pola/jenis-pakaian")
    suspend fun createJenisPakaian(
        @Body request: JenisPakaianRequest
    ): Response<ApiResponse<JenisPakaianDto>>

    @PUT("pola/jenis-pakaian/{id}")
    suspend fun updateJenisPakaian(
        @Path("id") id: Int,
        @Body request: JenisPakaianRequest
    ): Response<ApiResponse<JenisPakaianDto>>

    @DELETE("pola/jenis-pakaian/{id}")
    suspend fun deleteJenisPakaian(
        @Path("id") id: Int
    ): Response<ApiResponse<Unit>>

    @GET("pola/jenis-pakaian/{id}/detail")
    suspend fun getPolaDetailByJenis(
        @Path("id") jenisPakaianId: Int
    ): Response<ApiResponse<List<PolaDetailDto>>>

    @GET("pola/detail/{id}")
    suspend fun getPolaDetail(
        @Path("id") id: Int
    ): Response<ApiResponse<PolaDetailDto>>

    @POST("pola/detail")
    suspend fun createPolaDetail(
        @Body request: PolaDetailRequest
    ): Response<ApiResponse<PolaDetailDto>>

    @PUT("pola/detail/{id}")
    suspend fun updatePolaDetail(
        @Path("id") id: Int,
        @Body request: PolaDetailRequest
    ): Response<ApiResponse<PolaDetailDto>>

    @DELETE("pola/detail/{id}")
    suspend fun deletePolaDetail(
        @Path("id") id: Int
    ): Response<ApiResponse<Unit>>

    // Pesanan Endpoints
    @GET("pesanan")
    suspend fun getPesananList(
        @Query("search") search: String? = null,
        @Query("status") status: String? = null,
        @Query("pelanggan_id") pelangganId: Int? = null,
        @Query("deadline") deadline: String? = null,
        @Query("pembayaran") pembayaran: String? = null,
        @Query("per_page") perPage: Int = 30
    ): Response<ApiResponse<List<PesananDto>>>

    @GET("pesanan/{id}")
    suspend fun getPesananDetail(
        @Path("id") id: Int
    ): Response<ApiResponse<PesananDto>>

    @DELETE("pesanan/{id}")
    suspend fun batalkanPesanan(
        @Path("id") id: Int
    ): Response<ApiResponse<Unit>>
}
