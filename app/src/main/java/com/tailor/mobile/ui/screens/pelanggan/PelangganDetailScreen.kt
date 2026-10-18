package com.tailor.mobile.ui.screens.pelanggan

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tailor.mobile.data.source.remote.response.PelangganDto
import com.tailor.mobile.data.source.remote.response.PelangganPesananDto
import com.tailor.mobile.data.source.remote.response.PengukuranDto
import com.tailor.mobile.ui.components.StatusBadge
import com.tailor.mobile.ui.components.TailorButton
import com.tailor.mobile.ui.components.TailorButtonVariant
import com.tailor.mobile.ui.components.TailorCard
import com.tailor.mobile.ui.screens.pelanggan.components.PelangganFormSheet
import com.tailor.mobile.ui.screens.pelanggan.components.PengukuranFormSheet
import com.tailor.mobile.ui.theme.PureWhite
import com.tailor.mobile.ui.theme.Rose50
import com.tailor.mobile.ui.theme.Rose600
import com.tailor.mobile.ui.theme.Zinc100
import com.tailor.mobile.ui.theme.Zinc200
import com.tailor.mobile.ui.theme.Zinc400
import com.tailor.mobile.ui.theme.Zinc50
import com.tailor.mobile.ui.theme.Zinc500
import com.tailor.mobile.ui.theme.Zinc700
import com.tailor.mobile.ui.theme.Zinc800
import com.tailor.mobile.ui.theme.Zinc900
import com.tailor.mobile.util.Formatters
import kotlinx.coroutines.launch

