package com.tailor.mobile.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tailor.mobile.ui.components.BadgeVariant
import com.tailor.mobile.ui.components.StatusBadge
import com.tailor.mobile.ui.components.TailorCard
import com.tailor.mobile.ui.components.TailorSecondaryButton
import com.tailor.mobile.ui.screens.auth.AuthViewModel
import com.tailor.mobile.ui.theme.PureWhite
import com.tailor.mobile.ui.theme.Zinc200
import com.tailor.mobile.ui.theme.Zinc50
import com.tailor.mobile.ui.theme.Zinc500
import com.tailor.mobile.ui.theme.Zinc700
import com.tailor.mobile.ui.theme.Zinc900

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    authViewModel: AuthViewModel,
    onLogoutSuccess: () -> Unit
) {
    val state by authViewModel.loginState.collectAsState()

    Scaffold(
        containerColor = Zinc50,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "TAILOR SYSTEM",
                        fontSize = 18.sp,
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
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Status Card
            TailorCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                StatusBadge(
                    text = "Terhubung ke Laravel API",
                    variant = BadgeVariant.SUCCESS
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Selamat Datang!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Zinc900
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = state.user?.name ?: "Pemilik Usaha",
                    fontSize = 14.sp,
                    color = Zinc700
                )
                Text(
                    text = state.user?.email ?: "",
                    fontSize = 12.sp,
                    color = Zinc500
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Logout Action
            TailorSecondaryButton(
                text = "Keluar (Logout)",
                onClick = {
                    authViewModel.logout(onLogoutSuccess)
                }
            )
        }
    }
}
