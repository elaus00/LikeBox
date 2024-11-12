package com.example.likebox.presentation.view.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.likebox.presentation.view.screens.Screens
import com.example.likebox.presentation.viewmodel.NavigationViewModel

/**
 * 하단 네비게이션 바에 표시될 각 항목의 데이터 클래스
 *
 * @param screen 연결된 화면 정보
 * @param icon 표시될 아이콘
 * @param onClick 해당 항목 클릭 시 실행될 함수
 */
data class NavigationItem(
    val screen: Screens,
    val icon: ImageVector,
    val onClick: NavigationViewModel.() -> Unit
)

/**
 * 앱의 하단 네비게이션 바
 * - 라벨 없이 아이콘만 표시
 * - 선택된 항목은 아래에 빨간 점으로 표시
 *
 * @param viewModel 네비게이션 처리를 위한 ViewModel
 */
@Composable
fun AppNavigationBar(
    viewModel: NavigationViewModel = hiltViewModel()
) {
    val navigationState by viewModel.navigationState.collectAsState()

    val navigationItems = remember {
        listOf(
            NavigationItem(
                screen = Screens.Home.Root,
                icon = Icons.Default.Home,
                onClick = NavigationViewModel::navigateToHome
            ),
            NavigationItem(
                screen = Screens.Search.Root,
                icon = Icons.Default.Search,
                onClick = NavigationViewModel::navigateToSearch
            ),
            NavigationItem(
                screen = Screens.Library.Root,
                icon = Icons.Default.LibraryMusic,
                onClick = NavigationViewModel::navigateToLibrary
            ),
            NavigationItem(
                screen = Screens.Settings.Root,
                icon = Icons.Default.Settings,
                onClick = NavigationViewModel::navigateToSettings
            )
        )
    }

    NavigationBar(
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        navigationItems.forEach { item ->
            val isSelected = navigationState.currentScreen.route == item.screen.route

            NavigationBarItem(
                icon = {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        if (isSelected) {
                            SelectionDot(
                                modifier = Modifier.padding(top = 32.dp)
                            )
                        }
                    }
                },
                selected = isSelected,
                onClick = { item.onClick(viewModel) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

/**
 * 선택 표시를 위한 빨간 점 Composable
 */
@Composable
private fun SelectionDot(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(4.dp)
            .background(Color.Red, CircleShape)
    )
}