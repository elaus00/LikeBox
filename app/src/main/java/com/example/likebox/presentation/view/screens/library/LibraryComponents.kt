package com.example.likebox.presentation.view.screens.library

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import java.time.Instant

@Composable
fun ContentTypeSelector(
    selectedType: ContentType,
    onTypeSelected: (ContentType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ContentType.entries.forEach { type ->
            FilterChip(
                selected = selectedType == type,
                onClick = { onTypeSelected(type) },
                label = {
                    Text(
                        text = type.name.lowercase().capitalize(),
                        fontFamily = PretendardFontFamily
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFF93C58),
                    selectedLabelColor = Color.White,
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = selectedType == type,
                    borderColor = Color(0xFFE8E8E8)  // 비활성화 상태의 border 색상
                )
            )
        }
    }
}

@Composable
fun SortButton(
    selectedSort: String,
    onSortClick: () -> Unit
) {
    TextButton(
        onClick = onSortClick,
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = "Sort by: $selectedSort",
            color = Color(0xFFF93C58),
            fontSize = 14.sp,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Medium
        )
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = null,
            tint = Color(0xFFF93C58)
        )
    }
}

@Composable
fun PlatformIcon(
    platform: MusicPlatform,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = CircleShape,
        color = when(platform) {
            MusicPlatform.SPOTIFY -> Color(0xFF1DB954)
            MusicPlatform.APPLE_MUSIC -> Color(0xFFFC3C44)
        },
        modifier = modifier.size(14.dp)
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
                .padding(2.dp)
                .size(10.dp)
        )
    }
}

@Composable
fun ContentItemMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onMenuClick: () -> Unit
) {
    Box {
        IconButton(onClick = onMenuClick) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More options",
                tint = Color.Gray,
                modifier = Modifier.size(16.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismiss
        ) {
            DropdownMenuItem(
                text = { Text("Add to playlist") },
                onClick = onDismiss
            )
            DropdownMenuItem(
                text = { Text("Share") },
                onClick = onDismiss
            )
        }
    }
}

@Composable
fun MusicContentListItem(
    content: MusicContent,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        onClick = { onItemClick(content.id) }
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // AsyncImage 커버용 Box
            Box(modifier = Modifier
                .size(52.dp)
                .border(width = 0.5.dp, color = Color(0xFF919191).copy(0.4f), shape = RoundedCornerShape(12.dp))
            ){
                AsyncImage(
                    model = content.thumbnailUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                )
            }


            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = content.name,
                    fontSize = 15.sp,
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
//                    PlatformIcon(content.platform)
                    ContentSubtitle(content)
                }
            }

            ContentItemMenu(
                expanded = showMenu,
                onDismiss = { showMenu = false },
                onMenuClick = { showMenu = true }
            )
        }
    }
}

@Composable
private fun ContentSubtitle(content: MusicContent) {
    Text(
        text = with(content) {
            when {
                this is Track -> artists.firstOrNull() ?: ""
                this is Album -> "${artists.firstOrNull()} • $trackCount tracks"
                this is Playlist -> "$owner • $trackCount tracks"
                else -> ""
            }
        },
        fontSize = 12.sp,
        color = Color.Gray,
        maxLines = 1
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheetContent(
    selectedPlatforms: Set<MusicPlatform>,
    onPlatformSelectionChanged: (Set<MusicPlatform>) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(24.dp)
    ) {
        Text(
            "Filter",
            fontSize = 20.sp,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(24.dp))

        FilterSection(
            title = "Platforms",
            content = {
                PlatformFilterChips(
                    selectedPlatforms = selectedPlatforms,
                    onPlatformSelectionChanged = onPlatformSelectionChanged
                )
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        FilterSection(
            title = "Sort by",
            content = {
                SortOptions()
            }
        )
    }
}

@Composable
private fun FilterSection(
    title: String,
    content: @Composable () -> Unit
) {
    Text(
        title,
        fontSize = 16.sp,
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Medium
    )
    Spacer(modifier = Modifier.height(12.dp))
    content()
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PlatformFilterChips(
    selectedPlatforms: Set<MusicPlatform>,
    onPlatformSelectionChanged: (Set<MusicPlatform>) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MusicPlatform.entries.forEach { platform ->
            FilterChip(
                selected = platform in selectedPlatforms,
                onClick = {
                    onPlatformSelectionChanged(
                        if (platform in selectedPlatforms) {
                            selectedPlatforms - platform
                        } else {
                            selectedPlatforms + platform
                        }
                    )
                },
                label = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(
                                id = when(platform) {
                                    MusicPlatform.SPOTIFY -> R.drawable.spotify_logomark
                                    MusicPlatform.APPLE_MUSIC -> R.drawable.applemusic_logomark
                                }
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = when(platform) {
                                MusicPlatform.SPOTIFY -> Color(0xFF1DB954)
                                MusicPlatform.APPLE_MUSIC -> Color(0xFFFC3C44)
                            }
                        )
                        Text(platform.name)
                    }
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFF93C58),
                    selectedLabelColor = Color.White
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = platform in selectedPlatforms,
                    borderWidth = 0.5.dp
                )
            )
        }
    }
}

