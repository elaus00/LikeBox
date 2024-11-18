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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.likebox.domain.model.library.Playlist
import com.example.likebox.domain.model.library.Track
import com.example.likebox.presentation.state.HomeUiState
import com.example.likebox.presentation.state.SyncStatus
import com.example.likebox.presentation.view.navigation.LikeboxNavigationBar
import com.example.likebox.presentation.view.screens.Screens
import com.example.likebox.presentation.view.theme.SofiaSans
import com.example.likebox.presentation.view.theme.TextStyle
import com.example.likebox.presentation.view.theme.TextStyle.heading2
import com.example.likebox.presentation.viewmodel.HomeViewModel
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 * 홈 화면의 메인 컴포넌트입니다.
 * 네비게이션 바와 함께 홈 화면의 전체 레이아웃을 구성합니다.
 *
 * @param navController 화면 전환을 위한 네비게이션 컨트롤러
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

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
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues
                        .calculateTopPadding()
                        .minus(10.dp),
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
                            when (content) {
                                is Track -> {} // Track은 별도 처리 필요 없음
                                is Album -> navController.navigate(
                                    Screens.Main.Library.Details.AlbumDetail.route.replace(
                                        "{albumId}", content.id
                                    )
                                ) {
                                    // 중복된 화면 쌓이는 것 방지
                                    launchSingleTop = true
                                    // Home을 백스택에 유지
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
                        },
                        onViewMoreClick = {
                            navController.navigate(Screens.Main.Library.Root.route) {
                                launchSingleTop = true
                                popUpTo(Screens.Main.Home.Root.route) {
                                    saveState = true
                                }
                            }
                        }
                    )
                }
            }

            // Error handling
            uiState.error?.let { error ->
                Snackbar(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(error)
                }
            }
        }
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
                content.thumbnailUrl.toInt()  // ResourceId를 직접 전달
            } else {
                content.thumbnailUrl
            },
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Content Type Label
        ContentTypeLabel(
            contentType = when {
                content is Track -> ContentType.TRACK
                content is Album -> ContentType.ALBUM
                else -> ContentType.PLAYLIST
            },
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.TopEnd)
        )

        // Gradient and Content Info
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
                    )
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
                    )
                )
            }
        }
    }
}

/**
 * 홈 화면 상단의 앱 로고와 알림 아이콘을 포함하는 상단바입니다.
 */
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

/**
 * 앱의 로고('L')와 앱 이름('LIKEBOX')을 표시하는 컴포넌트입니다.
 */
@Composable
private fun LogoSection() {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .alpha(0.9f)
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

/**
 * 상단 바 우측의 알림 아이콘을 표시하는 컴포넌트입니다.
 */
@Composable
private fun NotificationIcon() {
    Icon(
        Icons.Outlined.Notifications,
        contentDescription = "notification icon"
    )
}


/**
 * 음악 서비스들의 동기화 상태를 보여주는 섹션입니다.
 * 각 음악 서비스의 현재 동기화 상태를 리스트로 표시합니다.
 */
@Composable
private fun SyncStatusSection(
    uiState: HomeUiState,
    onSyncClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
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
                    text = uiState.lastSyncTime?.let {
                        "Synced ${formatLastSyncTime(it)}"
                    } ?: "Not synced yet",
                    style = TextStyle.body1.copy(
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                )
                IconButton(
                    onClick = onSyncClick,
                    enabled = !uiState.isLoading
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.sync),
                        contentDescription = "Sync",
                        tint = Color(0xFF444444).copy(0.78f),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        uiState.platformStatuses.forEach { (platform, status) ->
            MusicServiceItem(
                platform = platform,
                syncStatus = status.syncStatus
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

/**
 * 시간 포맷팅 유틸리티 함수
 */
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

/**
 * 개별 음악 서비스의 로고와 동기화 상태를 표시하는 아이템 컴포넌트입니다.
 *
 * @param service 표시할 음악 서비스 정보
 */
@Composable
private fun MusicServiceItem(
    platform: MusicPlatform,
    syncStatus: SyncStatus
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(HomeTheme.HomeScreenConstants.DefaultCornerRadius))
            .background(color = Color(0x4DF8F8F8))
            .border(
                width = HomeTheme.HomeScreenConstants.BorderWidth,
                color = Color(0x33767676),
                shape = RoundedCornerShape(HomeTheme.HomeScreenConstants.DefaultCornerRadius)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(
                id = when(platform) {
                    MusicPlatform.SPOTIFY -> R.drawable.spotif_logotype
                    MusicPlatform.APPLE_MUSIC -> R.drawable.apple_music_logotype
                }
            ),
            contentDescription = platform.name,
            modifier = Modifier.width(70.dp)
        )
        SyncStatusIcon(syncStatus)
    }
}

/**
 * 동기화 상태에 따른 아이콘을 표시하는 유틸리티 컴포넌트입니다.
 * SYNCED, SYNCING, ERROR, WAITING 상태에 따라 다른 아이콘을 표시합니다.
 *
 * @param status 표시할 동기화 상태
 */
@Composable
private fun SyncStatusIcon(status: SyncStatus) {
    when (status) {
        SyncStatus.COMPLETED -> Icon(
            painter = painterResource(id = R.drawable.complete),
            contentDescription = "Synced",
            modifier = Modifier.size(20.dp),
            tint = Color(0xFF00BC74) // #00BC74
        )
        SyncStatus.IN_PROGRESS -> {
            val mainColor = Color(0xFF0096FF) // #0096FF
            val subColor = mainColor.copy(alpha = 0.25f)
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = mainColor,
                trackColor = subColor
            )
        }
        SyncStatus.ERROR -> Icon(
            painter = painterResource(id = R.drawable.fail),
            contentDescription = "Error",
            modifier = Modifier.size(20.dp),
            tint = Color(0xFFE50000).copy(alpha = 0.8f) // #E50000 with 80% opacity
        )
        SyncStatus.IDLE -> Icon(
            painter = painterResource(id = R.drawable.pause),
            contentDescription = "Waiting",
            modifier = Modifier.size(20.dp),
            tint = Color(0xFF444444).copy(alpha = 0.9f) // #444444 with 90% opacity
        )
    }
}