enum class PelangganDetailTab {
    PENGUKURAN,
    PESANAN
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PelangganDetailScreen(
    viewModel: PelangganViewModel,
    pelanggan: PelangganDto,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var activeTab by remember { mutableStateOf(PelangganDetailTab.PENGUKURAN) }
    var showEditSheet by remember { mutableStateOf(false) }
    var showAddPengukuranSheet by remember { mutableStateOf(false) }
    var showDeletePelangganDialog by remember { mutableStateOf(false) }
    var pengukuranToDelete by remember { mutableStateOf<PengukuranDto?>(null) }

    val editSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val addPengukuranSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        containerColor = Zinc50,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detail Pelanggan",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Zinc900
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Zinc900
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showDeletePelangganDialog = true }) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Hapus Pelanggan",
                            tint = Rose600
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PureWhite)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(2.dp))
                // Customer Profile Card
                TailorCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar Initial
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Zinc900),
                            contentAlignment = Alignment.Center
                        ) {
                            val initial = pelanggan.nama.trim().firstOrNull()?.uppercase() ?: "P"
                            Text(
                                text = initial,
                                color = PureWhite,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = pelanggan.nama,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Zinc900
                            )

                            if (!pelanggan.noHp.isNullOrBlank()) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(top = 2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Phone,
                                        contentDescription = null,
                                        tint = Zinc500,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = pelanggan.noHp,
                                        fontSize = 13.sp,
                                        color = Zinc700,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }

                    if (!pelanggan.alamat.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Alamat:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Zinc500
                        )
                        Text(
                            text = pelanggan.alamat,
                            fontSize = 13.sp,
                            color = Zinc800,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    if (!pelanggan.catatan.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Catatan:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Zinc500
                        )
                        Text(
                            text = pelanggan.catatan,
                            fontSize = 13.sp,
                            color = Zinc800,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (!pelanggan.noHp.isNullOrBlank()) {
                            OutlinedButton(
                                onClick = {
                                    val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                                        data = Uri.parse("tel:${pelanggan.noHp}")
                                    }
                                    context.startActivity(dialIntent)
                                },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Phone,
                                    contentDescription = null,
                                    tint = Zinc900,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Telepon",
                                    fontSize = 13.sp,
                                    color = Zinc900,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        TailorButton(
                            text = "Edit Data",
                            onClick = { showEditSheet = true },
                            variant = TailorButtonVariant.Secondary,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Edit,
                                    contentDescription = null,
                                    tint = Zinc900,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Tabs: Pengukuran vs Riwayat Pesanan
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PureWhite, shape = RoundedCornerShape(8.dp))
                        .border(1.dp, Zinc200, shape = RoundedCornerShape(8.dp))
                        .padding(4.dp)
                ) {
                    val pengukuranCount = uiState.pengukuranList.size
                    val pesananCount = pelanggan.pesanan?.size ?: 0

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (activeTab == PelangganDetailTab.PENGUKURAN) Zinc900 else PureWhite)
                            .clickable { activeTab = PelangganDetailTab.PENGUKURAN }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Pengukuran ($pengukuranCount)",
                            fontSize = 13.sp,
                            fontWeight = if (activeTab == PelangganDetailTab.PENGUKURAN) FontWeight.SemiBold else FontWeight.Medium,
                            color = if (activeTab == PelangganDetailTab.PENGUKURAN) PureWhite else Zinc500
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (activeTab == PelangganDetailTab.PESANAN) Zinc900 else PureWhite)
                            .clickable { activeTab = PelangganDetailTab.PESANAN }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Pesanan ($pesananCount)",
                            fontSize = 13.sp,
                            fontWeight = if (activeTab == PelangganDetailTab.PESANAN) FontWeight.SemiBold else FontWeight.Medium,
                            color = if (activeTab == PelangganDetailTab.PESANAN) PureWhite else Zinc500
                        )
                    }
                }
            }

            // Tab 1: Pengukuran Content
            if (activeTab == PelangganDetailTab.PENGUKURAN) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Riwayat Pengukuran Tubuh",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Zinc900
                        )

                        TailorButton(
                            text = "+ Catat Ukuran",
                            onClick = { showAddPengukuranSheet = true },
                            variant = TailorButtonVariant.Primary
                        )
                    }
                }

                if (uiState.isLoadingPengukuran) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Zinc900, modifier = Modifier.size(28.dp))
                        }
                    }
                } else if (uiState.pengukuranList.isEmpty()) {
                    item {
                        TailorCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Straighten,
                                    contentDescription = null,
                                    tint = Zinc400,
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Belum Ada Catatan Ukuran",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Zinc900
                                )
                                Text(
                                    text = "Catat dimensi tubuh pelanggan agar tersimpan dan dapat digunakan kembali saat pemesanan.",
                                    fontSize = 13.sp,
                                    color = Zinc500,
                                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                } else {
                    items(uiState.pengukuranList, key = { it.id }) { item ->
                        MeasurementCard(
                            pengukuran = item,
                            onDeleteClick = { pengukuranToDelete = item }
                        )
                    }
                }
            }

            // Tab 2: Riwayat Pesanan Content
            if (activeTab == PelangganDetailTab.PESANAN) {
                val pesananList = pelanggan.pesanan ?: emptyList()
                if (pesananList.isEmpty()) {
                    item {
                        TailorCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Belum Ada Riwayat Pesanan",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Zinc900
                                )
                                Text(
                                    text = "Pesanan yang dibuat untuk pelanggan ini akan muncul di sini.",
                                    fontSize = 13.sp,
                                    color = Zinc500,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                } else {
                    items(pesananList, key = { it.id }) { item ->
                        CustomerOrderCard(order = item)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Modal Sheet Edit Customer
    if (showEditSheet) {
        PelangganFormSheet(
            sheetState = editSheetState,
            initialData = pelanggan,
            isSaving = uiState.isSaving,
            errorMessage = uiState.formError,
            onDismiss = {
                viewModel.clearFormError()
                showEditSheet = false
            },
            onSave = { nama, noHp, alamat, catatan ->
                viewModel.savePelanggan(
                    id = pelanggan.id,
                    nama = nama,
                    noHp = noHp,
                    alamat = alamat,
                    catatan = catatan,
                    onSuccess = {
                        scope.launch { editSheetState.hide() }
                        showEditSheet = false
                    }
                )
            }
        )
    }

    // Modal Sheet Add Measurement
    if (showAddPengukuranSheet) {
        PengukuranFormSheet(
            sheetState = addPengukuranSheetState,
            pelangganNama = pelanggan.nama,
            latestPengukuran = uiState.latestPengukuran,
            isSaving = uiState.isSaving,
            errorMessage = uiState.formError,
            onDismiss = {
                viewModel.clearFormError()
                showAddPengukuranSheet = false
            },
            onSave = { tanggalUkur, dataUkuran, catatan ->
                viewModel.savePengukuran(
                    pelangganId = pelanggan.id,
                    tanggalUkur = tanggalUkur,
                    dataUkuran = dataUkuran,
                    catatan = catatan,
                    onSuccess = {
                        scope.launch { addPengukuranSheetState.hide() }
                        showAddPengukuranSheet = false
                    }
                )
            }
        )
    }

    // Confirmation Dialog Delete Customer
    if (showDeletePelangganDialog) {
        AlertDialog(
            onDismissRequest = { showDeletePelangganDialog = false },
            title = {
                Text(
                    text = "Hapus Pelanggan?",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Zinc900
                )
            },
            text = {
                Text(
                    text = "Data pelanggan '${pelanggan.nama}' beserta seluruh catatan ukurannya akan dihapus permanen. Tindakan ini tidak dapat dibatalkan.",
                    fontSize = 13.sp,
                    color = Zinc700
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deletePelanggan(pelanggan.id) {
                            showDeletePelangganDialog = false
                            onBackClick()
                        }
                    }
                ) {
                    Text(text = "Hapus", color = Rose600, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeletePelangganDialog = false }) {
                    Text(text = "Batal", color = Zinc500)
                }
            },
            containerColor = PureWhite
        )
    }

    // Confirmation Dialog Delete Measurement
    pengukuranToDelete?.let { target ->
        AlertDialog(
            onDismissRequest = { pengukuranToDelete = null },
            title = {
                Text(
                    text = "Hapus Pengukuran?",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Zinc900
                )
            },
            text = {
                Text(
                    text = "Catatan pengukuran tanggal ${Formatters.formatDate(target.tanggalUkur)} akan dihapus.",
                    fontSize = 13.sp,
                    color = Zinc700
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deletePengukuran(target.id, pelanggan.id) {
                            pengukuranToDelete = null
                        }
                    }
                ) {
                    Text(text = "Hapus", color = Rose600, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { pengukuranToDelete = null }) {
                    Text(text = "Batal", color = Zinc500)
                }
            },
            containerColor = PureWhite
        )
    }
}

@Composable
private fun MeasurementCard(
    pengukuran: PengukuranDto,
    onDeleteClick: () -> Unit
) {
    val readableLabels = mapOf(
        "lebar_bahu" to "Lebar Bahu",
        "lingkar_dada" to "Lingkar Dada",
        "panjang_baju" to "Panjang Baju",
        "lingkar_leher" to "Lingkar Leher",
        "panjang_lengan" to "Panjang Lengan",
        "lingkar_kerung_lengan" to "Kerung Lengan",
        "lingkar_pinggang" to "Lingkar Pinggang",
        "lingkar_pinggul" to "Lingkar Pinggul",
        "panjang_celana" to "Panjang Celana",
        "lingkar_paha" to "Lingkar Paha"
    )

    TailorCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Tanggal Ukur: ${Formatters.formatDate(pengukuran.tanggalUkur)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Zinc900
                )
            }

            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Hapus Ukuran",
                    tint = Zinc400,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Grid of Measurements
        val entries = (pengukuran.dataUkuran ?: emptyMap()).entries.toList()
        entries.chunked(2).forEach { rowEntries ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowEntries.forEach { (key, value) ->
                    val label = readableLabels[key] ?: key.replace("_", " ").replaceFirstChar { it.uppercase() }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(Zinc100, shape = RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                color = Zinc500
                            )
                            Text(
                                text = "$value cm",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Zinc900
                            )
                        }
                    }
                }
                if (rowEntries.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        if (!pengukuran.catatan.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PureWhite, shape = RoundedCornerShape(8.dp))
                    .border(1.dp, Zinc200, shape = RoundedCornerShape(8.dp))
                    .padding(10.dp)
            ) {
                Text(
                    text = "Catatan: ${pengukuran.catatan}",
                    fontSize = 12.sp,
                    color = Zinc700
                )
            }
        }
    }
}

@Composable
private fun CustomerOrderCard(
    order: PelangganPesananDto
) {
    TailorCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = order.nomorPesanan,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Zinc900
            )

            StatusBadge(status = order.status)
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Deadline: ${Formatters.formatDate(order.deadline)}",
                fontSize = 12.sp,
                color = Zinc500
            )

            val itemCount = order.itemPesananCount ?: 0
            Text(
                text = "$itemCount Item",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Zinc700
            )
        }

        if (!order.catatan.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = order.catatan,
                fontSize = 12.sp,
                color = Zinc500,
                maxLines = 2
            )
        }
    }
}
