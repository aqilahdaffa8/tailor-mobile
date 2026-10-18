package com.tailor.mobile.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
) {
    object Dashboard : BottomNavItem(
        route = "nav_dashboard",
        label = "Beranda",
        icon = Icons.Outlined.Dashboard,
        selectedIcon = Icons.Filled.Dashboard
    )

    object Pesanan : BottomNavItem(
        route = "nav_pesanan",
        label = "Pesanan",
        icon = Icons.Outlined.ReceiptLong,
        selectedIcon = Icons.Filled.ReceiptLong
    )

    object Pelanggan : BottomNavItem(
        route = "nav_pelanggan",
        label = "Pelanggan",
        icon = Icons.Outlined.People,
        selectedIcon = Icons.Filled.People
    )

    object Menu : BottomNavItem(
        route = "nav_menu",
        label = "Menu",
        icon = Icons.Outlined.GridView,
        selectedIcon = Icons.Filled.GridView
    )
}
