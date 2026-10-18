package com.tailor.mobile.ui.screens.pola

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
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tailor.mobile.data.source.remote.response.JenisPakaianDto
import com.tailor.mobile.ui.components.TailorButton
import com.tailor.mobile.ui.components.TailorButtonVariant
import com.tailor.mobile.ui.components.TailorCard
import com.tailor.mobile.ui.screens.pola.components.JenisPakaianFormSheet
import com.tailor.mobile.ui.theme.ControlShape
import com.tailor.mobile.ui.theme.PillShape
import com.tailor.mobile.ui.theme.PureWhite
import com.tailor.mobile.ui.theme.Zinc100
import com.tailor.mobile.ui.theme.Zinc200
import com.tailor.mobile.ui.theme.Zinc400
import com.tailor.mobile.ui.theme.Zinc50
import com.tailor.mobile.ui.theme.Zinc500
import com.tailor.mobile.ui.theme.Zinc600
import com.tailor.mobile.ui.theme.Zinc900

data class JenisPakaianWithCategory(
    val jenis: JenisPakaianDto,
    val namaKategori: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PolaScreen(
    onBack: () -> Unit,
    viewModel: PolaViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showAddJenisSheet by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.successSnackbar) {
        uiState.successSnackbar?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbar()
        }
    }

    // Detail Screen Navigation Handling
    if (uiState.selectedJenisPakaian != null) {
        PolaDetailScreen(
            viewModel = viewModel,
            onBack = {
                viewModel.clearSelectedJenisPakaian()
            }
        )
        return
    }

    // Flatten all JenisPakaian with their category name
    val allItems = remember(uiState.kategoriList, uiState.selectedKategoriId, uiState.searchQuery) {
        val list = mutableListOf<JenisPakaianWithCategory>()
        val categories = if (uiState.selectedKategoriId != null) {
            uiState.kategoriList.filter { it.id == uiState.selectedKategoriId }
        } else {
            uiState.kategoriList
        }

        for (kat in categories) {
            kat.jenisPakaian?.forEach { j ->
                val matchesSearch = uiState.searchQuery.isBlank() ||
                        j.namaJenis.contains(uiState.searchQuery, ignoreCase = true) ||
                        kat.namaKategori.contains(uiState.searchQuery, ignoreCase = true)
                if (matchesSearch) {
                    list.add(JenisPakaianWithCategory(j, kat.namaKategori))
                }
            }
        }
        list
    }

    Scaffold(
        containerColor = Zinc50,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Library Pola",
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
                    IconButton(onClick = { showAddJenisSheet = true }) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = "Tambah Jenis Pakaian",
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
                onClick = { showAddJenisSheet = true },
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
                        text = "Tambah Pola",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = { viewModel.loadKategoriList(isRefresh = true) },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
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
                                text = "Cari jenis pakaian atau kategori...",
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

                // Horizontal Category Filter Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PureWhite)
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // "Semua" Chip
                    val isAllSelected = uiState.selectedKategoriId == null
                    Box(
                        modifier = Modifier
                            .clip(PillShape)
                            .background(if (isAllSelected) Zinc900 else Zinc100)
                            .clickable { viewModel.selectKategori(null) }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Semua",
                            fontSize = 12.sp,
                            fontWeight = if (isAllSelected) FontWeight.SemiBold else FontWeight.Medium,
                            color = if (isAllSelected) PureWhite else Zinc600
                        )
                    }

                    // Individual Categories
                    uiState.kategoriList.forEach { kategori ->
                        val isSelected = uiState.selectedKategoriId == kategori.id
                        Box(
                            modifier = Modifier
                                .clip(PillShape)
                                .background(if (isSelected) Zinc900 else Zinc100)
                                .clickable { viewModel.selectKategori(kategori.id) }
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = kategori.namaKategori,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                                color = if (isSelected) PureWhite else Zinc600
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Content List
                if (uiState.isLoading && !uiState.isRefreshing) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Zinc900, strokeWidth = 2.dp)
                    }
                } else if (allItems.isEmpty()) {
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
                                    imageVector = Icons.Outlined.Straighten,
                                    contentDescription = null,
                                    tint = Zinc500,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Text(
                                text = "Tidak Ada Pola Ditemukan",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Zinc900
                            )
                            Text(
                                text = if (uiState.searchQuery.isNotBlank())
                                    "Tidak ditemukan model pakaian dengan kata kunci '${uiState.searchQuery}'"
                                else
                                    "Belum ada model pakaian dalam kategori ini. Tambahkan model pertama Anda.",
                                fontSize = 13.sp,
                                color = Zinc500,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            TailorButton(
                                text = "+ Tambah Model Baru",
                                onClick = { showAddJenisSheet = true },
                                variant = TailorButtonVariant.Primary
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 88.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(allItems, key = { it.jenis.id }) { item ->
                            JenisPakaianCard(
                                item = item,
                                onClick = { viewModel.selectJenisPakaian(item.jenis) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Bottom Sheet: Tambah Jenis Pakaian
    if (showAddJenisSheet) {
        JenisPakaianFormSheet(
            kategoriList = uiState.kategoriList,
            initialKategoriId = uiState.selectedKategoriId,
            isSaving = uiState.isSaving,
            errorMessage = uiState.formError,
            onDismiss = {
                showAddJenisSheet = false
                viewModel.clearFormError()
            },
            onSubmit = { kategoriId, nama, deskripsi ->
                viewModel.createJenisPakaian(
                    kategoriId = kategoriId,
                    namaJenis = nama,
                    deskripsi = deskripsi,
                    onSuccess = {
                        showAddJenisSheet = false
                    }
                )
            }
        )
    }
}

@Composable
private fun JenisPakaianCard(
    item: JenisPakaianWithCategory,
    onClick: () -> Unit
) {
    TailorCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Category Tag
                Box(
                    modifier = Modifier
                        .clip(PillShape)
                        .background(Zinc100)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = item.namaKategori,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Zinc600
                    )
                }

                Text(
                    text = item.jenis.namaJenis,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Zinc900
                )

                val count = item.jenis.polaDetailCount ?: 0
                val countText = if (count > 0) "$count Ukuran Pola Tersedia" else "Belum ada ukuran standar"
                Text(
                    text = countText,
                    fontSize = 12.sp,
                    color = if (count > 0) Zinc600 else Zinc400,
                    fontWeight = FontWeight.Normal
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = Zinc400
            )
        }
    }
}
