package com.tailor.mobile.ui.screens.menu

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SquareFoot
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tailor.mobile.ui.components.TailorCard
import com.tailor.mobile.ui.components.TailorSecondaryButton
import com.tailor.mobile.ui.screens.auth.AuthViewModel
import com.tailor.mobile.ui.theme.CardShape
import com.tailor.mobile.ui.theme.ControlShape
import com.tailor.mobile.ui.theme.PillShape
import com.tailor.mobile.ui.theme.PureWhite
import com.tailor.mobile.ui.theme.Zinc100
import com.tailor.mobile.ui.theme.Zinc200
import com.tailor.mobile.ui.theme.Zinc400
import com.tailor.mobile.ui.theme.Zinc50
import com.tailor.mobile.ui.theme.Zinc500
import com.tailor.mobile.ui.theme.Zinc600
import com.tailor.mobile.ui.theme.Zinc700
import com.tailor.mobile.ui.theme.Zinc900

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    authViewModel: AuthViewModel,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val loginState by authViewModel.loginState.collectAsState()

    Scaffold(
        modifier = modifier,
        containerColor = Zinc50,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Menu & Fitur Lainnya",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Zinc900
                    )
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
            // Profile Card (DESIGN.md: avatar inisial monokrom + nama + email)
            TailorCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val initial = (loginState.user?.name ?: "P").take(1).uppercase()
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(PillShape)
                            .background(Zinc900),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = initial,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = PureWhite
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = loginState.user?.name ?: "Pemilik Usaha",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Zinc900
                        )
                        Text(
                            text = loginState.user?.email ?: "owner@tailor.test",
                            fontSize = 12.sp,
                            color = Zinc500
                        )
                    }
                }
            }

            // Section: Modul Usaha Jahit
            Text(
                text = "Modul Operasional",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Zinc500
            )

            TailorCard(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = 4.dp
            ) {
                MenuItemRow(
                    title = "Library Pola",
                    subtitle = "Standar pola dan ukuran pakaian",
                    icon = Icons.Outlined.Straighten,
                    onClick = { /* Akan disambungkan di Tahap 4 */ }
                )
                MenuItemDivider()
                MenuItemRow(
                    title = "Pengukuran Pelanggan",
                    subtitle = "Riwayat dimensi tubuh pelanggan",
                    icon = Icons.Outlined.SquareFoot,
                    onClick = { /* Akan disambungkan di Tahap 3 */ }
                )
                MenuItemDivider()
                MenuItemRow(
                    title = "Inventory Bahan & Alat",
                    subtitle = "Stok kain, benang, dan peralatan",
                    icon = Icons.Outlined.Inventory2,
                    onClick = { /* Akan disambungkan di Tahap 7 */ }
                )
                MenuItemDivider()
                MenuItemRow(
                    title = "Keuangan & Pengeluaran",
                    subtitle = "Arus kas pendapatan dan biaya operasional",
                    icon = Icons.Outlined.AccountBalanceWallet,
                    onClick = { /* Akan disambungkan di Tahap 8 */ }
                )
            }

            // Section: Pengaturan & Bantuan
            Text(
                text = "Lainnya",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Zinc500
            )

            TailorCard(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = 4.dp
            ) {
                MenuItemRow(
                    title = "Tutorial & Knowledge Base",
                    subtitle = "Tips, teknik jahit, dan artikel",
                    icon = Icons.Outlined.MenuBook,
                    onClick = { /* Akan disambungkan di Tahap 9 */ }
                )
                MenuItemDivider()
                MenuItemRow(
                    title = "Pengaturan Sistem",
                    subtitle = "Profil usaha, jenis jasa, dan tahapan",
                    icon = Icons.Outlined.Settings,
                    onClick = { /* Akan disambungkan di Tahap 9 */ }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Logout Button
            TailorSecondaryButton(
                text = "Keluar dari Akun (Logout)",
                onClick = onLogoutClick
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun MenuItemRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(ControlShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(ControlShape)
                .background(Zinc100),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Zinc900,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Zinc900
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Zinc500
            )
        }
        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = null,
            tint = Zinc400,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun MenuItemDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .height(1.dp)
            .background(Zinc100)
    )
}
