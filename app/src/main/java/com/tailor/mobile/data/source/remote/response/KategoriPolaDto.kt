package com.tailor.mobile.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class KategoriPolaDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("nama_kategori")
    val namaKategori: String,
    @SerializedName("deskripsi")
    val deskripsi: String? = null,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("updated_at")
    val updatedAt: String? = null,
    @SerializedName("jenis_pakaian")
    val jenisPakaian: List<JenisPakaianDto>? = null
)
