package com.tailor.mobile.ui.screens.pelanggan

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tailor.mobile.ui.components.TailorCard
import com.tailor.mobile.ui.theme.PureWhite
import com.tailor.mobile.ui.theme.Zinc50
import com.tailor.mobile.ui.theme.Zinc500
import com.tailor.mobile.ui.theme.Zinc900

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PelangganPlaceholderScreen(
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        containerColor = Zinc50,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Pelanggan",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Zinc900
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PureWhite)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            TailorCard {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Modul Pelanggan",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Zinc900
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Akan dikembangkan pada Tahap 3 (Daftar Pelanggan, Tambah/Edit, & Riwayat Ukuran Tubuh).",
                        fontSize = 13.sp,
                        color = Zinc500
                    )
                }
            }
        }
    }
}
