package com.tailor.mobile.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class PengukuranRequest(
    @SerializedName("pelanggan_id")
    val pelangganId: Int,

    @SerializedName("tanggal_ukur")
    val tanggalUkur: String,

    @SerializedName("data_ukuran")
    val dataUkuran: Map<String, String>,

    @SerializedName("catatan")
    val catatan: String? = null
)
