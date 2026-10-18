package com.tailor.mobile.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class DashboardSummaryDto(
    @SerializedName("periode")
    val periode: PeriodeDto,

    @SerializedName("pesanan_aktif")
    val pesananAktif: Int = 0,

    @SerializedName("pesanan_mendekati_deadline")
    val pesananMendekatiDeadline: Int = 0,

    @SerializedName("pesanan_terlambat")
    val pesananTerlambat: Int = 0,

    @SerializedName("pembayaran_belum_lunas")
    val pembayaranBelumLunas: PembayaranBelumLunasDto,

    @SerializedName("keuangan_bulan_ini")
    val keuanganBulanIni: KeuanganBulanIniDto,

    @SerializedName("stok_menipis")
    val stokMenipis: Int = 0
)

data class PeriodeDto(
    @SerializedName("label")
    val label: String,

    @SerializedName("awal")
    val awal: String,

    @SerializedName("akhir")
    val akhir: String
)

data class PembayaranBelumLunasDto(
    @SerializedName("jumlah_pesanan")
    val jumlahPesanan: Int = 0,

    @SerializedName("total_sisa")
    val totalSisa: Double = 0.0
)

data class KeuanganBulanIniDto(
    @SerializedName("pendapatan")
    val pendapatan: Double = 0.0,

    @SerializedName("pengeluaran")
    val pengeluaran: Double = 0.0,

    @SerializedName("selisih")
    val selisih: Double = 0.0
)

data class PesananRingkasDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nomor_pesanan")
    val nomorPesanan: String,

    @SerializedName("pelanggan_id")
    val pelangganId: Int,

    @SerializedName("tanggal_pesan")
    val tanggalPesan: String,

    @SerializedName("deadline")
    val deadline: String,

    @SerializedName("catatan")
    val catatan: String? = null,

    @SerializedName("status")
    val status: String,

    @SerializedName("sisa_tagihan")
    val sisaTagihan: String? = null,

    @SerializedName("status_bayar")
    val statusBayar: String? = null,

    @SerializedName("item_pesanan_count")
    val itemPesananCount: Int = 0,

    @SerializedName("total_tagihan")
    val totalTagihan: String? = null,

    @SerializedName("pelanggan")
    val pelanggan: PelangganRingkasDto? = null
)

data class PelangganRingkasDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nama")
    val nama: String,

    @SerializedName("no_hp")
    val noHp: String? = null,

    @SerializedName("alamat")
    val alamat: String? = null
)

data class BahanMenipisDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nama_bahan")
    val namaBahan: String,

    @SerializedName("satuan")
    val satuan: String,

    @SerializedName("stok")
    val stok: Double = 0.0,

    @SerializedName("stok_minimum")
    val stokMinimum: Double = 0.0
)

data class ReminderDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("judul")
    val judul: String,

    @SerializedName("pesan")
    val pesan: String,

    @SerializedName("tipe")
    val tipe: String,

    @SerializedName("referensi_id")
    val referensiId: Int? = null,

    @SerializedName("referensi_tipe")
    val referensiTipe: String? = null,

    @SerializedName("is_read")
    val isRead: Boolean = false
)
