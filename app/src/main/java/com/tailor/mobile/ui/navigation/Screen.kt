package com.tailor.mobile.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Main : Screen("main")
}
