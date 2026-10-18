package com.tailor.mobile.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class PelangganRequest(
    @SerializedName("nama")
    val nama: String,

    @SerializedName("no_hp")
    val noHp: String,

    @SerializedName("alamat")
    val alamat: String? = null,

    @SerializedName("catatan")
    val catatan: String? = null
)
