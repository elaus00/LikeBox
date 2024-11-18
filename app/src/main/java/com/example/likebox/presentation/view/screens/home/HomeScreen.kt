package com.example.likebox.presentation.view.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import com.example.likebox.R
import com.example.likebox.presentation.view.navigation.LikeboxNavigationBar
import com.example.likebox.presentation.view.theme.PretendardFontFamily
import com.example.likebox.presentation.view.theme.SofiaSans

// 공통으로 사용되는 상수들
private object HomeScreenConstants {
    val DefaultHorizontalPadding = 25.dp
    val DefaultCornerRadius = 18.dp
    val DefaultSpacing = 8.dp
    val BorderWidth = 0.5.dp
    val CardBorderColor = Color.Black.copy(alpha = 0.1f)
    val SubtextColor = Color.Black.copy(alpha = 0.5f)

    val MusicServices = listOf(
        MusicServiceInfo("Spotify", R.drawable.spotif_logotype, SyncStatus.SYNCED),
        MusicServiceInfo("genie", R.drawable.genie_logotype, SyncStatus.SYNCING),
        MusicServiceInfo("Apple Music", R.drawable.apple_music_logotype, SyncStatus.ERROR),
        MusicServiceInfo("TIDAL", R.drawable.tidal_logotype, SyncStatus.WAITING)
    )

    val RecentSongs = listOf(
        SongInfo("WHY, WHY, WHY", "SUMIN, Slom", R.drawable.album_cover_sample_1),
        SongInfo("ENGLAND", "YANG HONG WON", R.drawable.album_cover_sample_2)
    )
}

// 데이터 클래스들
data class MusicServiceInfo(val name: String, val logo: Int, val status: SyncStatus)
data class SongInfo(val title: String, val artist: String, val coverArt: Int)

enum class SyncStatus { SYNCED, SYNCING, ERROR, WAITING }

/**
 * 홈 화면의 메인 컴포넌트입니다.
 * 네비게이션 바와 함께 홈 화면의 전체 레이아웃을 구성합니다.
 *
 * @param navController 화면 전환을 위한 네비게이션 컨트롤러
 */
@Composable
fun HomeScreen(navController: NavController) {
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
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding().minus(10.dp),
                    bottom = paddingValues.calculateBottomPadding(),
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr)
                )
                .padding(horizontal = HomeScreenConstants.DefaultHorizontalPadding)
        ) {
            HomeTopBar()
            Spacer(modifier = Modifier.height(HomeScreenConstants.DefaultSpacing))
            SyncStatusSection()
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.BottomCenter
            ) {
                RecentlySyncedSongs()
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
        modifier = Modifier.fillMaxWidth(),
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
 * 노래 카드 하단의 제목과 아티스트 정보를 오버레이로 표시하는 컴포넌트입니다.
 *
 * @param song 표시할 노래 정보
 */
@Composable
private fun SongCardOverlay(song: SongInfo) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.2f))
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = song.title,
            color = Color.White.copy(alpha = 0.95f),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = song.artist,
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


/**
 * 음악 서비스들의 동기화 상태를 보여주는 섹션입니다.
 * 각 음악 서비스의 현재 동기화 상태를 리스트로 표시합니다.
 */
@Composable
private fun SyncStatusSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader(
            title = "Sync Status",
            subtitle = "Synced 15 minutes ago"
        )
        HomeScreenConstants.MusicServices.forEach { service ->
            MusicServiceItem(service)
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

/**
 * 개별 음악 서비스의 로고와 동기화 상태를 표시하는 아이템 컴포넌트입니다.
 *
 * @param service 표시할 음악 서비스 정보
 */
@Composable
private fun MusicServiceItem(service: MusicServiceInfo) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(HomeScreenConstants.DefaultCornerRadius))
            .background(color = Color(0x4DF8F8F8))
            .border(
                width = HomeScreenConstants.BorderWidth,
                color = Color(0x33767676),
                shape = RoundedCornerShape(HomeScreenConstants.DefaultCornerRadius)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = service.logo),
            contentDescription = service.name,
            modifier = Modifier.width(70.dp)
        )
        SyncStatusIcon(service.status)
    }
}

/**
 * 최근 동기화된 노래들을 가로 스크롤 형태로 표시하는 섹션입니다.
 */
@Composable
private fun RecentlySyncedSongs() {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader(
            title = "Recently Synced Songs",
            subtitle = "view more"
        )
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            repeat(5) { index ->
                val song = HomeScreenConstants.RecentSongs[index % 2]
                SongCard(song)
            }
        }
    }
}

/**
 * 개별 노래의 커버아트와 정보를 카드 형태로 표시하는 컴포넌트입니다.
 *
 * @param song 표시할 노래 정보
 */
@Composable
private fun SongCard(song: SongInfo) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val cardWidth = screenWidth * (191f/390f)  // 디스플레이 대비 카드 너비 비율 계산

    Box(
        modifier = Modifier
            .width(cardWidth)  // 계산된 너비 적용
            .aspectRatio(191f / 265f)
            .clip(RoundedCornerShape(23.dp))
            .border(
                width = HomeScreenConstants.BorderWidth,
                color = HomeScreenConstants.CardBorderColor,
                shape = RoundedCornerShape(23.dp)
            )
            .shadow(elevation = 4.dp, spotColor = Color(0x59000000), ambientColor = Color(0x59000000))
    ) {
        Image(
            painter = painterResource(id = song.coverArt),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        // Box를 사용하여 오버레이를 하단에 배치
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.6f)
                        ),
                        startY = 300f,  // 그라데이션 시작 지점
                        endY = 500f     // 그라데이션 끝 지점
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
                    text = song.title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = song.artist,
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * 각 섹션의 헤더를 표시하는 유틸리티 컴포넌트입니다.
 * 제목과 부제목(또는 액션 텍스트)을 함께 표시합니다.
 *
 * @param title 섹션의 주 제목
 * @param subtitle 섹션의 부제목 또는 액션 텍스트
 */
@Composable
private fun SectionHeader(title: String, subtitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = subtitle,
            fontSize = 12.sp,
            fontFamily = PretendardFontFamily,
            color = HomeScreenConstants.SubtextColor
        )
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
        SyncStatus.SYNCED -> Icon(
            painter = painterResource(id = R.drawable.complete),
            contentDescription = "Synced",
            modifier = Modifier.size(20.dp),
            tint = Color(0xFF00BC74) // #00BC74
        )
        SyncStatus.SYNCING -> {
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
        SyncStatus.WAITING -> Icon(
            painter = painterResource(id = R.drawable.pause),
            contentDescription = "Waiting",
            modifier = Modifier.size(20.dp),
            tint = Color(0xFF444444).copy(alpha = 0.9f) // #444444 with 90% opacity
        )
    }
}