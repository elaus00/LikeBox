import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Check
import coil.compose.AsyncImage
import com.example.likebox.R
import com.example.likebox.domain.model.*
import com.example.likebox.presentation.view.navigation.LikeboxNavigationBar
import com.example.likebox.presentation.view.screens.components.getDummyContent
import com.example.likebox.presentation.view.screens.library.LibraryComponents
import com.example.likebox.presentation.view.theme.PretendardFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onNavigateToPlaylist: (String) -> Unit,
    onNavigateToAlbum: (String) -> Unit,
    onNavigateToArtist: (String) -> Unit
) {
    var selectedContentType by remember { mutableStateOf(ContentType.TRACK) }
    var selectedPlatforms by remember { mutableStateOf(setOf<MusicPlatform>()) }
    var showFilterSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.background(Color.White),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                // Top section with title and filter
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Library",
                        fontSize = 28.sp,
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.SemiBold
                    )

                    IconButton(onClick = { showFilterSheet = true }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filter",
                            tint = Color.Black
                        )
                    }
                }

                // Content Type Selector
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                ) {
                    ContentType.entries.forEachIndexed { index, type ->
                        SegmentedButton(
                            selected = selectedContentType == type,
                            onClick = { selectedContentType = type },
                            shape = SegmentedButtonDefaults.itemShape(index = index, count = 3),
                            colors = SegmentedButtonDefaults.colors(
                                activeContainerColor = Color(0xFFF93C58),
                                activeBorderColor = Color(0xFFF93C58),
                                activeContentColor = Color.White,
                                inactiveContentColor = Color(0xFF171A1F)
                            )
                        ) {
                            Text(
                                text = type.name.lowercase().capitalize(),
                                fontFamily = PretendardFontFamily,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                        }
                    }
                }
            }
        },
        containerColor = Color.White,
        bottomBar = {
            Surface(
                shadowElevation = 4.dp,
                tonalElevation = 4.dp,
                color = Color.Transparent
            ) {
                LikeboxNavigationBar(navController)
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(
                horizontal = 24.dp,
                vertical = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                getDummyContent(selectedContentType)
                    .filter { selectedPlatforms.isEmpty() || it.platform in selectedPlatforms }
            ) { content ->
                MusicContentItem(
                    content = content,
                    onItemClick = { id ->
                        when (content) {
                            is Playlist -> onNavigateToPlaylist(id)
                            is Album -> onNavigateToAlbum(id)
                            is Track -> Unit
                        }
                    }
                )
            }
        }

        if (showFilterSheet) {
            FilterBottomSheet(
                selectedPlatforms = selectedPlatforms,
                onPlatformSelectionChanged = { selectedPlatforms = it },
                onDismiss = { showFilterSheet = false }
            )
        }
    }
}

@Composable
private fun MusicContentItem(
    content: MusicContent,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        onClick = { onItemClick(content.id) },
        border = BorderStroke(
            width = 1.dp,
            color = Color.Black.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Cover Image
            AsyncImage(
                model = content.thumbnailUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            // Content Info
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = content.name,
                    fontSize = 16.sp,
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Content Metadata
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Platform Icon
                    Surface(
                        shape = CircleShape,
                        color = when(content.platform) {
                            MusicPlatform.SPOTIFY -> Color(0xFF1DB954)
                            MusicPlatform.APPLE_MUSIC -> Color(0xFFFC3C44)
                        },
                        modifier = Modifier.size(16.dp)
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
                                .padding(3.dp)
                                .size(10.dp)
                        )
                    }

                    Text(
                        text = when {
                            content is Track -> content.artists.firstOrNull() ?: ""
                            content is Album -> "${content.artists.firstOrNull()} • ${content.trackCount} tracks"
                            content is Playlist -> "${content.owner} • ${content.trackCount} tracks"
                            else -> "" // 다른 타입이 추가될 경우를 대비
                        },
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 1
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun FilterBottomSheet(
    selectedPlatforms: Set<MusicPlatform>,
    onPlatformSelectionChanged: (Set<MusicPlatform>) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                "Filter",
                fontSize = 20.sp,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Platforms",
                fontSize = 16.sp,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Platform Selection
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
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(platform.name)
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFFF93C58),
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sort Options
            Text(
                "Sort by",
                fontSize = 16.sp,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(12.dp))

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
    }
}