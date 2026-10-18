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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tailor.mobile.data.source.remote.response.PelangganDto
import com.tailor.mobile.ui.components.TailorButton
import com.tailor.mobile.ui.components.TailorButtonVariant
import com.tailor.mobile.ui.components.TailorTextField
import com.tailor.mobile.ui.theme.PureWhite
import com.tailor.mobile.ui.theme.Rose50
import com.tailor.mobile.ui.theme.Rose600
import com.tailor.mobile.ui.theme.Zinc200
import com.tailor.mobile.ui.theme.Zinc500
import com.tailor.mobile.ui.theme.Zinc900

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PelangganFormSheet(
    sheetState: SheetState,
    initialData: PelangganDto? = null,
    isSaving: Boolean,
    errorMessage: String? = null,
    onDismiss: () -> Unit,
    onSave: (nama: String, noHp: String, alamat: String?, catatan: String?) -> Unit
) {
    var nama by remember { mutableStateOf(initialData?.nama ?: "") }
    var noHp by remember { mutableStateOf(initialData?.noHp ?: "") }
    var alamat by remember { mutableStateOf(initialData?.alamat ?: "") }
    var catatan by remember { mutableStateOf(initialData?.catatan ?: "") }
    var validationError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(initialData) {
        nama = initialData?.nama ?: ""
        noHp = initialData?.noHp ?: ""
        alamat = initialData?.alamat ?: ""
        catatan = initialData?.catatan ?: ""
        validationError = null
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
            Text(
                text = if (initialData == null) "Tambah Pelanggan Baru" else "Edit Data Pelanggan",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Zinc900
            )
            Text(
                text = "Simpan data identitas dan kontak pelanggan",
                fontSize = 13.sp,
                color = Zinc500,
                modifier = Modifier.padding(top = 2.dp, bottom = 20.dp)
            )

            if (!errorMessage.isNullOrBlank() || validationError != null) {
                val errorText = validationError ?: errorMessage.orEmpty()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .background(Rose50, shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
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
                value = nama,
                onValueChange = {
                    nama = it
                    validationError = null
                },
                label = "Nama Lengkap *",
                placeholder = "Contoh: Budi Santoso",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            TailorTextField(
                value = noHp,
                onValueChange = {
                    noHp = it
                    validationError = null
                },
                label = "Nomor HP / WhatsApp *",
                placeholder = "Contoh: 081234567890",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            TailorTextField(
                value = alamat,
                onValueChange = { alamat = it },
                label = "Alamat (Opsional)",
                placeholder = "Contoh: Jl. Mawar No. 12, Jakarta",
                singleLine = false,
                maxLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            TailorTextField(
                value = catatan,
                onValueChange = { catatan = it },
                label = "Catatan Tambahan (Opsional)",
                placeholder = "Preferensi kain, model langganan, dll.",
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
                    text = if (initialData == null) "Simpan" else "Perbarui",
                    onClick = {
                        if (nama.isBlank()) {
                            validationError = "Nama pelanggan wajib diisi."
                            return@TailorButton
                        }
                        if (noHp.isBlank()) {
                            validationError = "Nomor HP wajib diisi."
                            return@TailorButton
                        }
                        onSave(nama, noHp, alamat, catatan)
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
