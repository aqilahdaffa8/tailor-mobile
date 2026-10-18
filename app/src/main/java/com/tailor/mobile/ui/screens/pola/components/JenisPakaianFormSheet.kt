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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tailor.mobile.data.source.remote.response.KategoriPolaDto
import com.tailor.mobile.ui.components.TailorButton
import com.tailor.mobile.ui.components.TailorButtonVariant
import com.tailor.mobile.ui.components.TailorTextField
import com.tailor.mobile.ui.theme.ControlShape
import com.tailor.mobile.ui.theme.PillShape
import com.tailor.mobile.ui.theme.PureWhite
import com.tailor.mobile.ui.theme.Rose600
import com.tailor.mobile.ui.theme.Zinc100
import com.tailor.mobile.ui.theme.Zinc200
import com.tailor.mobile.ui.theme.Zinc500
import com.tailor.mobile.ui.theme.Zinc600
import com.tailor.mobile.ui.theme.Zinc900

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun JenisPakaianFormSheet(
    kategoriList: List<KategoriPolaDto>,
    initialKategoriId: Int? = null,
    isSaving: Boolean,
    errorMessage: String? = null,
    onDismiss: () -> Unit,
    onSubmit: (kategoriId: Int, nama: String, deskripsi: String?) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var selectedKategoriId by remember {
        mutableStateOf(initialKategoriId ?: kategoriList.firstOrNull()?.id ?: 0)
    }
    var namaJenis by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }
    var localValidation by remember { mutableStateOf<String?>(null) }

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tambah Jenis Pakaian",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Zinc900
                )
            }

            if (errorMessage != null || localValidation != null) {
                Text(
                    text = localValidation ?: errorMessage ?: "",
                    color = Rose600,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Category Selection Chips
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Kategori Pakaian *",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Zinc900
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    kategoriList.forEach { kategori ->
                        val isSelected = selectedKategoriId == kategori.id
                        Box(
                            modifier = Modifier
                                .clip(PillShape)
                                .background(if (isSelected) Zinc900 else Zinc100)
                                .clickable { selectedKategoriId = kategori.id }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = kategori.namaKategori,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (isSelected) PureWhite else Zinc600
                            )
                        }
                    }
                }
            }

            TailorTextField(
                value = namaJenis,
                onValueChange = {
                    namaJenis = it
                    localValidation = null
                },
                label = "Nama Model / Jenis Pakaian *",
                placeholder = "Contoh: Kemaja Slim Fit, Celana Chino, Dress A-Line"
            )

            TailorTextField(
                value = deskripsi,
                onValueChange = { deskripsi = it },
                label = "Deskripsi / Catatan Pola (Opsional)",
                placeholder = "Keterangan potongan pola atau acuan standar",
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
                        if (selectedKategoriId == 0) {
                            localValidation = "Pilih kategori pakaian terlebih dahulu."
                            return@TailorButton
                        }
                        if (namaJenis.isBlank()) {
                            localValidation = "Nama jenis pakaian wajib diisi."
                            return@TailorButton
                        }
                        onSubmit(selectedKategoriId, namaJenis, deskripsi)
                    },
                    enabled = !isSaving,
                    variant = TailorButtonVariant.Primary,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
