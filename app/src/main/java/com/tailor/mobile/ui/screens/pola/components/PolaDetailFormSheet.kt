package com.tailor.mobile.ui.screens.pola.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tailor.mobile.data.source.remote.response.PolaDetailDto
import com.tailor.mobile.ui.components.TailorButton
import com.tailor.mobile.ui.components.TailorButtonVariant
import com.tailor.mobile.ui.components.TailorTextField
import com.tailor.mobile.ui.theme.ControlShape
import com.tailor.mobile.ui.theme.PillShape
import com.tailor.mobile.ui.theme.PureWhite
import com.tailor.mobile.ui.theme.Rose600
import com.tailor.mobile.ui.theme.Zinc100
import com.tailor.mobile.ui.theme.Zinc500
import com.tailor.mobile.ui.theme.Zinc600
import com.tailor.mobile.ui.theme.Zinc900

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PolaDetailFormSheet(
    initialDetail: PolaDetailDto? = null,
    isSaving: Boolean,
    errorMessage: String? = null,
    onDismiss: () -> Unit,
    onSubmit: (size: String, dataUkuran: Map<String, String>, catatan: String?) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val commonSizes = listOf("XS", "S", "M", "L", "XL", "XXL", "All Size")
    var selectedSize by remember {
        mutableStateOf(initialDetail?.size ?: "M")
    }
    var customSizeText by remember {
        mutableStateOf(if (initialDetail?.size !in commonSizes && initialDetail?.size != null) initialDetail.size else "")
    }
    var catatan by remember { mutableStateOf(initialDetail?.catatan ?: "") }
    var localValidation by remember { mutableStateOf<String?>(null) }

    // Map measurement fields
    val measurements = remember {
        mutableStateMapOf<String, String>().apply {
            initialDetail?.dataUkuran?.forEach { (k, v) ->
                put(k, v?.toString() ?: "")
            }
        }
    }

    fun getMeasurement(key: String): String = measurements[key] ?: ""
    fun setMeasurement(key: String, value: String) {
        if (value.isBlank()) {
            measurements.remove(key)
        } else {
            measurements[key] = value
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = PureWhite,
        shape = ControlShape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Text(
                text = if (initialDetail == null) "Tambah Ukuran Standar" else "Edit Ukuran Standar",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Zinc900
            )

            if (errorMessage != null || localValidation != null) {
                Text(
                    text = localValidation ?: errorMessage ?: "",
                    color = Rose600,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Size selection
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Pilih Ukuran (Size) *",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Zinc900
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    commonSizes.forEach { sz ->
                        val isSelected = selectedSize == sz
                        Box(
                            modifier = Modifier
                                .clip(PillShape)
                                .background(if (isSelected) Zinc900 else Zinc100)
                                .clickable {
                                    selectedSize = sz
                                    customSizeText = ""
                                }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = sz,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (isSelected) PureWhite else Zinc600
                            )
                        }
                    }
                }

                TailorTextField(
                    value = customSizeText,
                    onValueChange = {
                        customSizeText = it
                        if (it.isNotBlank()) {
                            selectedSize = it.uppercase()
                        }
                    },
                    label = "Atau Ketik Ukuran Kustom",
                    placeholder = "Contoh: 3XL, Jumbo, Anak 10-12 thn"
                )
            }

            // Standard Measurements Section
            Text(
                text = "Dimensi Standar (cm)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Zinc900
            )

            // Upper body metrics
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    TailorTextField(
                        value = getMeasurement("lebar_bahu"),
                        onValueChange = { setMeasurement("lebar_bahu", it) },
                        label = "Lebar Bahu",
                        placeholder = "cm",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    TailorTextField(
                        value = getMeasurement("lingkar_dada"),
                        onValueChange = { setMeasurement("lingkar_dada", it) },
                        label = "Lingkar Dada",
                        placeholder = "cm",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    TailorTextField(
                        value = getMeasurement("panjang_baju"),
                        onValueChange = { setMeasurement("panjang_baju", it) },
                        label = "Panjang Baju",
                        placeholder = "cm",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    TailorTextField(
                        value = getMeasurement("panjang_lengan"),
                        onValueChange = { setMeasurement("panjang_lengan", it) },
                        label = "Panjang Lengan",
                        placeholder = "cm",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    TailorTextField(
                        value = getMeasurement("lingkar_pinggang"),
                        onValueChange = { setMeasurement("lingkar_pinggang", it) },
                        label = "Lingkar Pinggang",
                        placeholder = "cm",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    TailorTextField(
                        value = getMeasurement("lingkar_pinggul"),
                        onValueChange = { setMeasurement("lingkar_pinggul", it) },
                        label = "Lingkar Pinggul",
                        placeholder = "cm",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                }
            }

            // Lower body metrics
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    TailorTextField(
                        value = getMeasurement("panjang_celana"),
                        onValueChange = { setMeasurement("panjang_celana", it) },
                        label = "Panjang Celana/Rok",
                        placeholder = "cm",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    TailorTextField(
                        value = getMeasurement("lingkar_paha"),
                        onValueChange = { setMeasurement("lingkar_paha", it) },
                        label = "Lingkar Paha",
                        placeholder = "cm",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                }
            }

            TailorTextField(
                value = catatan,
                onValueChange = { catatan = it },
                label = "Catatan Pola (Opsional)",
                placeholder = "Contoh: Toleransi potongan 1.5 cm, khusus bahan katun",
                singleLine = false,
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TailorButton(
                    text = "Batal",
                    onClick = onDismiss,
                    variant = TailorButtonVariant.Secondary,
                    modifier = Modifier.weight(1f)
                )
                TailorButton(
                    text = if (isSaving) "Menyimpan..." else "Simpan",
                    onClick = {
                        val finalSize = if (customSizeText.isNotBlank()) customSizeText.trim() else selectedSize.trim()
                        if (finalSize.isBlank()) {
                            localValidation = "Ukuran (size) wajib ditentukan."
                            return@TailorButton
                        }
                        onSubmit(finalSize, measurements.toMap(), catatan)
                    },
                    enabled = !isSaving,
                    variant = TailorButtonVariant.Primary,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
