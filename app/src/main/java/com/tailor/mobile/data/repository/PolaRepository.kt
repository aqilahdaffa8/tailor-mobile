package com.tailor.mobile.data.repository

import android.content.Context
import com.tailor.mobile.data.source.remote.ApiClient
import com.tailor.mobile.data.source.remote.ApiService
import com.tailor.mobile.data.source.remote.request.JenisPakaianRequest
import com.tailor.mobile.data.source.remote.request.PolaDetailRequest
import com.tailor.mobile.data.source.remote.response.JenisPakaianDto
import com.tailor.mobile.data.source.remote.response.KategoriPolaDto
import com.tailor.mobile.data.source.remote.response.PolaDetailDto
import com.tailor.mobile.util.Resource
import java.io.IOException

class PolaRepository(private val apiService: ApiService) {

    companion object {
        fun create(context: Context): PolaRepository {
            return PolaRepository(ApiClient.getApiService(context))
        }
    }

    suspend fun getKategoriList(search: String? = null): Resource<List<KategoriPolaDto>> {
        return try {
            val response = apiService.getKategoriPolaList(search = search?.ifBlank { null })
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.body()?.message ?: "Gagal memuat kategori pola.")
            }
        } catch (e: IOException) {
            Resource.Error("Koneksi gagal: ${e.localizedMessage ?: "Tidak dapat terhubung ke server."}")
        } catch (e: Exception) {
            Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
        }
    }

    suspend fun getJenisPakaianByKategori(kategoriId: Int): Resource<List<JenisPakaianDto>> {
        return try {
            val response = apiService.getJenisPakaianByKategori(kategoriId)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.body()?.message ?: "Gagal memuat jenis pakaian.")
            }
        } catch (e: IOException) {
            Resource.Error("Koneksi gagal: ${e.localizedMessage ?: "Tidak dapat terhubung ke server."}")
        } catch (e: Exception) {
            Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
        }
    }

    suspend fun createJenisPakaian(request: JenisPakaianRequest): Resource<JenisPakaianDto> {
        return try {
            val response = apiService.createJenisPakaian(request)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.body()?.message ?: "Gagal membuat jenis pakaian.")
            }
        } catch (e: IOException) {
            Resource.Error("Koneksi gagal: ${e.localizedMessage ?: "Tidak dapat terhubung ke server."}")
        } catch (e: Exception) {
            Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
        }
    }

    suspend fun updateJenisPakaian(id: Int, request: JenisPakaianRequest): Resource<JenisPakaianDto> {
        return try {
            val response = apiService.updateJenisPakaian(id, request)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.body()?.message ?: "Gagal memperbarui jenis pakaian.")
            }
        } catch (e: IOException) {
            Resource.Error("Koneksi gagal: ${e.localizedMessage ?: "Tidak dapat terhubung ke server."}")
        } catch (e: Exception) {
            Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
        }
    }

    suspend fun deleteJenisPakaian(id: Int): Resource<Unit> {
        return try {
            val response = apiService.deleteJenisPakaian(id)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error(response.body()?.message ?: "Gagal menghapus jenis pakaian.")
            }
        } catch (e: IOException) {
            Resource.Error("Koneksi gagal: ${e.localizedMessage ?: "Tidak dapat terhubung ke server."}")
        } catch (e: Exception) {
            Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
        }
    }

    suspend fun getPolaDetailByJenis(jenisPakaianId: Int): Resource<List<PolaDetailDto>> {
        return try {
            val response = apiService.getPolaDetailByJenis(jenisPakaianId)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.body()?.message ?: "Gagal memuat standar ukuran pola.")
            }
        } catch (e: IOException) {
            Resource.Error("Koneksi gagal: ${e.localizedMessage ?: "Tidak dapat terhubung ke server."}")
        } catch (e: Exception) {
            Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
        }
    }

    suspend fun getPolaDetail(id: Int): Resource<PolaDetailDto> {
        return try {
            val response = apiService.getPolaDetail(id)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.body()?.message ?: "Gagal memuat detail pola.")
            }
        } catch (e: IOException) {
            Resource.Error("Koneksi gagal: ${e.localizedMessage ?: "Tidak dapat terhubung ke server."}")
        } catch (e: Exception) {
            Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
        }
    }

    suspend fun createPolaDetail(request: PolaDetailRequest): Resource<PolaDetailDto> {
        return try {
            val response = apiService.createPolaDetail(request)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.body()?.message ?: "Gagal menambahkan detail pola ukuran.")
            }
        } catch (e: IOException) {
            Resource.Error("Koneksi gagal: ${e.localizedMessage ?: "Tidak dapat terhubung ke server."}")
        } catch (e: Exception) {
            Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
        }
    }

    suspend fun updatePolaDetail(id: Int, request: PolaDetailRequest): Resource<PolaDetailDto> {
        return try {
            val response = apiService.updatePolaDetail(id, request)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.body()?.message ?: "Gagal memperbarui detail pola ukuran.")
            }
        } catch (e: IOException) {
            Resource.Error("Koneksi gagal: ${e.localizedMessage ?: "Tidak dapat terhubung ke server."}")
        } catch (e: Exception) {
            Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
        }
    }

    suspend fun deletePolaDetail(id: Int): Resource<Unit> {
        return try {
            val response = apiService.deletePolaDetail(id)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error(response.body()?.message ?: "Gagal menghapus detail pola ukuran.")
            }
        } catch (e: IOException) {
            Resource.Error("Koneksi gagal: ${e.localizedMessage ?: "Tidak dapat terhubung ke server."}")
        } catch (e: Exception) {
            Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
        }
    }
}
