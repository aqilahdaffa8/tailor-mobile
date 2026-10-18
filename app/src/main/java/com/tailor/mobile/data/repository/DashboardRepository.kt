package com.tailor.mobile.data.repository

import android.content.Context
import com.tailor.mobile.data.source.remote.ApiClient
import com.tailor.mobile.data.source.remote.ApiService
import com.tailor.mobile.data.source.remote.response.BahanMenipisDto
import com.tailor.mobile.data.source.remote.response.DashboardSummaryDto
import com.tailor.mobile.data.source.remote.response.PesananRingkasDto
import com.tailor.mobile.data.source.remote.response.ReminderDto
import com.tailor.mobile.util.Resource
import java.io.IOException

class DashboardRepository(private val apiService: ApiService) {

    companion object {
        fun create(context: Context): DashboardRepository {
            return DashboardRepository(ApiClient.getApiService(context))
        }
    }

    suspend fun getSummary(): Resource<DashboardSummaryDto> {
        return try {
            val response = apiService.getDashboardSummary()
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.body()?.message ?: "Gagal memuat ringkasan dashboard.")
            }
        } catch (e: IOException) {
            Resource.Error("Koneksi gagal: ${e.localizedMessage ?: "Tidak dapat terhubung ke server."}")
        } catch (e: Exception) {
            Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
        }
    }

    suspend fun getPesananMendekati(limit: Int = 10): Resource<List<PesananRingkasDto>> {
        return try {
            val response = apiService.getPesananMendekatiDeadline(limit)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.body()?.message ?: "Gagal memuat pesanan mendekati deadline.")
            }
        } catch (e: IOException) {
            Resource.Error("Koneksi gagal: ${e.localizedMessage ?: "Tidak dapat terhubung ke server."}")
        } catch (e: Exception) {
            Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
        }
    }

    suspend fun getPesananTerlambat(limit: Int = 10): Resource<List<PesananRingkasDto>> {
        return try {
            val response = apiService.getPesananTerlambat(limit)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.body()?.message ?: "Gagal memuat pesanan terlambat.")
            }
        } catch (e: IOException) {
            Resource.Error("Koneksi gagal: ${e.localizedMessage ?: "Tidak dapat terhubung ke server."}")
        } catch (e: Exception) {
            Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
        }
    }

    suspend fun getStokMenipis(limit: Int = 10): Resource<List<BahanMenipisDto>> {
        return try {
            val response = apiService.getStokMenipis(limit)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.body()?.message ?: "Gagal memuat data stok menipis.")
            }
        } catch (e: IOException) {
            Resource.Error("Koneksi gagal: ${e.localizedMessage ?: "Tidak dapat terhubung ke server."}")
        } catch (e: Exception) {
            Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
        }
    }

    suspend fun getReminders(): Resource<List<ReminderDto>> {
        return try {
            val response = apiService.getReminders()
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.body()?.message ?: "Gagal memuat daftar reminder.")
            }
        } catch (e: IOException) {
            Resource.Error("Koneksi gagal: ${e.localizedMessage ?: "Tidak dapat terhubung ke server."}")
        } catch (e: Exception) {
            Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
        }
    }

    suspend fun dismissReminder(id: Int): Resource<ReminderDto> {
        return try {
            val response = apiService.dismissReminder(id)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.body()?.message ?: "Gagal menandai reminder.")
            }
        } catch (e: IOException) {
            Resource.Error("Koneksi gagal: ${e.localizedMessage ?: "Tidak dapat terhubung ke server."}")
        } catch (e: Exception) {
            Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
        }
    }
}
