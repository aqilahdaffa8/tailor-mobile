package com.tailor.mobile.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class JenisPakaianDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("kategori_pola_id")
    val kategoriPolaId: Int,
    @SerializedName("nama_jenis")
    val namaJenis: String,
    @SerializedName("deskripsi")
    val deskripsi: String? = null,
    @SerializedName("pola_detail_count")
    val polaDetailCount: Int? = null,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("updated_at")
    val updatedAt: String? = null,
    @SerializedName("kategori_pola")
    val kategoriPola: KategoriPolaDto? = null,
    @SerializedName("pola_detail")
    val polaDetail: List<PolaDetailDto>? = null
)
