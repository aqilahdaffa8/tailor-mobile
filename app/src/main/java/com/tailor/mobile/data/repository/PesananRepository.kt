package com.tailor.mobile.data.repository

import android.content.Context
import com.tailor.mobile.data.source.remote.ApiClient
import com.tailor.mobile.data.source.remote.ApiService
import com.tailor.mobile.data.source.remote.response.PesananDto
import com.tailor.mobile.util.Resource
import java.io.IOException

class PesananRepository(private val apiService: ApiService) {

    companion object {
        fun create(context: Context): PesananRepository {
            return PesananRepository(ApiClient.getApiService(context))
        }
    }

    suspend fun getPesananList(
        search: String? = null,
        status: String? = null,
        pelangganId: Int? = null,
        deadline: String? = null,
        pembayaran: String? = null,
        perPage: Int = 30
    ): Resource<List<PesananDto>> {
        return try {
            val response = apiService.getPesananList(
                search = search?.ifBlank { null },
                status = status?.ifBlank { null },
                pelangganId = pelangganId,
                deadline = deadline?.ifBlank { null },
                pembayaran = pembayaran?.ifBlank { null },
                perPage = perPage
            )
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.body()?.message ?: "Gagal memuat daftar pesanan.")
            }
        } catch (e: IOException) {
            Resource.Error("Koneksi gagal: ${e.localizedMessage ?: "Tidak dapat terhubung ke server."}")
        } catch (e: Exception) {
            Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
        }
    }

    suspend fun getPesananDetail(id: Int): Resource<PesananDto> {
        return try {
            val response = apiService.getPesananDetail(id)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.body()?.message ?: "Gagal memuat detail pesanan.")
            }
        } catch (e: IOException) {
            Resource.Error("Koneksi gagal: ${e.localizedMessage ?: "Tidak dapat terhubung ke server."}")
        } catch (e: Exception) {
            Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
        }
    }

    suspend fun batalkanPesanan(id: Int): Resource<Unit> {
        return try {
            val response = apiService.batalkanPesanan(id)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error(response.body()?.message ?: "Gagal membatalkan pesanan.")
            }
        } catch (e: IOException) {
            Resource.Error("Koneksi gagal: ${e.localizedMessage ?: "Tidak dapat terhubung ke server."}")
        } catch (e: Exception) {
            Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
        }
    }
}
