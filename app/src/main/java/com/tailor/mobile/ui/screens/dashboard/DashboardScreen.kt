package com.tailor.mobile.ui.screens.dashboard

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import com.tailor.mobile.ui.components.BadgeVariant
import com.tailor.mobile.ui.components.StatusBadge
import com.tailor.mobile.ui.components.TailorCard
import com.tailor.mobile.ui.screens.dashboard.components.DashboardPesananItem
import com.tailor.mobile.ui.screens.dashboard.components.MetricCard
import com.tailor.mobile.ui.screens.dashboard.components.QuickActionButton
import com.tailor.mobile.ui.screens.dashboard.components.ReminderCardItem
import com.tailor.mobile.ui.theme.AmberBg
import com.tailor.mobile.ui.theme.AmberBorder
import com.tailor.mobile.ui.theme.AmberText
import com.tailor.mobile.ui.theme.ControlShape
import com.tailor.mobile.ui.theme.PillShape
import com.tailor.mobile.ui.theme.PureWhite
import com.tailor.mobile.ui.theme.RoseBg
import com.tailor.mobile.ui.theme.RoseText
import com.tailor.mobile.ui.theme.Zinc100
import com.tailor.mobile.ui.theme.Zinc200
import com.tailor.mobile.ui.theme.Zinc50
import com.tailor.mobile.ui.theme.Zinc500
import com.tailor.mobile.ui.theme.Zinc600
import com.tailor.mobile.ui.theme.Zinc700
import com.tailor.mobile.ui.theme.Zinc800
import com.tailor.mobile.ui.theme.Zinc900
import com.tailor.mobile.util.Formatters

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onNavigateToNewOrder: () -> Unit,
    onNavigateToNewCustomer: () -> Unit,
    onOrderClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    var showReminderSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        containerColor = Zinc50,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "TAILOR SYSTEM",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Zinc900,
                            letterSpacing = (-0.3).sp
                        )
                        Text(
                            text = state.summary?.periode?.label ?: "Ringkasan Usaha",
                            fontSize = 12.sp,
                            color = Zinc500
                        )
                    }
                },
                actions = {
                    // Refresh Button
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(
                            imageVector = Icons.Outlined.Refresh,
                            contentDescription = "Muat ulang",
                            tint = Zinc700
                        )
                    }

                    // Reminder Bell with Badge
                    IconButton(onClick = { showReminderSheet = true }) {
                        Box(contentAlignment = Alignment.TopEnd) {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = "Pengingat",
                                tint = Zinc700
                            )
                            if (state.reminders.isNotEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(PillShape)
                                        .background(Color(0xFFE11D48))
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PureWhite
                )
            )
        }
    ) { innerPadding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Zinc900, strokeWidth = 2.5.dp)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(2.dp))
                }

                // Error Banner if present
                if (!state.errorMessage.isNullOrBlank()) {
                    item {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = ControlShape,
                            color = RoseBg,
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x33E11D48))
                        ) {
                            Text(
                                text = state.errorMessage ?: "",
                                color = RoseText,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }

                // Low Stock Warning Banner if any
                if (state.stokMenipis.isNotEmpty()) {
                    item {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = ControlShape,
                            color = AmberBg,
                            border = androidx.compose.foundation.BorderStroke(1.dp, AmberBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.WarningAmber,
                                    contentDescription = null,
                                    tint = AmberText,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "${state.stokMenipis.size} Bahan Stok Menipis",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = AmberText
                                    )
                                    Text(
                                        text = state.stokMenipis.joinToString(", ") { "${it.namaBahan} (${it.stok} ${it.satuan})" },
                                        fontSize = 12.sp,
                                        color = Zinc700,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }

                // 2x2 Metric Grid
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            val aktif = state.summary?.pesananAktif ?: 0
                            val mendekati = state.summary?.pesananMendekatiDeadline ?: 0
                            val terlambat = state.summary?.pesananTerlambat ?: 0

                            MetricCard(
                                title = "Pesanan Aktif",
                                value = aktif.toString(),
                                subtitle = "$mendekati mendekati • $terlambat lewat",
                                badgeText = if (terlambat > 0) "Perhatian" else null,
                                badgeVariant = BadgeVariant.ERROR,
                                modifier = Modifier.weight(1f)
                            )

                            val sisaTagihan = state.summary?.pembayaranBelumLunas?.totalSisa ?: 0.0
                            val pesananBelumLunas = state.summary?.pembayaranBelumLunas?.jumlahPesanan ?: 0

                            MetricCard(
                                title = "Belum Lunas",
                                value = Formatters.formatRupiah(sisaTagihan),
                                subtitle = "$pesananBelumLunas pesanan",
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            val pendapatan = state.summary?.keuanganBulanIni?.pendapatan ?: 0.0
                            val pengeluaran = state.summary?.keuanganBulanIni?.pengeluaran ?: 0.0

                            MetricCard(
                                title = "Pendapatan Bulan Ini",
                                value = Formatters.formatRupiah(pendapatan),
                                subtitle = "Pengeluaran: ${Formatters.formatRupiah(pengeluaran)}",
                                modifier = Modifier.weight(1f)
                            )

                            val stokCount = state.summary?.stokMenipis ?: 0
                            MetricCard(
                                title = "Stok Menipis",
                                value = "$stokCount bahan",
                                subtitle = if (stokCount == 0) "Semua aman" else "Perlu restock",
                                badgeText = if (stokCount > 0) "Restock" else null,
                                badgeVariant = BadgeVariant.WARNING,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Quick Action Shortcuts
                item {
                    Text(
                        text = "Aksi Cepat",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Zinc900
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        QuickActionButton(
                            title = "+ Pesanan Baru",
                            icon = Icons.Outlined.AddCircleOutline,
                            onClick = onNavigateToNewOrder,
                            modifier = Modifier.weight(1f)
                        )
                        QuickActionButton(
                            title = "+ Pelanggan",
                            icon = Icons.Outlined.PersonAdd,
                            onClick = onNavigateToNewCustomer,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Section Tabs: Mendekati Deadline vs Terlambat
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val countMendekati = state.pesananMendekati.size
                        val countTerlambat = state.pesananTerlambat.size

                        DashboardTabButton(
                            title = "Mendekati Deadline ($countMendekati)",
                            isSelected = state.activeTab == DashboardTab.MENDEKATI,
                            onClick = { viewModel.selectTab(DashboardTab.MENDEKATI) },
                            modifier = Modifier.weight(1f)
                        )

                        DashboardTabButton(
                            title = "Terlambat ($countTerlambat)",
                            isSelected = state.activeTab == DashboardTab.TERLAMBAT,
                            onClick = { viewModel.selectTab(DashboardTab.TERLAMBAT) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Order List matching Active Tab
                val currentOrders = if (state.activeTab == DashboardTab.MENDEKATI) {
                    state.pesananMendekati
                } else {
                    state.pesananTerlambat
                }

                if (currentOrders.isEmpty()) {
                    item {
                        TailorCard(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = if (state.activeTab == DashboardTab.MENDEKATI) {
                                    "Tidak ada pesanan mendekati deadline dalam 3 hari ke depan."
                                } else {
                                    "Tidak ada pesanan yang terlambat. Kerja bagus!"
                                },
                                fontSize = 13.sp,
                                color = Zinc500,
                                modifier = Modifier.padding(vertical = 12.dp)
                            )
                        }
                    }
                } else {
                    items(currentOrders, key = { it.id }) { pesanan ->
                        DashboardPesananItem(
                            pesanan = pesanan,
                            onClick = { onOrderClick(pesanan.id) }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }

    // Reminder Bottom Sheet
    if (showReminderSheet) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = { showReminderSheet = false },
            sheetState = sheetState,
            containerColor = PureWhite
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Pengingat Penting",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Zinc900
                    )
                    StatusBadge(
                        text = "${state.reminders.size} Aktif",
                        variant = if (state.reminders.isNotEmpty()) BadgeVariant.WARNING else BadgeVariant.SUCCESS
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (state.reminders.isEmpty()) {
                    Text(
                        text = "Semua pengingat sudah dibaca.",
                        fontSize = 13.sp,
                        color = Zinc500,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(state.reminders, key = { it.id }) { reminder ->
                            ReminderCardItem(
                                reminder = reminder,
                                onDismiss = { viewModel.dismissReminder(reminder.id) }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun DashboardTabButton(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(ControlShape)
            .clickable(onClick = onClick),
        shape = ControlShape,
        color = if (isSelected) Zinc900 else PureWhite,
        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, Zinc200)
    ) {
        Box(
            modifier = Modifier.padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                color = if (isSelected) PureWhite else Zinc600
            )
        }
    }
}
