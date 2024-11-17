package com.example.likebox.presentation.view.screens.settings

// Android Compose 기본 import
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 이미지 로딩 라이브러리
import coil.compose.AsyncImage

// 도메인 모델
import com.example.likebox.domain.model.Settings
import com.example.likebox.domain.model.auth.User
import com.example.likebox.domain.model.library.MusicPlatform
import com.example.likebox.domain.model.settings.NotificationSettings
import com.example.likebox.domain.model.settings.SyncInterval
import com.example.likebox.domain.model.settings.SyncSettings
import com.example.likebox.domain.model.settings.ThemeSettings

// 커스텀 컴포넌트와 테마
import com.example.likebox.presentation.view.screens.library.PlatformIcon
import com.example.likebox.presentation.view.theme.PretendardFontFamily
import com.example.likebox.presentation.view.theme.mainColor

/**
 * 설정 화면의 상단 앱바
 */
@Composable
fun SettingsTopBar() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Settings",
                fontSize = 20.sp,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/**
 * 설정의 각 섹션을 감싸는 컨테이너
 */
@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.SemiBold,
            color = mainColor
        )
        content()
    }
}

/**
 * 스위치가 포함된 설정 아이템
 */
@Composable
private fun SwitchSettingItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = mainColor,
                checkedBorderColor = mainColor
            )
        )
    }
}

/**
 * 클릭 가능한 설정 아이템
 */
@Composable
private fun ClickableSettingItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    titleColor: Color = Color.Black,
    showArrow: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium,
                color = titleColor
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
        if (showArrow) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = mainColor
            )
        }
    }
}

/**
 * 동기화 간격 선택 드롭다운
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SyncIntervalSelector(
    currentInterval: SyncInterval,
    onIntervalSelected: (SyncInterval) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = currentInterval.name.lowercase().capitalize(),
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = mainColor,
                focusedLabelColor = mainColor,
                focusedTrailingIconColor = mainColor
            ),
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            SyncInterval.entries.forEach { interval ->
                DropdownMenuItem(
                    text = { Text(interval.name.lowercase().capitalize()) },
                    onClick = {
                        onIntervalSelected(interval)
                        expanded = false
                    },
                    trailingIcon = if (interval == currentInterval) {
                        {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = mainColor
                            )
                        }
                    } else null
                )
            }
        }
    }
}

/**
 * 데이터 관리 섹션
 */
@Composable
fun DataManagementSection(
    onExportClick: () -> Unit,
    onImportClick: () -> Unit,
    onResetClick: () -> Unit
) {
    SettingsSection(title = "Data Management") {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            ClickableSettingItem(
                title = "Export Data",
                subtitle = "Export your likes and settings",
                onClick = onExportClick
            )

            ClickableSettingItem(
                title = "Import Data",
                subtitle = "Import previously exported data",
                onClick = onImportClick
            )

            ClickableSettingItem(
                title = "Reset Settings",
                subtitle = "Reset all settings to default",
                onClick = onResetClick,
                titleColor = Color.Red
            )
        }
    }
}

/**
 * 프로필 섹션
 */
@Composable
fun ProfileSection(
    user: User?,
    onEditClick: () -> Unit,
    onImageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showEditDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        shape = MaterialTheme.shapes.medium,
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Image
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .border(2.dp, mainColor, CircleShape)
                    .clickable(onClick = onImageClick)
            ) {
                AsyncImage(
                    model = user?.profilePictureUrl ?: "/api/placeholder/80/80",
                    contentDescription = "Profile Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Profile Image",
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(24.dp)
                        .background(mainColor, CircleShape)
                        .padding(4.dp)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = user?.nickName ?: "Guest",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = user?.email ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            Button(
                onClick = onEditClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = mainColor
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Edit Profile")
            }
        }
    }

    if (showEditDialog) {
        ProfileEditDialog(
            user = user,
            onDismiss = { showEditDialog = false },
            onSave = { nickname, email, phone ->
                // Handle profile update
                showEditDialog = false
            }
        )
    }
}

/**
 * 프로필 편집 다이얼로그
 */
@Composable
private fun ProfileEditDialog(
    user: User?,
    onDismiss: () -> Unit,
    onSave: (nickname: String, email: String, phone: String) -> Unit
) {
    var nickname by remember { mutableStateOf(user?.nickName ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var phone by remember { mutableStateOf(user?.phoneNumber?.toString() ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Profile") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = nickname,
                    onValueChange = { nickname = it },
                    label = { Text("Nickname") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = mainColor,
                        focusedLabelColor = mainColor
                    )
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = mainColor,
                        focusedLabelColor = mainColor
                    )
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = mainColor,
                        focusedLabelColor = mainColor
                    )
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(nickname, email, phone) },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = mainColor
                )
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

/**
 * 전체 설정 목록을 표시하는 메인 컴포저블
 * 테마, 동기화, 플랫폼, 데이터 관리 등 모든 설정 섹션을 포함
 *
 * @param settings 현재 설정 상태
 * @param callbacks 각종 설정 변경에 대한 콜백 함수들
 * @param modifier 컴포넌트의 수정자
 */
@Composable
fun SettingsList(
    settings: Settings,
    callbacks: SettingsCallbacks,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 테마 설정 섹션
        item(key = "theme") {
            ThemeSection(
                themeSettings = settings.theme,
                onThemeSettingsChanged = callbacks.onThemeChanged
            )
        }

        // 플랫폼 연결 섹션
        item(key = "platforms") {
            PlatformSection(
                syncSettings = settings.sync,
                onSyncSettingsChanged = callbacks.onSyncSettingsChanged
            )
        }

        // 동기화 설정 섹션
        item(key = "sync") {
            SyncSection(
                syncSettings = settings.sync,
                onSyncSettingsChanged = callbacks.onSyncSettingsChanged
            )
        }

        // 데이터 관리 섹션
        item(key = "data") {
            DataManagementSection(
                onExportClick = callbacks.onExportClick,
                onImportClick = callbacks.onImportClick,
                onResetClick = callbacks.onResetClick
            )
        }
    }
}

