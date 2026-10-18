package com.tailor.mobile.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class PolaDetailDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("jenis_pakaian_id")
    val jenisPakaianId: Int,
    @SerializedName("size")
    val size: String,
    @SerializedName("data_ukuran")
    val dataUkuran: Map<String, Any?>? = null,
    @SerializedName("foto_pola")
    val fotoPola: String? = null,
    @SerializedName("file_pola")
    val filePola: String? = null,
    @SerializedName("catatan")
    val catatan: String? = null,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("updated_at")
    val updatedAt: String? = null,
    @SerializedName("jenis_pakaian")
    val jenisPakaian: JenisPakaianDto? = null
)
