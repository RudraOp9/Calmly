package com.calmly.navigation

sealed class Screen(val route: String) {
    object Main : Screen("main")
    // Future screens can be added here
}