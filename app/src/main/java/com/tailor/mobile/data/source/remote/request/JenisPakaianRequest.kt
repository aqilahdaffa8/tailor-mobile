package com.tailor.mobile.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class JenisPakaianRequest(
    @SerializedName("kategori_pola_id")
    val kategoriPolaId: Int,
    @SerializedName("nama_jenis")
    val namaJenis: String,
    @SerializedName("deskripsi")
    val deskripsi: String? = null
)
