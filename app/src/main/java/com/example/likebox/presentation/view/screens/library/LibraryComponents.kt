// LibraryComponents.kt
package com.example.likebox.presentation.view.screens.library

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.likebox.R
import com.example.likebox.domain.model.*
import com.example.likebox.presentation.view.theme.PretendardFontFamily

object LibraryComponents {
    @Composable
    fun ContentListItem(
        content: MusicContent,
        onItemClick: (String) -> Unit,
        showChevron: Boolean = true
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 1.dp)
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = content.thumbnailUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(7.5.dp))
                )

                Column(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = content.name,
                        fontSize = 16.sp,
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Medium
                    )

                    ContentMetadata(content = content)
                }
            }

            if (showChevron) {
                IconButton(onClick = { onItemClick(content.id) }) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Navigate",
                        modifier = Modifier.size(15.dp),
                        tint = Color.Black.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }

    @Composable
    private fun ContentMetadata(content: MusicContent) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(9.08.dp)
        ) {
            Text(
                text = getContentTypeLabel(content),
                fontSize = 10.sp,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium
            )
            MetadataDot()
            Text(
                text = getContentCreator(content),
                fontSize = 10.sp,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium
            )
        }
    }

    @Composable
    private fun MetadataDot() {
        Box(
            modifier = Modifier
                .size(2.27.dp)
                .background(
                    Color.Black.copy(alpha = 0.8f),
                    CircleShape
                )
        )
    }

    private fun getContentTypeLabel(content: MusicContent): String = when(content) {
        is Track -> "Song"
        is Album -> "Album"
        is Playlist -> "Playlist"
        else -> ""
    }

    private fun getContentCreator(content: MusicContent): String = when(content) {
        is Track -> content.artists.firstOrNull() ?: ""
        is Album -> content.artists.firstOrNull() ?: ""
        is Playlist -> content.owner
        else -> ""
    }

    @Composable
    fun PlatformSection(platform: MusicPlatform) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .padding(horizontal = 1.dp)
                .border(
                    width = 0.7.dp,
                    color = Color.Black.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(4.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        color = getPlatformColor(platform),
                        shape = RoundedCornerShape(4.dp)
                    )
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = platform.name,
                fontSize = 13.sp,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium,
                color = Color.Black.copy(alpha = 0.7f)
            )
        }
    }

    private fun getPlatformColor(platform: MusicPlatform): Color = when (platform) {
        MusicPlatform.SPOTIFY -> Color(0xFF1ED760)
        MusicPlatform.APPLE_MUSIC -> Color(0xFFFC3C44)
        else -> Color.Gray
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DetailTopBar(
        title: String,
        onNavigateBack: () -> Unit,
        showShare: Boolean = true,
        onShare: (() -> Unit)? = null,
        showMoreOptions: Boolean = true,
        additionalActions: @Composable (RowScope.() -> Unit) = {}
    ) {
        var showMenu by remember { mutableStateOf(false) }

        TopAppBar(
            title = {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.back_arrow),
                        contentDescription = "Back"
                    )
                }
            },
            actions = {
                // Custom actions passed from parent
                additionalActions()

                // Share action if enabled
                if (showShare) {
                    IconButton(onClick = { onShare?.invoke() }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share"
                        )
                    }
                }

                // More options menu if enabled
                if (showMoreOptions) {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options"
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Add to playlist") },
                            onClick = {
                                // Handle add to playlist
                                showMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Download") },
                            onClick = {
                                // Handle download
                                showMenu = false
                            }
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                actionIconContentColor = MaterialTheme.colorScheme.onSurface
            )
        )
    }

}