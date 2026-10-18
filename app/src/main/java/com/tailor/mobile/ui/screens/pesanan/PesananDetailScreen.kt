package com.tailor.mobile.ui.screens.pesanan

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.SquareFoot
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import com.tailor.mobile.data.source.remote.response.ItemPesananDto
import com.tailor.mobile.data.source.remote.response.PesananDto
import com.tailor.mobile.data.source.remote.response.ProgressItemDto
import com.tailor.mobile.ui.components.StatusBadge
import com.tailor.mobile.ui.components.TailorButton
import com.tailor.mobile.ui.components.TailorButtonVariant
import com.tailor.mobile.ui.components.TailorCard
import com.tailor.mobile.ui.theme.DialogShape
import com.tailor.mobile.ui.theme.EmeraldText
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
import com.tailor.mobile.util.Formatters

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PesananDetailScreen(
    viewModel: PesananViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val pesanan = uiState.selectedPesanan ?: return
    val snackbarHostState = remember { SnackbarHostState() }

    var showCancelDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.successSnackbar) {
        uiState.successSnackbar?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbar()
        }
    }

    Scaffold(
        containerColor = Zinc50,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = pesanan.nomorPesanan,
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
                    if (pesanan.status != "dibatalkan" && pesanan.status != "diambil") {
                        IconButton(onClick = { showCancelDialog = true }) {
                            Icon(
                                imageVector = Icons.Outlined.Block,
                                contentDescription = "Batalkan Pesanan",
                                tint = Rose600
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PureWhite
                )
            )
        }
    ) { innerPadding ->
        if (uiState.isLoadingDetail && pesanan.itemPesanan == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Zinc900, strokeWidth = 2.dp)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header Status Card
                TailorCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                StatusBadge(status = pesanan.status)
                                StatusBadge(status = pesanan.getEffectiveStatusBayar())
                            }

                            Text(
                                text = "${pesanan.itemPesanan?.size ?: pesanan.itemPesananCount ?: 1} Item",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Zinc500
                            )
                        }

                        HorizontalDivider(color = Zinc200, thickness = 0.5.dp)

                        // Customer info
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Zinc100),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Person,
                                    contentDescription = null,
                                    tint = Zinc900,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = pesanan.pelanggan?.nama ?: "Pelanggan",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Zinc900
                                )
                                if (!pesanan.pelanggan?.noHp.isNullOrBlank()) {
                                    Text(
                                        text = pesanan.pelanggan?.noHp ?: "",
                                        fontSize = 12.sp,
                                        color = Zinc500
                                    )
                                }
                            }
                        }

                        // Dates info
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Tanggal Pesan",
                                    fontSize = 11.sp,
                                    color = Zinc500,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = Formatters.formatTanggal(pesanan.tanggalPesan ?: pesanan.createdAt),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Zinc900
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Batas Deadline",
                                    fontSize = 11.sp,
                                    color = Zinc500,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = Formatters.formatTanggal(pesanan.deadline),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (pesanan.status == "aktif") Rose600 else Zinc900
                                )
                            }
                        }

                        if (!pesanan.catatan.isNullOrBlank()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Zinc50)
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = "Catatan: ${pesanan.catatan}",
                                    fontSize = 12.sp,
                                    color = Zinc600,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }

                // Financial Summary Card
                TailorCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Rincian Tagihan & Pembayaran",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Zinc900
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Total Biaya Jahitan", fontSize = 13.sp, color = Zinc600)
                            Text(
                                text = Formatters.formatRupiah(pesanan.getEffectiveTotal()),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Zinc900
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Total Telah Dibayar", fontSize = 13.sp, color = Zinc600)
                            Text(
                                text = Formatters.formatRupiah(pesanan.totalDibayar ?: 0.0),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Zinc900
                            )
                        }

                        HorizontalDivider(color = Zinc200, thickness = 0.5.dp)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Sisa Tagihan",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Zinc900
                            )
                            val sisa = pesanan.getEffectiveSisa()
                            Text(
                                text = Formatters.formatRupiah(sisa),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (sisa <= 0.0) EmeraldText else Rose600
                            )
                        }
                    }
                }

                // Section: Items List
                val items = pesanan.itemPesanan ?: emptyList()
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Item Jahitan (${items.size})",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Zinc900
                    )

                    if (items.isEmpty()) {
                        TailorCard(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Tidak ada detail item jahitan.",
                                fontSize = 13.sp,
                                color = Zinc500
                            )
                        }
                    } else {
                        items.forEachIndexed { index, item ->
                            ItemPesananCard(item = item, itemNumber = index + 1)
                        }
                    }
                }

                // Section: Payment History
                val pembayaranList = pesanan.pembayaran ?: emptyList()
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Riwayat Pembayaran (${pembayaranList.size})",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Zinc900
                    )

                    if (pembayaranList.isEmpty()) {
                        TailorCard(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Belum ada transaksi pembayaran untuk pesanan ini.",
                                fontSize = 13.sp,
                                color = Zinc500,
                                modifier = Modifier.padding(vertical = 6.dp)
                            )
                        }
                    } else {
                        pembayaranList.forEach { bayar ->
                            TailorCard(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                        Text(
                                            text = "Metode: ${bayar.metode.replaceFirstChar { it.uppercase() }}",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Zinc900
                                        )
                                        Text(
                                            text = Formatters.formatTanggal(bayar.tanggalBayar),
                                            fontSize = 12.sp,
                                            color = Zinc500
                                        )
                                        if (!bayar.catatan.isNullOrBlank()) {
                                            Text(
                                                text = bayar.catatan,
                                                fontSize = 11.sp,
                                                color = Zinc500
                                            )
                                        }
                                    }

                                    Text(
                                        text = Formatters.formatRupiah(bayar.jumlah),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldText
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Cancel Order Dialog
    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = {
                Text(
                    text = "Batalkan Pesanan?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = Zinc900
                )
            },
            text = {
                Text(
                    text = "Pesanan nomor '${pesanan.nomorPesanan}' akan diubah statusnya menjadi Dibatalkan. Riwayat pemakaian bahan tidak dikembalikan otomatis.",
                    fontSize = 14.sp,
                    color = Zinc600
                )
            },
            confirmButton = {
                TailorButton(
                    text = if (uiState.isActionLoading) "Membatalkan..." else "Ya, Batalkan",
                    onClick = {
                        viewModel.batalkanPesanan(pesanan.id) {
                            showCancelDialog = false
                        }
                    },
                    variant = TailorButtonVariant.Danger,
                    enabled = !uiState.isActionLoading
                )
            },
            dismissButton = {
                TailorButton(
                    text = "Kembali",
                    onClick = { showCancelDialog = false },
                    variant = TailorButtonVariant.Secondary
                )
            },
            containerColor = PureWhite,
            shape = DialogShape
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ItemPesananCard(item: ItemPesananDto, itemNumber: Int) {
    TailorCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${itemNumber}. ${item.jenisJasa?.namaJasa ?: "Jasa Jahit"}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Zinc900
                )
                Text(
                    text = Formatters.formatRupiah(item.harga),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Zinc900
                )
            }

            // Status & Spec Badges
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                StatusBadge(status = item.statusItem)

                if (item.polaDetail != null) {
                    Box(
                        modifier = Modifier
                            .clip(PillShape)
                            .background(Zinc100)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "Pola Size ${item.polaDetail.size}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Zinc900
                        )
                    }
                }

                if (item.pengukuran != null) {
                    Box(
                        modifier = Modifier
                            .clip(PillShape)
                            .background(Zinc100)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "Ukuran Tubuh Terlampir",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Zinc600
                        )
                    }
                }
            }

            if (!item.detailPekerjaan.isNullOrBlank()) {
                Text(
                    text = item.detailPekerjaan,
                    fontSize = 13.sp,
                    color = Zinc600,
                    lineHeight = 18.sp
                )
            }

            // Progress Stages Stepper List
            val stages = item.progress ?: emptyList()
            if (stages.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Zinc50)
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Tahapan Pengerjaan",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Zinc500
                    )

                    stages.forEach { stage ->
                        ProgressStepRow(stage = stage)
                    }
                }
            }

            // Fabric usage if any
            val bahanList = item.pemakaianBahan ?: emptyList()
            if (bahanList.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Pemakaian Bahan:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Zinc500
                    )
                    bahanList.forEach { b ->
                        Text(
                            text = "• ${b.bahan?.namaBahan ?: "Kain"}: ${b.jumlah} ${b.bahan?.satuan ?: "meter"}",
                            fontSize = 12.sp,
                            color = Zinc900
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProgressStepRow(stage: ProgressItemDto) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(if (stage.isCompleted) Zinc900 else Zinc200),
            contentAlignment = Alignment.Center
        ) {
            if (stage.isCompleted) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null,
                    tint = PureWhite,
                    modifier = Modifier.size(12.dp)
                )
            } else {
                Text(
                    text = "${stage.urutan}",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Zinc500
                )
            }
        }

        Text(
            text = stage.namaTahapan,
            fontSize = 12.sp,
            fontWeight = if (stage.isCompleted) FontWeight.SemiBold else FontWeight.Normal,
            color = if (stage.isCompleted) Zinc900 else Zinc500,
            modifier = Modifier.weight(1f)
        )

        if (stage.isCompleted && !stage.tanggalSelesai.isNullOrBlank()) {
            Text(
                text = Formatters.formatTanggal(stage.tanggalSelesai),
                fontSize = 10.sp,
                color = Zinc400
            )
        }
    }
}
