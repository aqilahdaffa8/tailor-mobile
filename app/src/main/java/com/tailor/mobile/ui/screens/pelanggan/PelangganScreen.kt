package com.tailor.mobile.ui.screens.pelanggan

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Search
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
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tailor.mobile.data.source.remote.response.PelangganDto
import com.tailor.mobile.ui.components.TailorButton
import com.tailor.mobile.ui.components.TailorButtonVariant
import com.tailor.mobile.ui.components.TailorCard
import com.tailor.mobile.ui.components.TailorTextField
import com.tailor.mobile.ui.screens.pelanggan.components.PelangganFormSheet
import com.tailor.mobile.ui.theme.PureWhite
import com.tailor.mobile.ui.theme.Zinc100
import com.tailor.mobile.ui.theme.Zinc200
import com.tailor.mobile.ui.theme.Zinc400
import com.tailor.mobile.ui.theme.Zinc50
import com.tailor.mobile.ui.theme.Zinc500
import com.tailor.mobile.ui.theme.Zinc700
import com.tailor.mobile.ui.theme.Zinc900
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PelangganScreen(
    viewModel: PelangganViewModel = viewModel(),
    initialShowAddDialog: Boolean = false,
    onCustomerCreatedForOrder: ((PelangganDto) -> Unit)? = null
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var showAddSheet by remember { mutableStateOf(initialShowAddDialog) }
    val addSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Show snackbar on success events
    LaunchedEffect(uiState.successSnackbar) {
        uiState.successSnackbar?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearSnackbar()
        }
    }

    // If detail is active, render detail screen and intercept back button
    if (uiState.selectedPelanggan != null) {
        BackHandler {
            viewModel.clearSelectedPelanggan()
        }
        PelangganDetailScreen(
            viewModel = viewModel,
            pelanggan = uiState.selectedPelanggan!!,
            onBackClick = { viewModel.clearSelectedPelanggan() }
        )
        return
    }

    Scaffold(
        containerColor = Zinc50,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Pelanggan",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Zinc900
                        )
                        Text(
                            text = "${uiState.pelangganList.size} Pelanggan Terdaftar",
                            fontSize = 12.sp,
                            color = Zinc500
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(
                            imageVector = Icons.Outlined.Refresh,
                            contentDescription = "Muat Ulang",
                            tint = Zinc900
                        )
                    }

                    TailorButton(
                        text = "+ Pelanggan",
                        onClick = { showAddSheet = true },
                        variant = TailorButtonVariant.Primary,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PureWhite)
            )
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = { viewModel.refresh() },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                // Search Bar
                TailorTextField(
                    value = uiState.searchQuery,
                    onValueChange = { viewModel.onSearchQueryChanged(it) },
                    placeholder = "Cari nama atau nomor HP pelanggan...",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Cari",
                            tint = Zinc500
                        )
                    },
                    trailingIcon = if (uiState.searchQuery.isNotBlank()) {
                        {
                            IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                                Icon(
                                    imageVector = Icons.Outlined.Clear,
                                    contentDescription = "Hapus pencarian",
                                    tint = Zinc500
                                )
                            }
                        }
                    } else null,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Customer List
                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 48.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        CircularProgressIndicator(color = Zinc900, modifier = Modifier.size(32.dp))
                    }
                } else if (!uiState.errorMessage.isNullOrBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Gagal memuat data",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Zinc900
                            )
                            Text(
                                text = uiState.errorMessage ?: "",
                                fontSize = 13.sp,
                                color = Zinc500,
                                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            TailorButton(
                                text = "Coba Lagi",
                                onClick = { viewModel.refresh() },
                                variant = TailorButtonVariant.Secondary
                            )
                        }
                    }
                } else if (uiState.pelangganList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Outlined.Person,
                                contentDescription = null,
                                tint = Zinc400,
                                modifier = Modifier.size(52.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = if (uiState.searchQuery.isNotBlank()) "Pelanggan tidak ditemukan" else "Belum Ada Pelanggan",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Zinc900
                            )
                            Text(
                                text = if (uiState.searchQuery.isNotBlank()) "Coba kata kunci pencarian yang lain." else "Tambahkan pelanggan pertama Anda untuk mulai mencatat pesanan.",
                                fontSize = 13.sp,
                                color = Zinc500,
                                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            if (uiState.searchQuery.isBlank()) {
                                TailorButton(
                                    text = "+ Tambah Pelanggan Pertama",
                                    onClick = { showAddSheet = true },
                                    variant = TailorButtonVariant.Primary
                                )
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(uiState.pelangganList, key = { it.id }) { customer ->
                            CustomerListItemCard(
                                customer = customer,
                                onClick = { viewModel.selectPelanggan(customer.id) }
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            }
        }
    }

    // Modal Sheet Tambah Pelanggan
    if (showAddSheet) {
        PelangganFormSheet(
            sheetState = addSheetState,
            initialData = null,
            isSaving = uiState.isSaving,
            errorMessage = uiState.formError,
            onDismiss = {
                viewModel.clearFormError()
                showAddSheet = false
            },
            onSave = { nama, noHp, alamat, catatan ->
                viewModel.savePelanggan(
                    id = null,
                    nama = nama,
                    noHp = noHp,
                    alamat = alamat,
                    catatan = catatan,
                    onSuccess = {
                        scope.launch { addSheetState.hide() }
                        showAddSheet = false
                    }
                )
            }
        )
    }
}

@Composable
private fun CustomerListItemCard(
    customer: PelangganDto,
    onClick: () -> Unit
) {
    TailorCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Initial circle
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Zinc900),
                contentAlignment = Alignment.Center
            ) {
                val initial = customer.nama.trim().firstOrNull()?.uppercase() ?: "P"
                Text(
                    text = initial,
                    color = PureWhite,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = customer.nama,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Zinc900
                    )

                    // Order count badge
                    val orderCount = customer.pesananCount ?: 0
                    Box(
                        modifier = Modifier
                            .background(Zinc100, shape = RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "$orderCount Pesanan",
                            fontSize = 12.sp,
                            color = Zinc700,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))

                if (!customer.noHp.isNullOrBlank()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Phone,
                            contentDescription = null,
                            tint = Zinc500,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = customer.noHp,
                            fontSize = 12.sp,
                            color = Zinc500
                        )
                    }
                }

                if (!customer.alamat.isNullOrBlank()) {
                    Text(
                        text = customer.alamat,
                        fontSize = 12.sp,
                        color = Zinc500,
                        maxLines = 1,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
                contentDescription = null,
                tint = Zinc400,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}
