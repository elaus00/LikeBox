package com.example.likebox.presentation.view.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.likebox.R
import com.example.likebox.presentation.view.screens.Screens

/**
 * Likebox 앱의 하단 네비게이션 바를 구현하는 컴포저블
 *
 * 주요 기능:
 * - Home, Search, Library, Settings 4개의 메인 섹션으로 구성
 * - 현재 선택된 아이템 하단에 인디케이터 표시
 * - 선택되지 않은 아이템은 투명도로 구분
 *
 * @param navController 네비게이션 상태 및 화면 전환을 관리하는 컨트롤러
 */
@Composable
fun LikeboxNavigationBar(
    navController: NavController
) {
    Surface(
        modifier = Modifier
            .wrapContentSize(),
        color = Color.White,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navigationItems.forEach { item ->
                NavigationItem(
                    item = item,
                    selected = isItemSelected(navController, item.screen),
                    onItemClick = {
                        navController.navigate(item.screen.route) {
                            // 중복 네비게이션 방지
                            launchSingleTop = true
                            // 백스택 처리
                            popUpTo(Screens.Main.Home.Root.route) {
                                saveState = true
                            }
                            // 상태 복원
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}

/**
 * 네비게이션 바의 각 아이템을 구현하는 컴포저블
 *
 * 디자인 스펙:
 * - 아이템 너비: 88.dp
 * - 아이템 패딩: 수직 14.dp
 * - 아이콘 컨테이너: 둥근 모서리(14.dp)
 * - 선택 인디케이터: 7.dp 원형 점
 *
 * @param item 네비게이션 아이템 정보 (화면 정보와 아이콘)
 * @param selected 현재 아이템이 선택되었는지 여부
 * @param onItemClick 아이템 클릭 시 호출되는 콜백
 */
@Composable
private fun NavigationItem(
    item: NavigationBarItem,
    selected: Boolean,
    onItemClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(horizontal = 12.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 아이콘 컨테이너
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White)
                .clickable(onClick = onItemClick)
                .padding(horizontal = 28.dp),
        ) {
            Icon(
                painter = painterResource(id = item.icon),
                contentDescription = null,
                modifier = Modifier.size(21.dp),
                tint = if (selected) {
                    Color(0xFF2F2F2F)  // 선택된 경우 진한 색상
                } else {
                    Color(0xFF2F2F2F).copy(alpha = 0.4f)  // 선택되지 않은 경우 흐린 색상
                }
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.navigation_indicator),
            contentDescription = null,
            modifier = Modifier.size(6.4.dp),
            tint = if (selected) Color(0xFFF93C58) else Color.Transparent
        )
    }
}

/**
 * 현재 네비게이션 스택에서 주어진 화면이 선택되었는지 확인하는 함수
 *
 * @param navController 현재 네비게이션 상태를 확인하기 위한 컨트롤러
 * @param screen 확인하고자 하는 화면
 * @return 해당 화면이 현재 선택되었는지 여부
 */
@Composable
private fun isItemSelected(
    navController: NavController,
    screen: Screens
): Boolean {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.hierarchy?.any {
        it.route == screen.route
    } ?: false
}

/**
 * 네비게이션 바의 각 아이템을 정의하는 데이터 클래스
 *
 * @property screen 해당 아이템이 대표하는 화면
 * @property icon 아이템에 표시될 아이콘 리소스 ID
 */
private data class NavigationBarItem(
    val screen: Screens,
    val icon: Int
)

/**
 * 네비게이션 바에 표시될 아이템들의 정적 리스트
 * 앱의 메인 섹션들을 정의함
 */
private val navigationItems = listOf(
    NavigationBarItem(
        screen = Screens.Main.Home.Root,
        icon = R.drawable.house
    ),
    NavigationBarItem(
        screen = Screens.Main.Search.Root,
        icon = R.drawable.search_icon
    ),
    NavigationBarItem(
        screen = Screens.Main.Library.Root,
        icon = R.drawable.music_library
    ),
    NavigationBarItem(
        screen = Screens.Main.Settings.Root,
        icon = R.drawable.settings
    )
)