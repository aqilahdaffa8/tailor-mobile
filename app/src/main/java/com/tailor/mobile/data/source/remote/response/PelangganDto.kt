package com.tailor.mobile.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class PelangganDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nama")
    val nama: String,

    @SerializedName("no_hp")
    val noHp: String? = null,

    @SerializedName("alamat")
    val alamat: String? = null,

    @SerializedName("catatan")
    val catatan: String? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null,

    @SerializedName("pesanan_count")
    val pesananCount: Int? = null,

    @SerializedName("pesanan")
    val pesanan: List<PelangganPesananDto>? = emptyList(),

    @SerializedName("pengukuran")
    val pengukuran: List<PengukuranDto>? = emptyList()
)

data class PelangganPesananDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nomor_pesanan")
    val nomorPesanan: String,

    @SerializedName("pelanggan_id")
    val pelangganId: Int,

    @SerializedName("tanggal_pesan")
    val tanggalPesan: String? = null,

    @SerializedName("deadline")
    val deadline: String? = null,

    @SerializedName("catatan")
    val catatan: String? = null,

    @SerializedName("status")
    val status: String,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("item_pesanan_count")
    val itemPesananCount: Int? = null
)
