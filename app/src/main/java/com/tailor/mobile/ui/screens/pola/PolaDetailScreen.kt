package com.tailor.mobile.ui.screens.pola

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tailor.mobile.data.source.remote.response.PolaDetailDto
import com.tailor.mobile.ui.components.StatusBadge
import com.tailor.mobile.ui.components.TailorButton
import com.tailor.mobile.ui.components.TailorButtonVariant
import com.tailor.mobile.ui.components.TailorCard
import com.tailor.mobile.ui.screens.pola.components.PolaDetailFormSheet
import com.tailor.mobile.ui.theme.ControlShape
import com.tailor.mobile.ui.theme.DialogShape
import com.tailor.mobile.ui.theme.PillShape
import com.tailor.mobile.ui.theme.PureWhite
import com.tailor.mobile.ui.theme.Rose600
import com.tailor.mobile.ui.theme.Zinc100
import com.tailor.mobile.ui.theme.Zinc200
import com.tailor.mobile.ui.theme.Zinc400
import com.tailor.mobile.ui.theme.Zinc50
import com.tailor.mobile.ui.theme.Zinc500
import com.tailor.mobile.ui.theme.Zinc600
import com.tailor.mobile.ui.theme.Zinc900

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PolaDetailScreen(
    viewModel: PolaViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val jenis = uiState.selectedJenisPakaian ?: return
    val snackbarHostState = remember { SnackbarHostState() }

    var showDeleteJenisDialog by remember { mutableStateOf(false) }
    var showDeleteSizeDialog by remember { mutableStateOf(false) }
    var showAddSizeSheet by remember { mutableStateOf(false) }
    var editingSizeDetail by remember { mutableStateOf<PolaDetailDto?>(null) }

    LaunchedEffect(uiState.successSnackbar) {
        uiState.successSnackbar?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbar()
        }
    }

    val selectedDetail: PolaDetailDto? = uiState.selectedJenisDetails.getOrNull(uiState.selectedSizeIndex)

    Scaffold(
        containerColor = Zinc50,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = jenis.namaJenis,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Zinc900
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Zinc900
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showDeleteJenisDialog = true }) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Hapus Model Pakaian",
                            tint = Rose600
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PureWhite
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Info Card
            TailorCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatusBadge(status = "pola")
                        Text(
                            text = "${uiState.selectedJenisDetails.size} Ukuran Tersedia",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Zinc500
                        )
                    }

                    Text(
                        text = jenis.namaJenis,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Zinc900
                    )

                    if (!jenis.deskripsi.isNullOrBlank()) {
                        Text(
                            text = jenis.deskripsi,
                            fontSize = 13.sp,
                            color = Zinc600,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // Size Selector Tabs Section
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Standar Ukuran Pola",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Zinc900
                    )

                    if (uiState.selectedJenisDetails.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .clip(PillShape)
                                .background(Zinc100)
                                .clickable { showAddSizeSheet = true }
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Add,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Zinc900
                            )
                            Text(
                                text = "Tambah Size",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Zinc900
                            )
                        }
                    }
                }

                if (uiState.isLoadingDetail) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Zinc900, strokeWidth = 2.dp)
                    }
                } else if (uiState.selectedJenisDetails.isEmpty()) {
                    // Empty sizes state
                    TailorCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Zinc100),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Straighten,
                                    contentDescription = null,
                                    tint = Zinc500
                                )
                            }
                            Text(
                                text = "Belum Ada Standar Ukuran",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Zinc900
                            )
                            Text(
                                text = "Tentukan acuan dimensi potongan untuk ukuran S, M, L, XL, dll.",
                                fontSize = 13.sp,
                                color = Zinc500,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            TailorButton(
                                text = "+ Tambah Ukuran Pertama",
                                onClick = { showAddSizeSheet = true },
                                variant = TailorButtonVariant.Primary
                            )
                        }
                    }
                } else {
                    // Size Pills Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        uiState.selectedJenisDetails.forEachIndexed { index, detail ->
                            val isSelected = index == uiState.selectedSizeIndex
                            Box(
                                modifier = Modifier
                                    .clip(PillShape)
                                    .background(if (isSelected) Zinc900 else PureWhite)
                                    .clickable { viewModel.setSelectedSizeIndex(index) }
                                    .padding(horizontal = 18.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    text = "Size ${detail.size}",
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) PureWhite else Zinc900
                                )
                            }
                        }
                    }

                    // Selected Size Detail Card
                    if (selectedDetail != null) {
                        TailorCard(modifier = Modifier.fillMaxWidth()) {
                            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Dimensi Acuan Size ${selectedDetail.size}",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Zinc900
                                    )
                                }

                                val ukuranMap = selectedDetail.dataUkuran ?: emptyMap()
                                if (ukuranMap.isEmpty()) {
                                    Text(
                                        text = "Belum ada detail angka dimensi pada ukuran ini.",
                                        fontSize = 13.sp,
                                        color = Zinc500
                                    )
                                } else {
                                    FlowRow(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        ukuranMap.forEach { (key, value) ->
                                            val label = formatMeasurementLabel(key)
                                            val valueStr = value?.toString() ?: "-"
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(Zinc100)
                                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                                            ) {
                                                Column {
                                                    Text(
                                                        text = label,
                                                        fontSize = 11.sp,
                                                        color = Zinc500,
                                                        fontWeight = FontWeight.Medium
                                                    )
                                                    Text(
                                                        text = "$valueStr cm",
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Zinc900
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                if (!selectedDetail.catatan.isNullOrBlank()) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Zinc50)
                                            .padding(12.dp),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = "Catatan Pola",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Zinc500
                                        )
                                        Text(
                                            text = selectedDetail.catatan,
                                            fontSize = 13.sp,
                                            color = Zinc900
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                // Size actions
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    TailorButton(
                                        text = "Edit Ukuran",
                                        onClick = { editingSizeDetail = selectedDetail },
                                        variant = TailorButtonVariant.Secondary,
                                        modifier = Modifier.weight(1f)
                                    )
                                    TailorButton(
                                        text = "Hapus Size",
                                        onClick = { showDeleteSizeDialog = true },
                                        variant = TailorButtonVariant.Danger,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal Sheet: Add new size
    if (showAddSizeSheet) {
        PolaDetailFormSheet(
            initialDetail = null,
            isSaving = uiState.isSaving,
            errorMessage = uiState.formError,
            onDismiss = {
                showAddSizeSheet = false
                viewModel.clearFormError()
            },
            onSubmit = { size, dataUkuran, catatan ->
                viewModel.savePolaDetail(
                    detailId = null,
                    jenisPakaianId = jenis.id,
                    size = size,
                    dataUkuran = dataUkuran,
                    catatan = catatan,
                    onSuccess = {
                        showAddSizeSheet = false
                    }
                )
            }
        )
    }

    // Modal Sheet: Edit size
    if (editingSizeDetail != null) {
        PolaDetailFormSheet(
            initialDetail = editingSizeDetail,
            isSaving = uiState.isSaving,
            errorMessage = uiState.formError,
            onDismiss = {
                editingSizeDetail = null
                viewModel.clearFormError()
            },
            onSubmit = { size, dataUkuran, catatan ->
                viewModel.savePolaDetail(
                    detailId = editingSizeDetail?.id,
                    jenisPakaianId = jenis.id,
                    size = size,
                    dataUkuran = dataUkuran,
                    catatan = catatan,
                    onSuccess = {
                        editingSizeDetail = null
                    }
                )
            }
        )
    }

    // Dialog: Delete Jenis Pakaian
    if (showDeleteJenisDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteJenisDialog = false },
            title = {
                Text(
                    text = "Hapus Jenis Pakaian?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = Zinc900
                )
            },
            text = {
                Text(
                    text = "Model pakaian '${jenis.namaJenis}' dan seluruh standar ukurannya akan dihapus secara permanen.",
                    fontSize = 14.sp,
                    color = Zinc600
                )
            },
            confirmButton = {
                TailorButton(
                    text = "Hapus",
                    onClick = {
                        showDeleteJenisDialog = false
                        viewModel.deleteJenisPakaian(jenis.id, onSuccess = onBack)
                    },
                    variant = TailorButtonVariant.Danger
                )
            },
            dismissButton = {
                TailorButton(
                    text = "Batal",
                    onClick = { showDeleteJenisDialog = false },
                    variant = TailorButtonVariant.Secondary
                )
            },
            containerColor = PureWhite,
            shape = DialogShape
        )
    }

    // Dialog: Delete Size
    if (showDeleteSizeDialog && selectedDetail != null) {
        AlertDialog(
            onDismissRequest = { showDeleteSizeDialog = false },
            title = {
                Text(
                    text = "Hapus Ukuran ${selectedDetail.size}?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = Zinc900
                )
            },
            text = {
                Text(
                    text = "Standar acuan dimensi ukuran ${selectedDetail.size} akan dihapus dari model ini.",
                    fontSize = 14.sp,
                    color = Zinc600
                )
            },
            confirmButton = {
                TailorButton(
                    text = "Hapus",
                    onClick = {
                        showDeleteSizeDialog = false
                        viewModel.deletePolaDetail(
                            detailId = selectedDetail.id,
                            jenisPakaianId = jenis.id,
                            onSuccess = {
                                viewModel.setSelectedSizeIndex(0)
                            }
                        )
                    },
                    variant = TailorButtonVariant.Danger
                )
            },
            dismissButton = {
                TailorButton(
                    text = "Batal",
                    onClick = { showDeleteSizeDialog = false },
                    variant = TailorButtonVariant.Secondary
                )
            },
            containerColor = PureWhite,
            shape = DialogShape
        )
    }
}

private fun formatMeasurementLabel(key: String): String {
    return key.replace("_", " ")
        .split(" ")
        .joinToString(" ") { word ->
            word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }
}
