package com.tailor.mobile.ui.screens.pesanan

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.ContentCut
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tailor.mobile.data.source.remote.response.PesananDto
import com.tailor.mobile.ui.components.StatusBadge
import com.tailor.mobile.ui.components.TailorButton
import com.tailor.mobile.ui.components.TailorButtonVariant
import com.tailor.mobile.ui.components.TailorCard
import com.tailor.mobile.ui.theme.ControlShape
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PesananScreen(
    onNavigateToNewOrder: () -> Unit = {},
    viewModel: PesananViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.successSnackbar) {
        uiState.successSnackbar?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbar()
        }
    }

    // Detail Screen Navigation Handling
    if (uiState.selectedPesanan != null) {
        BackHandler { viewModel.clearSelectedPesanan() }
        PesananDetailScreen(
            viewModel = viewModel,
            onBack = { viewModel.clearSelectedPesanan() }
        )
        return
    }

    Scaffold(
        containerColor = Zinc50,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Pesanan Jahit",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Zinc900
                    )
                },
                actions = {
                    IconButton(onClick = onNavigateToNewOrder) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = "Tambah Pesanan",
                            tint = Zinc900
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PureWhite
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToNewOrder,
                containerColor = Zinc900,
                contentColor = PureWhite,
                shape = PillShape
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(imageVector = Icons.Outlined.Add, contentDescription = null)
                    Text(
                        text = "Pesanan Baru",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = { viewModel.loadPesananList(isRefresh = true) },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Search Field
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PureWhite)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = { viewModel.onSearchQueryChanged(it) },
                        placeholder = {
                            Text(
                                text = "Cari no. pesanan atau nama pelanggan...",
                                fontSize = 13.sp,
                                color = Zinc400
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = null,
                                tint = Zinc400,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        trailingIcon = {
                            if (uiState.searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                                    Icon(
                                        imageVector = Icons.Outlined.Clear,
                                        contentDescription = "Hapus",
                                        tint = Zinc400,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        shape = ControlShape,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Zinc900,
                            unfocusedBorderColor = Zinc200,
                            focusedContainerColor = PureWhite,
                            unfocusedContainerColor = PureWhite,
                            cursorColor = Zinc900
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    )
                }

                // Filter Status Chips (All, Aktif, Selesai, Diambil, Dibatalkan)
                val statusList = listOf(
                    null to "Semua",
                    "aktif" to "Aktif",
                    "selesai" to "Selesai",
                    "diambil" to "Diambil",
                    "dibatalkan" to "Dibatalkan"
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PureWhite)
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    statusList.forEach { (statusVal, label) ->
                        val isSelected = uiState.selectedStatus == statusVal && uiState.selectedDeadlineFilter == null
                        Box(
                            modifier = Modifier
                                .clip(PillShape)
                                .background(if (isSelected) Zinc900 else Zinc100)
                                .clickable {
                                    viewModel.selectDeadlineFilter(null)
                                    viewModel.selectStatus(statusVal)
                                }
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                                color = if (isSelected) PureWhite else Zinc600
                            )
                        }
                    }

                    // Quick deadline filter: Terlambat
                    val isTerlambat = uiState.selectedDeadlineFilter == "terlambat"
                    Box(
                        modifier = Modifier
                            .clip(PillShape)
                            .background(if (isTerlambat) Rose600 else Zinc100)
                            .clickable {
                                if (isTerlambat) {
                                    viewModel.selectDeadlineFilter(null)
                                } else {
                                    viewModel.selectDeadlineFilter("terlambat")
                                }
                            }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Terlambat",
                            fontSize = 12.sp,
                            fontWeight = if (isTerlambat) FontWeight.SemiBold else FontWeight.Medium,
                            color = if (isTerlambat) PureWhite else Zinc600
                        )
                    }

                    // Quick deadline filter: Mendekati
                    val isMendekati = uiState.selectedDeadlineFilter == "mendekati"
                    Box(
                        modifier = Modifier
                            .clip(PillShape)
                            .background(if (isMendekati) Zinc900 else Zinc100)
                            .clickable {
                                if (isMendekati) {
                                    viewModel.selectDeadlineFilter(null)
                                } else {
                                    viewModel.selectDeadlineFilter("mendekati")
                                }
                            }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Mendekati Deadline",
                            fontSize = 12.sp,
                            fontWeight = if (isMendekati) FontWeight.SemiBold else FontWeight.Medium,
                            color = if (isMendekati) PureWhite else Zinc600
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // List content
                if (uiState.isLoading && !uiState.isRefreshing) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Zinc900, strokeWidth = 2.dp)
                    }
                } else if (uiState.pesananList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Zinc100),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.ReceiptLong,
                                    contentDescription = null,
                                    tint = Zinc500,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Text(
                                text = "Tidak Ada Pesanan",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Zinc900
                            )
                            Text(
                                text = if (uiState.searchQuery.isNotBlank())
                                    "Tidak ada pesanan cocok dengan '${uiState.searchQuery}'"
                                else
                                    "Belum ada pesanan dengan kriteria filter yang dipilih.",
                                fontSize = 13.sp,
                                color = Zinc500,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 88.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(uiState.pesananList, key = { it.id }) { pesanan ->
                            PesananCard(
                                pesanan = pesanan,
                                onClick = { viewModel.selectPesanan(pesanan) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PesananCard(
    pesanan: PesananDto,
    onClick: () -> Unit
) {
    TailorCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // Header Row: Nomor Pesanan & Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = pesanan.nomorPesanan,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Zinc900
                )
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    StatusBadge(status = pesanan.status)
                    StatusBadge(status = pesanan.getEffectiveStatusBayar())
                }
            }

            HorizontalDivider(color = Zinc100, thickness = 1.dp)

            // Pelanggan Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = Zinc500,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = pesanan.pelanggan?.nama ?: "Pelanggan",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Zinc900
                )
            }

            // Deadline & Biaya Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CalendarToday,
                        contentDescription = null,
                        tint = Zinc500,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Deadline: ${Formatters.formatTanggal(pesanan.deadline)}",
                        fontSize = 12.sp,
                        color = Zinc600
                    )
                }

                Text(
                    text = Formatters.formatRupiah(pesanan.getEffectiveTotal()),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Zinc900
                )
            }
        }
    }
}
