package com.example.likebox

import LibraryScreen
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.likebox.navigation.AppNavigationBar
import com.example.likebox.navigation.NavBarItem

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
