package com.example.likebox.presentation.view.screens.library

// 필요한 import문들
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LibraryScreen(
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("좋아요", "플레이리스트", "앨범", "아티스트")

    Column(modifier = modifier) {
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = selectedTab == index,
                    onClick = { selectedTab = index }
                )
            }
        }

        when (selectedTab) {
            0 -> LikesTab()
            1 -> PlaylistsTab()
            2 -> AlbumsTab()
            3 -> ArtistsTab()
        }
    }
}

@Composable
fun LikesTab() {
    // 좋아요 탭 내용 구현
}

@Composable
fun PlaylistsTab() {
    // 플레이리스트 탭 내용 구현
}

@Composable
fun AlbumsTab() {
    // 앨범 탭 내용 구현
}

@Composable
fun ArtistsTab() {
    // 아티스트 탭 내용 구현
}

