package com.example.likebox.presentation.view.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.likebox.domain.model.*
import com.example.likebox.domain.model.auth.User
import com.example.likebox.presentation.view.theme.PretendardFontFamily

/**
 * 설정 화면의 메인 컴포저블
 * 모든 설정 관련 UI 컴포넌트들을 조합하여 완성된 설정 화면을 구성
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settings: Settings,
    user: User?,
    onSettingsChanged: (Settings) -> Unit,
    onProfileEdit: () -> Unit,
    onProfileImageEdit: () -> Unit,
    onExportData: () -> Unit,
    onImportData: () -> Unit,
    onResetSettings: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        fontSize = 20.sp,
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 프로필 섹션
            item(key = "profile") {
                ProfileSection(
                    user = user,
                    onEditClick = onProfileEdit,
                    onImageClick = onProfileImageEdit
                )
            }

            // 테마 설정 섹션
            item(key = "theme") {
                ThemeSection(
                    themeSettings = settings.theme,
                    onThemeSettingsChanged = { theme ->
                        onSettingsChanged(settings.copy(theme = theme))
                    }
                )
            }

            // 플랫폼 연결 섹션
            item(key = "platforms") {
                PlatformSection(
                    syncSettings = settings.sync,
                    onSyncSettingsChanged = { sync ->
                        onSettingsChanged(settings.copy(sync = sync))
                    }
                )
            }

            // 동기화 설정 섹션
            item(key = "sync") {
                SyncSection(
                    syncSettings = settings.sync,
                    onSyncSettingsChanged = { sync ->
                        onSettingsChanged(settings.copy(sync = sync))
                    }
                )
            }

            // 데이터 관리 섹션
            item(key = "data") {
                DataManagementSection(
                    onExportClick = onExportData,
                    onImportClick = onImportData,
                    onResetClick = onResetSettings
                )
            }
        }
    }
}