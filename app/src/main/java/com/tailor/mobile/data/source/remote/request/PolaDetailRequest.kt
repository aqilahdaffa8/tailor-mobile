package com.tailor.mobile.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class PolaDetailRequest(
    @SerializedName("jenis_pakaian_id")
    val jenisPakaianId: Int,
    @SerializedName("size")
    val size: String,
    @SerializedName("data_ukuran")
    val dataUkuran: Map<String, String>? = null,
    @SerializedName("catatan")
    val catatan: String? = null
)
