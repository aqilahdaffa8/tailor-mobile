package com.tailor.mobile.data.repository

import android.content.Context
import com.tailor.mobile.data.source.remote.ApiClient
import com.tailor.mobile.data.source.remote.ApiService
import com.tailor.mobile.data.source.remote.request.PengukuranRequest
import com.tailor.mobile.data.source.remote.response.PengukuranDto
import com.tailor.mobile.util.Resource
import java.io.IOException

class PengukuranRepository(private val apiService: ApiService) {

    companion object {
        fun create(context: Context): PengukuranRepository {
            return PengukuranRepository(ApiClient.getApiService(context))
        }
    }

    suspend fun getPengukuranByPelanggan(pelangganId: Int): Resource<List<PengukuranDto>> {
        return try {
            val response = apiService.getPengukuranByPelanggan(pelangganId)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.body()?.message ?: "Gagal memuat riwayat pengukuran.")
            }
        } catch (e: IOException) {
            Resource.Error("Koneksi gagal: ${e.localizedMessage ?: "Tidak dapat terhubung ke server."}")
        } catch (e: Exception) {
            Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
        }
    }

    suspend fun getLatestPengukuran(pelangganId: Int): Resource<PengukuranDto> {
        return try {
            val response = apiService.getLatestPengukuran(pelangganId)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.body()?.message ?: "Belum ada data pengukuran terakhir.")
            }
        } catch (e: IOException) {
            Resource.Error("Koneksi gagal: ${e.localizedMessage ?: "Tidak dapat terhubung ke server."}")
        } catch (e: Exception) {
            Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
        }
    }

    suspend fun createPengukuran(request: PengukuranRequest): Resource<PengukuranDto> {
        return try {
            val response = apiService.createPengukuran(request)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.body()?.message ?: "Gagal menyimpan data pengukuran.")
            }
        } catch (e: IOException) {
            Resource.Error("Koneksi gagal: ${e.localizedMessage ?: "Tidak dapat terhubung ke server."}")
        } catch (e: Exception) {
            Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
        }
    }

    suspend fun updatePengukuran(id: Int, request: PengukuranRequest): Resource<PengukuranDto> {
        return try {
            val response = apiService.updatePengukuran(id, request)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.body()?.message ?: "Gagal memperbarui data pengukuran.")
            }
        } catch (e: IOException) {
            Resource.Error("Koneksi gagal: ${e.localizedMessage ?: "Tidak dapat terhubung ke server."}")
        } catch (e: Exception) {
            Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
        }
    }

    suspend fun deletePengukuran(id: Int): Resource<Unit> {
        return try {
            val response = apiService.deletePengukuran(id)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error(response.body()?.message ?: "Gagal menghapus pengukuran.")
            }
        } catch (e: IOException) {
            Resource.Error("Koneksi gagal: ${e.localizedMessage ?: "Tidak dapat terhubung ke server."}")
        } catch (e: Exception) {
            Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
        }
    }
}
