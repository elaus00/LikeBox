package com.example.likebox.presentation.view.screens.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.likebox.domain.model.library.MusicPlatform
import com.example.likebox.domain.model.settings.SyncInterval
import com.example.likebox.domain.model.settings.SyncSettings
import com.example.likebox.domain.model.settings.ThemeSettings
import com.example.likebox.presentation.view.screens.library.PlatformIcon
import com.example.likebox.presentation.view.theme.PretendardFontFamily
import com.example.likebox.presentation.view.theme.mainColor

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

@Composable
fun ThemeSection(
    themeSettings: ThemeSettings,
    onThemeSettingsChanged: (ThemeSettings) -> Unit
) {
    SettingsSection(title = "Theme") {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            SwitchSettingItem(
                title = "Dark Mode",
                subtitle = "Enable dark theme",
                checked = themeSettings.isDarkMode,
                onCheckedChange = { checked ->
                    onThemeSettingsChanged(themeSettings.copy(isDarkMode = checked))
                }
            )

            SwitchSettingItem(
                title = "Dynamic Colors",
                subtitle = "Use system accent colors",
                checked = themeSettings.useDynamicColor,
                onCheckedChange = { checked ->
                    onThemeSettingsChanged(themeSettings.copy(useDynamicColor = checked))
                }
            )
        }
    }
}

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
            color = mainColor  // 섹션 타이틀 색상 변경
        )
        content()
    }
}

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
                checkedTrackColor = mainColor,  // 스위치 켜진 상태 색상
                checkedBorderColor = mainColor  // 스위치 켜진 상태 테두리 색상
            )
        )
    }
}

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
                tint = mainColor  // 화살표 색상
            )
        }
    }
}

@Composable
private fun FilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: String
) {
    Surface(
        modifier = Modifier.padding(end = 8.dp),
        shape = RoundedCornerShape(16.dp),
        color = if (selected) mainColor else Color.White,
        border = BorderStroke(1.dp, if (selected) mainColor else Color.Gray.copy(alpha = 0.3f))
    ) {
        Box(
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = label,
                color = if (selected) Color.White else Color.Black,
                fontSize = 14.sp,
                fontFamily = PretendardFontFamily
            )
        }
    }
}

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

@Composable
fun PlatformSection(
    syncSettings: SyncSettings,
    onSyncSettingsChanged: (SyncSettings) -> Unit
) {
    SettingsSection(title = "Connected Platforms") {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            syncSettings.platformSyncEnabled.forEach { (platform, enabled) ->
                SwitchSettingItem(
                    title = platform.name,
                    subtitle = if (enabled) "Connected" else "Disconnected",
                    checked = enabled,
                    onCheckedChange = { checked ->
                        val newMap = syncSettings.platformSyncEnabled.toMutableMap()
                        newMap[platform] = checked
                        onSyncSettingsChanged(syncSettings.copy(platformSyncEnabled = newMap))
                    }
                )
            }
        }
    }
}



@Composable
fun ActionButton(
    text: String,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isDestructive) Color.Red else mainColor
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SyncSection(
    syncSettings: SyncSettings,
    onSyncSettingsChanged: (SyncSettings) -> Unit
) {
    SettingsSection(title = "Sync Settings") {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            var showIntervalDialog by remember { mutableStateOf(false) }

            ClickableSettingItem(
                title = "Sync Interval",
                subtitle = "Every ${syncSettings.syncInterval.name.lowercase()}",
                onClick = { showIntervalDialog = true }
            )

            SwitchSettingItem(
                title = "Auto Sync",
                subtitle = "Automatically sync content",
                checked = syncSettings.autoSync,
                onCheckedChange = { checked ->
                    onSyncSettingsChanged(syncSettings.copy(autoSync = checked))
                }
            )

            SwitchSettingItem(
                title = "Wi-Fi Only",
                subtitle = "Sync only when connected to Wi-Fi",
                checked = syncSettings.syncOnWifiOnly,
                onCheckedChange = { checked ->
                    onSyncSettingsChanged(syncSettings.copy(syncOnWifiOnly = checked))
                }
            )

            if (showIntervalDialog) {
                AlertDialog(
                    onDismissRequest = { showIntervalDialog = false },
                    title = { Text("Select Sync Interval") },
                    text = {
                        Column {
                            SyncInterval.entries.forEach { interval ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            onSyncSettingsChanged(
                                                syncSettings.copy(syncInterval = interval)
                                            )
                                            showIntervalDialog = false
                                        }
                                        .padding(vertical = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(interval.name.lowercase().capitalize())
                                    RadioButton(
                                        selected = interval == syncSettings.syncInterval,
                                        onClick = null
                                    )
                                }
                            }
                        }
                    },
                    confirmButton = {},
                    dismissButton = {
                        TextButton(onClick = { showIntervalDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}

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

@Composable
fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Something went wrong",
            style = MaterialTheme.typography.titleLarge
        )
        Text(message)
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}