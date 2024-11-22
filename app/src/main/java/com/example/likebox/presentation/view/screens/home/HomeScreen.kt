package com.example.likebox.presentation.view.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.likebox.R
import com.example.likebox.domain.model.library.Album
import com.example.likebox.domain.model.library.ContentType
import com.example.likebox.domain.model.library.MusicContent
import com.example.likebox.domain.model.library.MusicPlatform
import com.example.likebox.domain.model.library.PlatformState
import com.example.likebox.domain.model.library.Playlist
import com.example.likebox.domain.model.library.Track
import com.example.likebox.presentation.view.screens.auth.state.SyncStatus
import com.example.likebox.presentation.view.navigation.LikeboxNavigationBar
import com.example.likebox.presentation.view.screens.Screens
import com.example.likebox.presentation.view.screens.auth.CustomSnackbar
import com.example.likebox.presentation.view.theme.SofiaSans
import com.example.likebox.presentation.view.theme.TextStyle
import com.example.likebox.presentation.view.theme.TextStyle.heading2
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short
            )
        }
    }


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
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                CustomSnackbar(snackbarData = snackbarData)
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(
                    top = maxOf(paddingValues.calculateTopPadding().minus(10.dp), 0.dp),
                    bottom = paddingValues.calculateBottomPadding(),
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr)
                )
                .padding(horizontal = HomeTheme.HomeScreenConstants.DefaultHorizontalPadding)
        ) {
            HomeTopBar()
            Spacer(modifier = Modifier.height(HomeTheme.HomeScreenConstants.DefaultSpacing))

            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                SyncStatusSection(
                    uiState = uiState,
                    onSyncClick = viewModel::syncContent
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    RecentlySyncedContents(
                        contents = uiState.recentContents,
                        onContentClick = { content ->
                            handleContentNavigation(content, navController)
                        },
                        onViewMoreClick = {
                            navController.navigateToLibrary()
                        }
                    )
                }
            }
        }
    }
}

private fun NavController.navigateToLibrary() {
    navigate(Screens.Main.Library.Root.route) {
        launchSingleTop = true
        popUpTo(Screens.Main.Home.Root.route) {
            saveState = true
        }
    }
}

private fun handleContentNavigation(content: MusicContent, navController: NavController) {
    when (content) {
        is Track -> {} // Track은 별도 처리 필요 없음
        is Album -> navController.navigate(
            Screens.Main.Library.Details.AlbumDetail.route.replace(
                "{albumId}", content.id
            )
        ) {
            launchSingleTop = true
            popUpTo(Screens.Main.Home.Root.route) {
                saveState = true
            }
        }
        is Playlist -> navController.navigate(
            Screens.Main.Library.Details.PlaylistDetail.route.replace(
                "{playlistId}", content.id
            )
        ) {
            launchSingleTop = true
            popUpTo(Screens.Main.Home.Root.route) {
                saveState = true
            }
        }
        else -> {}
    }
}

@Composable
private fun HomeTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LogoSection()
        NotificationIcon()
    }
}