/**
 * 테마 설정 섹션 컴포저블
 */
@Composable
fun ThemeSection(
    themeSettings: ThemeSettings,
    onThemeSettingsChanged: (ThemeSettings) -> Unit
) {
    // 콜백 함수들을 메모이제이션하여 불필요한 재생성 방지
    val onDarkModeChanged = remember(onThemeSettingsChanged) {
        { checked: Boolean ->
            onThemeSettingsChanged(themeSettings.copy(isDarkMode = checked))
        }
    }

    val onDynamicColorChanged = remember(onThemeSettingsChanged) {
        { checked: Boolean ->
            onThemeSettingsChanged(themeSettings.copy(useDynamicColor = checked))
        }
    }

    SettingsSection(title = "Theme") {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            SwitchSettingItem(
                title = "Dark Mode",
                subtitle = "Enable dark theme",
                checked = themeSettings.isDarkMode,
                onCheckedChange = onDarkModeChanged
            )

            SwitchSettingItem(
                title = "Dynamic Colors",
                subtitle = "Use system accent colors",
                checked = themeSettings.useDynamicColor,
                onCheckedChange = onDynamicColorChanged
            )
        }
    }
}

/**
 * 동기화 설정 섹션 컴포저블
 */
@Composable
fun SyncSection(
    syncSettings: SyncSettings,
    onSyncSettingsChanged: (SyncSettings) -> Unit
) {
    // 콜백 함수들을 메모이제이션
    val onIntervalChanged = remember(onSyncSettingsChanged) {
        { interval: SyncInterval ->
            onSyncSettingsChanged(syncSettings.copy(syncInterval = interval))
        }
    }

    val onAutoSyncChanged = remember(onSyncSettingsChanged) {
        { enabled: Boolean ->
            onSyncSettingsChanged(syncSettings.copy(autoSync = enabled))
        }
    }

    val onWifiOnlyChanged = remember(onSyncSettingsChanged) {
        { enabled: Boolean ->
            onSyncSettingsChanged(syncSettings.copy(syncOnWifiOnly = enabled))
        }
    }

    SettingsSection(title = "Sync") {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            SyncIntervalSelector(
                currentInterval = syncSettings.syncInterval,
                onIntervalSelected = onIntervalChanged
            )

            SwitchSettingItem(
                title = "Auto Sync",
                subtitle = "Automatically sync content",
                checked = syncSettings.autoSync,
                onCheckedChange = onAutoSyncChanged
            )

            SwitchSettingItem(
                title = "Wi-Fi Only",
                subtitle = "Sync only when connected to Wi-Fi",
                checked = syncSettings.syncOnWifiOnly,
                onCheckedChange = onWifiOnlyChanged
            )
        }
    }
}

/**
 * 플랫폼 연결 섹션 컴포저블
 */
@Composable
fun PlatformSection(
    syncSettings: SyncSettings,
    onSyncSettingsChanged: (SyncSettings) -> Unit
) {
    SettingsSection(title = "Connected Platforms") {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            syncSettings.platformSyncEnabled.forEach { (platform, enabled) ->
                key(platform) {
                    val onPlatformSyncChanged = remember(platform, onSyncSettingsChanged) {
                        { checked: Boolean ->
                            val newMap = syncSettings.platformSyncEnabled.toMutableMap()
                            newMap[platform] = checked
                            onSyncSettingsChanged(syncSettings.copy(platformSyncEnabled = newMap))
                        }
                    }

                    PlatformConnectionItem(
                        platform = platform,
                        isConnected = enabled,
                        onConnectClick = { onPlatformSyncChanged(!enabled) }
                    )
                }
            }
        }
    }
}

/**
 * 각 음악 플랫폼 연결 아이템 컴포저블
 */
@Composable
private fun PlatformConnectionItem(
    platform: MusicPlatform,
    isConnected: Boolean,
    onConnectClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PlatformIcon(platform = platform)
            Column {
                Text(
                    text = platform.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = if (isConnected) "Connected" else "Not connected",
                    fontSize = 12.sp,
                    color = if (isConnected) mainColor else Color.Gray
                )
            }
        }
        TextButton(
            onClick = onConnectClick,
            colors = ButtonDefaults.textButtonColors(
                contentColor = mainColor
            )
        ) {
            Text(if (isConnected) "Disconnect" else "Connect")
        }
    }
}

/**
 * 모든 설정 관련 콜백을 담는 데이터 클래스
 */
class SettingsCallbacks(
    val onThemeChanged: (ThemeSettings) -> Unit,
    val onSyncSettingsChanged: (SyncSettings) -> Unit,
    val onNotificationSettingsChanged: (NotificationSettings) -> Unit,
    val onExportClick: () -> Unit,
    val onImportClick: () -> Unit,
    val onResetClick: () -> Unit
)