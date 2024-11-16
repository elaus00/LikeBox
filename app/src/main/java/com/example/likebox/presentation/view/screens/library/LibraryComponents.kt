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
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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

    @Composable
    fun ContentListItem(
        content: MusicContent,
        onItemClick: (String) -> Unit,
        showChevron: Boolean = true,
        modifier: Modifier = Modifier
    ) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(12.dp),
            onClick = { onItemClick(content.id) }
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Thumbnail with platform indicator
                Box {
                    AsyncImage(
                        model = content.thumbnailUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    // Platform indicator
                    Surface(
                        modifier = Modifier
                            .size(16.dp)
                            .align(Alignment.BottomEnd)
                            .offset(x = 4.dp, y = 4.dp),
                        shape = CircleShape,
                        color = getPlatformColor(content.platform)
                    ) {
                        Icon(
                            painter = painterResource(
                                id = when(content.platform) {
                                    MusicPlatform.SPOTIFY -> R.drawable.spotify_logomark
                                    MusicPlatform.APPLE_MUSIC -> R.drawable.applemusic_logomark
                                }
                            ),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .padding(4.dp)
                                .size(8.dp)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                ) {
                    Text(
                        text = content.name,
                        fontSize = 16.sp,
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    ContentMetadata(content = content)
                }

                if (showChevron) {
                    IconButton(
                        onClick = { onItemClick(content.id) },
                        modifier = Modifier.offset(x = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Navigate",
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun ContentMetadata(content: MusicContent) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ContentTypeChip(content)
            MetadataDot()
            Text(
                text = getContentCreator(content),
                fontSize = 12.sp,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

    @Composable
    private fun ContentTypeChip(content: MusicContent) {
        Surface(
            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                text = getContentTypeLabel(content),
                fontSize = 10.sp,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    }

    @Composable
    fun PlatformTabRow(
        selectedPlatform: MusicPlatform,
        onPlatformSelected: (MusicPlatform) -> Unit,
        modifier: Modifier = Modifier
    ) {
        Surface(
            modifier = modifier,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 1.dp
        ) {
            TabRow(
                selectedTabIndex = MusicPlatform.entries.indexOf(selectedPlatform),
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary,
                indicator = { tabPositions ->
                    SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(
                            tabPositions[MusicPlatform.entries.indexOf(selectedPlatform)]
                        ),
                        height = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            ) {
                MusicPlatform.entries.forEach { platform ->
                    PlatformTab(
                        platform = platform,
                        selected = platform == selectedPlatform,
                        onClick = { onPlatformSelected(platform) }
                    )
                }
            }
        }
    }

    @Composable
    private fun PlatformTab(
        platform: MusicPlatform,
        selected: Boolean,
        onClick: () -> Unit
    ) {
        Tab(
            selected = selected,
            onClick = onClick,
            text = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = if (selected) {
                            getPlatformColor(platform)
                        } else {
                            getPlatformColor(platform).copy(alpha = 0.6f)
                        }
                    ) {
                        Icon(
                            painter = painterResource(
                                id = when(platform) {
                                    MusicPlatform.SPOTIFY -> R.drawable.spotify_logomark
                                    MusicPlatform.APPLE_MUSIC -> R.drawable.applemusic_logomark
                                }
                            ),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .padding(4.dp)
                                .size(16.dp)
                        )
                    }
                    Text(
                        text = platform.name,
                        fontFamily = PretendardFontFamily,
                        fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal
                    )
                }
            }
        )
    }

}