@Composable
private fun LogoSection() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.alpha(0.9f)
    ) {
        Image(
            painter = painterResource(id = R.drawable.main_logo),
            contentDescription = "Logo",
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "LIKEBOX",
            fontSize = 20.sp,
            fontFamily = SofiaSans,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
private fun NotificationIcon() {
    Icon(
        Icons.Outlined.Notifications,
        contentDescription = "notification icon"
    )
}

@Composable
private fun SyncStatusSection(
    uiState: HomeUiState,
    onSyncClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SyncStatusHeader(
            lastSyncTime = uiState.lastSyncTime,
            isLoading = uiState.isLoading,
            onSyncClick = onSyncClick
        )

        uiState.platformStates.entries
            .sortedBy { (platform, state) ->
                if (state.isEnabled) 0 else 1
            }
            .forEach { (platform, state) ->
                PlatformSyncItem(
                    platform = platform,
                    state = state
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
    }
}

@Composable
private fun SyncStatusHeader(
    lastSyncTime: LocalDateTime?,
    isLoading: Boolean,
    onSyncClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = "Sync Status",
            style = heading2
        )

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = lastSyncTime?.let {
                    "Synced ${formatLastSyncTime(it)}"
                } ?: "Not synced yet",
                style = TextStyle.body1.copy(
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            )
            IconButton(
                onClick = onSyncClick,
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Color(0xFF444444).copy(0.78f)
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.sync),
                        contentDescription = "Sync",
                        tint = Color(0xFF444444).copy(0.78f),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun PlatformSyncItem(
    platform: MusicPlatform,
    state: PlatformState
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                color = if (state.isEnabled) {
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                } else {
                    MaterialTheme.colorScheme.surface.copy(alpha = 0.1f)
                }
            )
            .border(
                width = 1.dp,
                color = if (state.isEnabled) {
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                } else {
                    Color.Transparent
                },
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(
                    id = when(platform) {
                        MusicPlatform.SPOTIFY -> R.drawable.spotif_logotype
                        MusicPlatform.APPLE_MUSIC -> R.drawable.apple_music_logotype
                        MusicPlatform.YOUTUBE_MUSIC -> R.drawable.youtube_music_logotype
                        MusicPlatform.MELON -> R.drawable.melon_logotype
                        MusicPlatform.GENIE -> R.drawable.genie_logotype
                        MusicPlatform.FLOO -> R.drawable.flo_logotype
                        MusicPlatform.TIDAL -> R.drawable.tidal_logotype
                        MusicPlatform.AMAZON_MUSIC -> R.drawable.amazon_music_logo
                    }
                ),
                contentDescription = platform.name,
                modifier = Modifier.width(70.dp),
                alpha = if (state.isEnabled) 1f else 0.5f
            )

            if (!state.isEnabled) {
                Text(
                    text = "Coming soon",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 10.sp
                    )
                )
            } else {
                state.lastSyncTime?.let { timestamp ->
                    Text(
                        text = formatTimestamp(timestamp),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 10.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun SyncStatusIcon(status: SyncStatus) {
    when (status) {
        SyncStatus.COMPLETED -> Icon(
            painter = painterResource(id = R.drawable.complete),
            contentDescription = "Synced",
            modifier = Modifier.size(20.dp),
            tint = Color(0xFF00BC74)
        )
        SyncStatus.IN_PROGRESS -> {
            val mainColor = Color(0xFF0096FF)
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = mainColor,
                trackColor = mainColor.copy(alpha = 0.25f)
            )
        }
        SyncStatus.ERROR -> Icon(
            painter = painterResource(id = R.drawable.fail),
            contentDescription = "Error",
            modifier = Modifier.size(20.dp),
            tint = Color(0xFFE50000).copy(alpha = 0.8f)
        )
        SyncStatus.IDLE -> Icon(
            painter = painterResource(id = R.drawable.pause),
            contentDescription = "Waiting",
            modifier = Modifier.size(20.dp),
            tint = Color(0xFF444444).copy(alpha = 0.9f)
        )
        SyncStatus.NOT_SYNCED -> Icon(
            painter = painterResource(id = R.drawable.pause),
            contentDescription = "Waiting",
            modifier = Modifier.size(20.dp),
            tint = Color(0xFF444444).copy(alpha = 0.5f)
        )
    }
}

@Composable
private fun RecentlySyncedContents(
    contents: List<MusicContent>,
    onContentClick: (MusicContent) -> Unit,
    onViewMoreClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recently Synced Contents",
                style = heading2
            )
            Text(
                text = "view more",
                style = TextStyle.body1.copy(
                    color = Color.Gray,
                    fontSize = 12.sp
                ),
                modifier = Modifier.clickable(onClick = onViewMoreClick)
            )
        }

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            contents.forEach { content ->
                MusicContentCard(
                    content = content,
                    onClick = { onContentClick(content) }
                )
            }
        }
    }
}

@Composable
private fun MusicContentCard(
    content: MusicContent,
    onClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val cardWidth = screenWidth * (191f/390f)

    Box(
        modifier = Modifier
            .width(cardWidth)
            .aspectRatio(191f / 265f)
            .clip(HomeTheme.cardShape)
            .border(
                width = HomeTheme.cardBorderWidth,
                color = HomeTheme.HomeScreenConstants.CardBorderColor,
                shape = HomeTheme.cardShape
            )
            .shadow(
                elevation = 4.dp,
                spotColor = Color(0x59000000),
                ambientColor = Color(0x59000000)
            )
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = if (content.thumbnailUrl.toIntOrNull() != null) {
                content.thumbnailUrl.toInt()
            } else {
                content.thumbnailUrl
            },
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        ContentTypeLabel(
            contentType = when(content) {
                is Track -> ContentType.TRACK
                is Album -> ContentType.ALBUM
                else -> ContentType.PLAYLIST
            },
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.TopEnd)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = HomeTheme.gradientAlpha)
                        ),
                        startY = 300f,
                        endY = 500f
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = content.name,
                    style = TextStyle.body1.copy(
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = when(content) {
                        is Track -> content.artists.joinToString(", ")
                        is Album -> content.artists.firstOrNull() ?: ""
                        is Playlist -> "${content.owner} • ${content.trackCount} tracks"
                        else -> ""
                    },
                    style = TextStyle.body1.copy(
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

private fun formatLastSyncTime(time: LocalDateTime): String {
    val now = LocalDateTime.now()
    val minutes = ChronoUnit.MINUTES.between(time, now)

    return when {
        minutes < 1 -> "just now"
        minutes < 60 -> "$minutes minutes ago"
        minutes < 1440 -> "${minutes / 60} hours ago"
        else -> "${minutes / 1440} days ago"
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    return when {
        diff < 60_000 -> "Just now" // 1분 이내
        diff < 3600_000 -> "${diff / 60_000}m ago" // 1시간 이내
        diff < 86400_000 -> "${diff / 3600_000}h ago" // 24시간 이내
        else -> SimpleDateFormat("MM.dd HH:mm", Locale.getDefault())
            .format(Date(timestamp))
    }
}


