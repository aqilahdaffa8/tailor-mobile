package com.tailor.mobile.ui.screens.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tailor.mobile.ui.navigation.BottomNavItem
import com.tailor.mobile.ui.screens.auth.AuthViewModel
import com.tailor.mobile.ui.screens.dashboard.DashboardScreen
import com.tailor.mobile.ui.screens.dashboard.DashboardViewModel
import com.tailor.mobile.ui.screens.menu.MenuScreen
import com.tailor.mobile.ui.screens.pelanggan.PelangganScreen
import com.tailor.mobile.ui.screens.pelanggan.PelangganViewModel
import com.tailor.mobile.ui.screens.pesanan.PesananPlaceholderScreen
import com.tailor.mobile.ui.screens.pola.PolaScreen
import com.tailor.mobile.ui.theme.PureWhite
import com.tailor.mobile.ui.theme.Zinc100
import com.tailor.mobile.ui.theme.Zinc200
import com.tailor.mobile.ui.theme.Zinc50
import com.tailor.mobile.ui.theme.Zinc500
import com.tailor.mobile.ui.theme.Zinc900

@Composable
fun MainScaffold(
    authViewModel: AuthViewModel,
    onLogoutSuccess: () -> Unit,
    dashboardViewModel: DashboardViewModel = viewModel(),
    pelangganViewModel: PelangganViewModel = viewModel()
) {
    var currentTab by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Dashboard) }
    var triggerAddCustomer by remember { mutableStateOf(false) }
    var showingPolaLibrary by remember { mutableStateOf(false) }

    if (showingPolaLibrary) {
        BackHandler { showingPolaLibrary = false }
        PolaScreen(
            onBack = { showingPolaLibrary = false }
        )
        return
    }

    val navItems = listOf(
        BottomNavItem.Dashboard,
        BottomNavItem.Pesanan,
        BottomNavItem.Pelanggan,
        BottomNavItem.Menu
    )

    Scaffold(
        containerColor = Zinc50,
        bottomBar = {
            NavigationBar(
                containerColor = PureWhite,
                tonalElevation = 0.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        // Subtle Zinc 200 border-t per DESIGN.md
                        drawLine(
                            color = Zinc200,
                            start = Offset(0f, 0f),
                            end = Offset(size.width, 0f),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
            ) {
                navItems.forEach { item ->
                    val isSelected = currentTab == item
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            if (currentTab == BottomNavItem.Pelanggan && item == BottomNavItem.Pelanggan) {
                                pelangganViewModel.clearSelectedPelanggan()
                            }
                            currentTab = item
                        },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.icon,
                                contentDescription = item.label
                            )
                        },
                        label = {
                            Text(
                                text = item.label,
                                fontSize = 12.sp, // Enforce min 12sp per DESIGN.md
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Zinc900,
                            selectedTextColor = Zinc900,
                            indicatorColor = Zinc100,
                            unselectedIconColor = Zinc500,
                            unselectedTextColor = Zinc500
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                is BottomNavItem.Dashboard -> {
                    DashboardScreen(
                        viewModel = dashboardViewModel,
                        onNavigateToNewOrder = {
                            currentTab = BottomNavItem.Pesanan
                        },
                        onNavigateToNewCustomer = {
                            triggerAddCustomer = true
                            currentTab = BottomNavItem.Pelanggan
                        },
                        onOrderClick = { /* Nanti navigasi ke detail pesanan di Tahap 5 */ }
                    )
                }
                is BottomNavItem.Pesanan -> {
                    PesananPlaceholderScreen()
                }
                is BottomNavItem.Pelanggan -> {
                    val openAdd = triggerAddCustomer
                    if (triggerAddCustomer) {
                        triggerAddCustomer = false
                    }
                    PelangganScreen(
                        viewModel = pelangganViewModel,
                        initialShowAddDialog = openAdd
                    )
                }
                is BottomNavItem.Menu -> {
                    MenuScreen(
                        authViewModel = authViewModel,
                        onLogoutClick = {
                            authViewModel.logout(onLogoutSuccess)
                        },
                        onNavigateToPelanggan = {
                            pelangganViewModel.clearSelectedPelanggan()
                            currentTab = BottomNavItem.Pelanggan
                        },
                        onNavigateToPola = {
                            showingPolaLibrary = true
                        }
                    )
                }
            }
        }
    }
}
