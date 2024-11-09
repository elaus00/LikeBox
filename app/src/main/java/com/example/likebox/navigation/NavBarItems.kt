package com.example.likebox.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavBarItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val contentDescription: String? = null
) {
    object Home : NavBarItem(
        route = Screen.Main.Home.route,
        title = "홈",
        icon = Icons.Default.Home,
        contentDescription = "홈 화면"
    )

    object Search : NavBarItem(
        route = Screen.Main.Search.route,
        title = "검색",
        icon = Icons.Default.Search,
        contentDescription = "검색 화면"
    )

    object Library : NavBarItem(
        route = Screen.Main.Library.route,
        title = "보관함",
        icon = Icons.Default.LibraryMusic,
        contentDescription = "보관함 화면"
    )

    object Settings : NavBarItem(
        route = Screen.Main.Settings.route,
        title = "설정",
        icon = Icons.Default.Settings,
        contentDescription = "설정 화면"
    )

    companion object {
        val items = listOf(Home, Search, Library, Settings)
    }
}