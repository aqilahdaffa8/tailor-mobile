package com.tailor.mobile.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class PengukuranDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("pelanggan_id")
    val pelangganId: Int,

    @SerializedName("tanggal_ukur")
    val tanggalUkur: String,

    @SerializedName("data_ukuran")
    val dataUkuran: Map<String, String>? = emptyMap(),

    @SerializedName("catatan")
    val catatan: String? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null
)
