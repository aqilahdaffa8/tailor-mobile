package com.tailor.mobile.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class PesananDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nomor_pesanan")
    val nomorPesanan: String,

    @SerializedName("pelanggan_id")
    val pelangganId: Int,

    @SerializedName("tanggal_pesan")
    val tanggalPesan: String? = null,

    @SerializedName("deadline")
    val deadline: String,

    @SerializedName("catatan")
    val catatan: String? = null,

    @SerializedName("status")
    val status: String, // aktif, selesai, diambil, dibatalkan

    // Computed / function fields in list & detail
    @SerializedName("total_harga")
    val totalHarga: Double? = null,

    @SerializedName("total_tagihan")
    val totalTagihan: String? = null,

    @SerializedName("total_dibayar")
    val totalDibayar: Double? = null,

    @SerializedName("sisa_tagihan")
    val sisaTagihan: String? = null,

    @SerializedName("sisa_pembayaran")
    val sisaPembayaran: Double? = null,

    @SerializedName("status_bayar")
    val statusBayar: String? = null,

    @SerializedName("status_pembayaran")
    val statusPembayaran: String? = null,

    @SerializedName("item_pesanan_count")
    val itemPesananCount: Int? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null,

    // Relations
    @SerializedName("pelanggan")
    val pelanggan: PelangganDto? = null,

    @SerializedName("item_pesanan")
    val itemPesanan: List<ItemPesananDto>? = null,

    @SerializedName("pembayaran")
    val pembayaran: List<PembayaranDto>? = null
) {
    fun getEffectiveTotal(): Double {
        return totalHarga ?: totalTagihan?.toDoubleOrNull() ?: 0.0
    }

    fun getEffectiveSisa(): Double {
        return sisaPembayaran ?: sisaTagihan?.toDoubleOrNull() ?: 0.0
    }

    fun getEffectiveStatusBayar(): String {
        return statusPembayaran ?: statusBayar ?: "Belum Bayar"
    }
}

data class ItemPesananDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("pesanan_id")
    val pesananId: Int,

    @SerializedName("jenis_jasa_id")
    val jenisJasaId: Int,

    @SerializedName("pola_detail_id")
    val polaDetailId: Int? = null,

    @SerializedName("pengukuran_id")
    val pengukuranId: Int? = null,

    @SerializedName("detail_pekerjaan")
    val detailPekerjaan: String? = null,

    @SerializedName("harga")
    val harga: String,

    @SerializedName("deadline")
    val deadline: String? = null,

    @SerializedName("catatan")
    val catatan: String? = null,

    @SerializedName("status_item")
    val statusItem: String,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null,

    @SerializedName("jenis_jasa")
    val jenisJasa: JenisJasaDto? = null,

    @SerializedName("pola_detail")
    val polaDetail: PolaDetailDto? = null,

    @SerializedName("pengukuran")
    val pengukuran: PengukuranDto? = null,

    @SerializedName("progress")
    val progress: List<ProgressItemDto>? = null,

    @SerializedName("pemakaian_bahan")
    val pemakaianBahan: List<PemakaianBahanDto>? = null,

    @SerializedName("foto_referensi")
    val fotoReferensi: List<FotoReferensiDto>? = null
)

data class JenisJasaDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nama_jasa")
    val namaJasa: String,

    @SerializedName("deskripsi")
    val deskripsi: String? = null,

    @SerializedName("harga_default")
    val hargaDefault: String? = null,

    @SerializedName("is_active")
    val isActive: Boolean? = true
)

data class ProgressItemDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("item_pesanan_id")
    val itemPesananId: Int,

    @SerializedName("nama_tahapan")
    val namaTahapan: String,

    @SerializedName("urutan")
    val urutan: Int,

    @SerializedName("is_completed")
    val isCompleted: Boolean = false,

    @SerializedName("tanggal_selesai")
    val tanggalSelesai: String? = null
)

data class PembayaranDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("pesanan_id")
    val pesananId: Int,

    @SerializedName("jumlah")
    val jumlah: String,

    @SerializedName("metode")
    val metode: String,

    @SerializedName("tanggal_bayar")
    val tanggalBayar: String,

    @SerializedName("catatan")
    val catatan: String? = null
)

data class PemakaianBahanDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("item_pesanan_id")
    val itemPesananId: Int,

    @SerializedName("bahan_id")
    val bahanId: Int,

    @SerializedName("jumlah")
    val jumlah: String,

    @SerializedName("catatan")
    val catatan: String? = null,

    @SerializedName("bahan")
    val bahan: BahanItemDto? = null
)

data class BahanItemDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nama_bahan")
    val namaBahan: String,

    @SerializedName("satuan")
    val satuan: String,

    @SerializedName("stok")
    val stok: Double = 0.0
)

data class FotoReferensiDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("item_pesanan_id")
    val itemPesananId: Int,

    @SerializedName("path_foto")
    val pathFoto: String,

    @SerializedName("keterangan")
    val keterangan: String? = null
)
