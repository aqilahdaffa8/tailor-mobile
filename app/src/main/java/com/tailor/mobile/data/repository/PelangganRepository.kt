package com.tailor.mobile.data.repository

import android.content.Context
import com.tailor.mobile.data.source.remote.ApiClient
import com.tailor.mobile.data.source.remote.ApiService
import com.tailor.mobile.data.source.remote.request.PelangganRequest
import com.tailor.mobile.data.source.remote.response.PelangganDto
import com.tailor.mobile.util.Resource
import java.io.IOException

class PelangganRepository(private val apiService: ApiService) {

    companion object {
        fun create(context: Context): PelangganRepository {
            return PelangganRepository(ApiClient.getApiService(context))
        }
    }

    suspend fun getPelangganList(search: String? = null, perPage: Int = 30): Resource<List<PelangganDto>> {
        return try {
            val response = apiService.getPelangganList(search = search?.ifBlank { null }, perPage = perPage)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.body()?.message ?: "Gagal memuat daftar pelanggan.")
            }
        } catch (e: IOException) {
            Resource.Error("Koneksi gagal: ${e.localizedMessage ?: "Tidak dapat terhubung ke server."}")
        } catch (e: Exception) {
            Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
        }
    }

    suspend fun getPelangganDetail(id: Int): Resource<PelangganDto> {
        return try {
            val response = apiService.getPelangganDetail(id)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.body()?.message ?: "Gagal memuat detail pelanggan.")
            }
        } catch (e: IOException) {
            Resource.Error("Koneksi gagal: ${e.localizedMessage ?: "Tidak dapat terhubung ke server."}")
        } catch (e: Exception) {
            Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
        }
    }

    suspend fun createPelanggan(request: PelangganRequest): Resource<PelangganDto> {
        return try {
            val response = apiService.createPelanggan(request)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                val errorMsg = response.body()?.message
                    ?: "Gagal menambahkan pelanggan baru."
                Resource.Error(errorMsg)
            }
        } catch (e: IOException) {
            Resource.Error("Koneksi gagal: ${e.localizedMessage ?: "Tidak dapat terhubung ke server."}")
        } catch (e: Exception) {
            Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
        }
    }

    suspend fun updatePelanggan(id: Int, request: PelangganRequest): Resource<PelangganDto> {
        return try {
            val response = apiService.updatePelanggan(id, request)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                val errorMsg = response.body()?.message
                    ?: "Gagal memperbarui data pelanggan."
                Resource.Error(errorMsg)
            }
        } catch (e: IOException) {
            Resource.Error("Koneksi gagal: ${e.localizedMessage ?: "Tidak dapat terhubung ke server."}")
        } catch (e: Exception) {
            Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
        }
    }

    suspend fun deletePelanggan(id: Int): Resource<Unit> {
        return try {
            val response = apiService.deletePelanggan(id)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error(response.body()?.message ?: "Gagal menghapus pelanggan. Pastikan tidak ada pesanan aktif terkait.")
            }
        } catch (e: IOException) {
            Resource.Error("Koneksi gagal: ${e.localizedMessage ?: "Tidak dapat terhubung ke server."}")
        } catch (e: Exception) {
            Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
        }
    }
}
