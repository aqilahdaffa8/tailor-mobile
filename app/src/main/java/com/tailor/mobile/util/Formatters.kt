package com.tailor.mobile.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

object Formatters {

    private val localeIndonesia = Locale("id", "ID")
    private val currencyFormat = NumberFormat.getCurrencyInstance(localeIndonesia).apply {
        maximumFractionDigits = 0
    }

    fun formatRupiah(amount: Double): String {
        return try {
            currencyFormat.format(amount).replace("Rp ", "Rp")
        } catch (e: Exception) {
            "Rp0"
        }
    }

    fun formatRupiah(amountString: String?): String {
        val amount = amountString?.toDoubleOrNull() ?: 0.0
        return formatRupiah(amount)
    }

    fun formatTanggal(dateStr: String?): String {
        if (dateStr.isNullOrBlank()) return "-"
        return try {
            // Check for ISO format with time
            val inputFormat = if (dateStr.contains("T")) {
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.US)
            } else {
                SimpleDateFormat("yyyy-MM-dd", Locale.US)
            }
            val date = inputFormat.parse(dateStr)
            val outputFormat = SimpleDateFormat("dd MMM yyyy", localeIndonesia)
            date?.let { outputFormat.format(it) } ?: dateStr
        } catch (e: Exception) {
            // Fallback: take YYYY-MM-DD
            dateStr.take(10)
        }
    }

    fun formatDate(dateStr: String?): String = formatTanggal(dateStr)
}
