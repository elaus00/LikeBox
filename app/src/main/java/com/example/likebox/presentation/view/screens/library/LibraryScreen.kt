import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.likebox.domain.model.*
import com.example.likebox.presentation.view.navigation.LikeboxNavigationBar
import com.example.likebox.presentation.view.screens.components.getDummyContent
import com.example.likebox.presentation.view.theme.PretendardFontFamily

@Composable
fun LibraryScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onNavigateToPlaylist: (String) -> Unit,
    onNavigateToAlbum: (String) -> Unit,
    onNavigateToArtist: (String) -> Unit
) {
    Scaffold(
        bottomBar = {
            Surface(
                shadowElevation = 4.dp,
                tonalElevation = 4.dp,
                color = Color.Transparent
            ) {
                LikeboxNavigationBar(navController)
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        var selectedContentType by remember { mutableStateOf(ContentType.TRACK) }
        var selectedPlatforms by remember { mutableStateOf(setOf(MusicPlatform.SPOTIFY)) }
        var expandedSortMenu by remember { mutableStateOf(false) }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding().minus(10.dp),
                    bottom = paddingValues.calculateBottomPadding(),
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr)
                )
                .background(Color.White)
                .padding(top = 20.dp)
        ) {
            // Library Title
            Text(
                text = "Library",
                fontSize = 28.sp,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            // Platform Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MusicPlatform.entries.forEach { platform ->
                    PlatformFilterChip(
                        platform = platform,
                        isSelected = platform in selectedPlatforms,
                        onSelected = {
                            selectedPlatforms = if (platform in selectedPlatforms) {
                                selectedPlatforms - platform
                            } else {
                                selectedPlatforms + platform
                            }
                        }
                    )
                }
            }

            // Content Type & Sort Options Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    ContentType.entries.forEach { contentType ->
                        ContentTypeChip(
                            type = contentType,
                            isSelected = selectedContentType == contentType,
                            onSelected = { selectedContentType = contentType }
                        )
                    }
                }

                IconButton(onClick = { expandedSortMenu = true }) {
                    Icon(
                        imageVector = Icons.Default.Sort,
                        contentDescription = "Sort",
                        tint = Color(0xFF171A1F)
                    )
                }

                DropdownMenu(
                    expanded = expandedSortMenu,
                    onDismissRequest = { expandedSortMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Latest") },
                        onClick = { expandedSortMenu = false }
                    )
                    DropdownMenuItem(
                        text = { Text("Name") },
                        onClick = { expandedSortMenu = false }
                    )
                }
            }

            // Content List by Platform
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .weight(1f), // Add weight to make sure the content fills available space
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                selectedPlatforms.forEach { platform ->
                    item {
                        PlatformSection(platform = platform)
                    }

                    items(getDummyContent(selectedContentType)) { content ->
                        ContentListItem(
                            content = content,
                            onItemClick = { id ->
                                when (content) {
                                    is Playlist -> onNavigateToPlaylist(id)
                                    is Album -> onNavigateToAlbum(id)
                                    is Track -> Unit // Handle track click
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun PlatformFilterChip(
    platform: MusicPlatform,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    Surface(
        modifier = Modifier
            .border(
                width = 0.69.dp,
                color = Color.Black.copy(alpha = 0.2f),
                shape = RoundedCornerShape(18.87.dp)
            ),
        shape = RoundedCornerShape(18.87.dp),
        color = if (isSelected) Color(0xFFB0B0B0).copy(alpha = 0.6f) else Color.White,
        onClick = onSelected
    ) {
        Text(
            text = platform.name,
            fontSize = 10.32.sp,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black.copy(alpha = 0.75f),
            modifier = Modifier.padding(horizontal = 6.88.dp, vertical = 6.88.dp)
        )
    }
}

@Composable
private fun ContentTypeChip(
    type: ContentType,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = onSelected,
        label = {
            Text(
                text = type.name.lowercase().capitalize(),
                fontSize = 14.sp,
                fontFamily = PretendardFontFamily
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color(0xFFF8F9FA),
            selectedLabelColor = Color(0xFF171A1F)
        )
    )
}

@Composable
private fun PlatformSection(platform: MusicPlatform) {
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
                    color = when (platform) {
                        MusicPlatform.SPOTIFY -> Color(0xFF1ED760)
                        MusicPlatform.APPLE_MUSIC -> Color(0xFFFC3C44)
                        else -> Color.Gray
                    },
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

@Composable
private fun ContentListItem(
    content: MusicContent,
    onItemClick: (String) -> Unit
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

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(9.08.dp)
                ) {
                    Text(
                        text = when(content) {
                            is Track -> "Song"
                            is Album -> "Album"
                            is Playlist -> "Playlist"
                            else -> ""
                        },
                        fontSize = 10.sp,
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Medium
                    )
                    Box(
                        modifier = Modifier
                            .size(2.27.dp)
                            .background(
                                Color.Black.copy(alpha = 0.8f),
                                CircleShape
                            )
                    )
                    Text(
                        text = when(content) {
                            is Track -> content.artists.firstOrNull() ?: ""
                            is Album -> content.artists.firstOrNull() ?: ""
                            is Playlist -> content.owner
                            else -> ""
                        },
                        fontSize = 10.sp,
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        IconButton(
            onClick = { onItemClick(content.id) }
        ) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Navigate",
                modifier = Modifier.size(15.dp),
                tint = Color.Black.copy(alpha = 0.8f)
            )
        }
    }
}