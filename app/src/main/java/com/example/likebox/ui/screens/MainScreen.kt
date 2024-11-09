package com.example.likebox.ui.screens

import LibraryScreen
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.likebox.ui.navigation.AppNavigationBar
import com.example.likebox.ui.navigation.NavBarItem
import com.example.likebox.ui.screens.home.HomeScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            AppNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavBarItem.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(NavBarItem.Home.route) {
                HomeScreen()
            }
            composable(NavBarItem.Search.route) {
                SearchScreen()
            }
            composable(NavBarItem.Library.route) {
                LibraryScreen()
            }
            composable(NavBarItem.Settings.route) {
                SettingsScreen()
            }
        }
    }
}


@Composable
fun SettingsScreen() {
    TODO("Not yet implemented")
}

@Composable
fun SearchScreen() {
    TODO("Not yet implemented")
}