@Composable
private fun SortOptions() {
    Column {
        listOf("Latest", "Name", "Creator", "Date Added").forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* Handle sort selection */ }
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = option,
                    fontSize = 14.sp,
                    fontFamily = PretendardFontFamily
                )
            }
        }
    }
}

@Composable
fun SortDropdownMenu(
    expanded: Boolean,
    selectedSort: String,
    onSortSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = modifier
    ) {
        listOf("Latest", "Name", "Creator", "Date Added").forEach { option ->
            DropdownMenuItem(
                text = { Text(option) },
                onClick = { onSortSelected(option) },
                trailingIcon = if (selectedSort == option) {
                    {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color(0xFFF93C58)
                        )
                    }
                } else null
            )
        }
    }
}

@Composable
fun WaveTitle(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = "My Library",
            fontSize = 26.sp,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.offset(y = offset.dp)
        )
        Icon(
            imageVector = Icons.Default.MusicNote,
            contentDescription = null,
            tint = Color(0xFFF93C58),
            modifier = Modifier
                .padding(start = 8.dp)
                .offset(y = -offset.dp)
        )
    }
}

fun getDummyContent(contentType: ContentType): List<MusicContent> {
    return when (contentType) {
        ContentType.TRACK -> listOf(
            Track(
                id = "1",
                platformId = "spotify_1",
                platform = MusicPlatform.SPOTIFY,
                name = "Dynamite",
                thumbnailUrl = "https://example.com/dynamite.jpg",
                artists = listOf("BTS"),
                album = "BE",
                durationMs = 199054,
                createdAt = Instant.now().epochSecond,
                updatedAt = Instant.now().epochSecond
            ),
            Track(
                id = "2",
                platformId = "apple_1",
                platform = MusicPlatform.APPLE_MUSIC,
                name = "Butter",
                thumbnailUrl = "https://example.com/butter.jpg",
                artists = listOf("BTS"),
                album = "Butter",
                durationMs = 164000,
                createdAt = Instant.now().epochSecond,
                updatedAt = Instant.now().epochSecond
            ),
            Track(
                id = "3",
                platformId = "spotify_2",
                platform = MusicPlatform.SPOTIFY,
                name = "Spring Day",
                thumbnailUrl = "https://example.com/springday.jpg",
                artists = listOf("BTS"),
                album = "You Never Walk Alone",
                durationMs = 285000,
                createdAt = Instant.now().epochSecond,
                updatedAt = Instant.now().epochSecond
            )
        )
        ContentType.ALBUM -> listOf(
            Album(
                id = "1",
                platformId = "spotify_album_1",
                platform = MusicPlatform.SPOTIFY,
                name = "BE",
                thumbnailUrl = "https://example.com/be_album.jpg",
                artists = listOf("BTS"),
                releaseDate = Instant.now().epochSecond,
                trackCount = 8,
                createdAt = Instant.now().epochSecond,
                updatedAt = Instant.now().epochSecond,
                tracks = listOf()
            ),
            Album(
                id = "2",
                platformId = "apple_album_1",
                platform = MusicPlatform.APPLE_MUSIC,
                name = "Map of the Soul: 7",
                thumbnailUrl = "https://example.com/mots7_album.jpg",
                artists = listOf("BTS"),
                releaseDate = Instant.now().epochSecond,
                trackCount = 20,
                createdAt = Instant.now().epochSecond,
                updatedAt = Instant.now().epochSecond,
                tracks = listOf()
            )
        )
        ContentType.PLAYLIST -> listOf(
            Playlist(
                id = "1",
                platformId = "spotify_playlist_1",
                platform = MusicPlatform.SPOTIFY,
                name = "BTS Hits",
                thumbnailUrl = "https://example.com/playlist1.jpg",
                description = "Best BTS songs",
                trackCount = 50,
                owner = "User123",
                tracks = emptyList(),
                createdAt = Instant.now().epochSecond,
                updatedAt = Instant.now().epochSecond
            ),
            Playlist(
                id = "2",
                platformId = "apple_playlist_1",
                platform = MusicPlatform.APPLE_MUSIC,
                name = "K-pop Favorites",
                thumbnailUrl = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fm.youtube.com%2Fwatch%3Fv%3DXtT0RiON5Q4%26pp%3DygUJI2J0c2FyYnRz&psig=AOvVaw0CJvgXecW3u4LC45ulh1ED&ust=1731863389164000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCOiWjYOs4YkDFQAAAAAdAAAAABAEhttps://www.google.com/url?sa=i&url=https%3A%2F%2Fm.youtube.com%2Fwatch%3Fv%3DXtT0RiON5Q4%26pp%3DygUJI2J0c2FyYnRz&psig=AOvVaw0CJvgXecW3u4LC45ulh1ED&ust=1731863389164000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCOiWjYOs4YkDFQAAAAAdAAAAABAE",
                description = "Popular K-pop songs",
                trackCount = 100,
                owner = "User456",
                tracks = emptyList(),
                createdAt = Instant.now().epochSecond,
                updatedAt = Instant.now().epochSecond
            )
        )
    }
}