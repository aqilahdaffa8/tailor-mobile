package com.tailor.mobile.ui.screens.pelanggan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tailor.mobile.data.source.remote.response.PengukuranDto
import com.tailor.mobile.ui.components.TailorButton
import com.tailor.mobile.ui.components.TailorButtonVariant
import com.tailor.mobile.ui.components.TailorTextField
import com.tailor.mobile.ui.theme.PureWhite
import com.tailor.mobile.ui.theme.Rose50
import com.tailor.mobile.ui.theme.Rose600
import com.tailor.mobile.ui.theme.Zinc100
import com.tailor.mobile.ui.theme.Zinc200
import com.tailor.mobile.ui.theme.Zinc500
import com.tailor.mobile.ui.theme.Zinc700
import com.tailor.mobile.ui.theme.Zinc900
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PengukuranFormSheet(
    sheetState: SheetState,
    pelangganNama: String,
    latestPengukuran: PengukuranDto? = null,
    isSaving: Boolean,
    errorMessage: String? = null,
    onDismiss: () -> Unit,
    onSave: (tanggalUkur: String, dataUkuran: Map<String, String>, catatan: String?) -> Unit
) {
    val todayDate = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }
    var tanggalUkur by remember { mutableStateOf(todayDate) }
    var catatan by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }

    // Standard measurements map
    val ukuranMap = remember {
        mutableStateMapOf(
            "lebar_bahu" to "",
            "lingkar_dada" to "",
            "panjang_baju" to "",
            "lingkar_leher" to "",
            "panjang_lengan" to "",
            "lingkar_kerung_lengan" to "",
            "lingkar_pinggang" to "",
            "lingkar_pinggul" to "",
            "panjang_celana" to "",
            "lingkar_paha" to ""
        )
    }

    val upperBodyFields = listOf(
        "lebar_bahu" to "Lebar Bahu (cm)",
        "lingkar_dada" to "Lingkar Dada (cm)",
        "panjang_baju" to "Panjang Baju (cm)",
        "lingkar_leher" to "Lingkar Leher (cm)",
        "panjang_lengan" to "Panjang Lengan (cm)",
        "lingkar_kerung_lengan" to "Lingkar Kerung Lengan (cm)"
    )

    val lowerBodyFields = listOf(
        "lingkar_pinggang" to "Lingkar Pinggang (cm)",
        "lingkar_pinggul" to "Lingkar Pinggul (cm)",
        "panjang_celana" to "Panjang Celana/Rok (cm)",
        "lingkar_paha" to "Lingkar Paha (cm)"
    )

    fun copyFromLatest() {
        if (latestPengukuran?.dataUkuran != null) {
            latestPengukuran.dataUkuran.forEach { (key, value) ->
                ukuranMap[key] = value
            }
            if (!latestPengukuran.catatan.isNullOrBlank()) {
                catatan = latestPengukuran.catatan
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = PureWhite,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Catat Pengukuran Tubuh",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Zinc900
                    )
                    Text(
                        text = "Untuk: $pelangganNama",
                        fontSize = 13.sp,
                        color = Zinc500
                    )
                }

                if (latestPengukuran?.dataUkuran != null && latestPengukuran.dataUkuran.isNotEmpty()) {
                    OutlinedButton(
                        onClick = { copyFromLatest() },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ContentCopy,
                            contentDescription = null,
                            tint = Zinc700,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text(
                            text = "Copy Terakhir",
                            fontSize = 12.sp,
                            color = Zinc900
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (!errorMessage.isNullOrBlank() || validationError != null) {
                val errorText = validationError ?: errorMessage.orEmpty()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .background(Rose50, shape = RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = errorText,
                        color = Rose600,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            TailorTextField(
                value = tanggalUkur,
                onValueChange = { tanggalUkur = it },
                label = "Tanggal Pengukuran (YYYY-MM-DD) *",
                placeholder = "YYYY-MM-DD",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Upper Body Section
            Text(
                text = "Bagian Atas & Baju",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Zinc900,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Grid 2 Columns for fields
            upperBodyFields.chunked(2).forEach { rowFields ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    rowFields.forEach { (key, label) ->
                        TailorTextField(
                            value = ukuranMap[key] ?: "",
                            onValueChange = { ukuranMap[key] = it },
                            label = label,
                            placeholder = "0",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (rowFields.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Lower Body Section
            Text(
                text = "Bagian Bawah & Celana",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Zinc900,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            lowerBodyFields.chunked(2).forEach { rowFields ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    rowFields.forEach { (key, label) ->
                        TailorTextField(
                            value = ukuranMap[key] ?: "",
                            onValueChange = { ukuranMap[key] = it },
                            label = label,
                            placeholder = "0",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (rowFields.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            TailorTextField(
                value = catatan,
                onValueChange = { catatan = it },
                label = "Catatan Pengukuran (Opsional)",
                placeholder = "Misal: Lebih lebar di dada, kerung lengan agak longgar",
                singleLine = false,
                maxLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TailorButton(
                    text = "Batal",
                    onClick = onDismiss,
                    variant = TailorButtonVariant.Secondary,
                    modifier = Modifier.weight(1f),
                    enabled = !isSaving
                )

                TailorButton(
                    text = "Simpan Ukuran",
                    onClick = {
                        val activeMeasurements = ukuranMap.filterValues { it.isNotBlank() }
                        if (activeMeasurements.isEmpty()) {
                            validationError = "Masukkan minimal salah satu data ukuran tubuh."
                            return@TailorButton
                        }
                        if (tanggalUkur.isBlank()) {
                            validationError = "Tanggal pengukuran tidak boleh kosong."
                            return@TailorButton
                        }
                        onSave(tanggalUkur, activeMeasurements, catatan)
                    },
                    variant = TailorButtonVariant.Primary,
                    modifier = Modifier.weight(1f),
                    isLoading = isSaving
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
