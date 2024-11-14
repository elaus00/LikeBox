package com.example.likebox.presentation.view.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.likebox.R
import com.example.likebox.presentation.view.navigation.AppNavigationBar
import com.example.likebox.presentation.view.theme.PretendardFontFamily
import com.example.likebox.presentation.view.theme.SofiaSans
import com.example.likebox.presentation.view.theme.TopBar

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Scaffold(
            bottomBar = {
                Surface(
                    shadowElevation = 4.dp,
                    tonalElevation = 4.dp,
                    color = Color.Transparent
                ) {
                    AppNavigationBar()
                }
            },
            modifier = Modifier.systemBarsPadding(),
            containerColor = Color.White
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(25.dp)
                ) {
                    HomeTopBar()
                    Spacer(modifier = Modifier.height(8.dp))
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
    }
}

@Composable
fun HomeTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(Color(0xFFF93C58), RoundedCornerShape(3.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "L",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontFamily = SofiaSans,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "LIKEBOX",
                fontSize = 20.sp,
                fontFamily = SofiaSans,
                fontWeight = FontWeight.ExtraBold
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.notification_icon),
            contentDescription = "Notifications",
            modifier = Modifier.size(24.dp),
            tint = Color.Black.copy(alpha = 0.8f)
        )
    }
}

@Composable
fun SyncStatusSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Sync Status",
                fontSize = 16.sp,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Synced 15 minutes ago",
                fontSize = 10.sp,
                fontFamily = PretendardFontFamily,
                color = Color.Black.copy(alpha = 0.5f)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        MusicServiceItem(
            serviceName = "Spotify",
            logo = R.drawable.spotif_logotype,
            syncStatus = SyncStatus.SYNCED
        )
        Spacer(modifier = Modifier.height(6.dp))
        MusicServiceItem(
            serviceName = "genie",
            logo = R.drawable.genie_logo,
            syncStatus = SyncStatus.SYNCING
        )
        Spacer(modifier = Modifier.height(6.dp))
        MusicServiceItem(
            serviceName = "Apple Music",
            logo = R.drawable.apple_music_logo,
            syncStatus = SyncStatus.ERROR
        )
        Spacer(modifier = Modifier.height(6.dp))
        MusicServiceItem(
            serviceName = "TIDAL",
            logo = R.drawable.tidal_logotype,
            syncStatus = SyncStatus.WAITING
        )
    }
}

enum class SyncStatus {
    SYNCED, SYNCING, ERROR, WAITING
}

@Composable
fun MusicServiceItem(
    serviceName: String,
    logo: Int,
    syncStatus: SyncStatus
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(color = Color(0x4DF8F8F8))
            .border(
                width = 0.5.dp,
                color = Color(0x33767676),
                shape = RoundedCornerShape(size = 18.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = logo),
            contentDescription = serviceName,
            modifier = Modifier.width(70.dp)
        )

        when (syncStatus) {
            SyncStatus.SYNCED -> Icon(
                painter = painterResource(id = R.drawable.complete),
                contentDescription = "Synced",
                modifier = Modifier.size(20.dp)
            )
            SyncStatus.SYNCING -> CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp
            )
            SyncStatus.ERROR -> Icon(
                painter = painterResource(id = R.drawable.fail),
                contentDescription = "Error",
                modifier = Modifier.size(20.dp)
            )
            SyncStatus.WAITING -> Icon(
                painter = painterResource(id = R.drawable.pause),
                contentDescription = "Waiting",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun RecentlySyncedSongs() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recently Synced Songs",
                fontSize = 16.sp,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "view more",
                fontSize = 10.sp,
                fontFamily = PretendardFontFamily,
                color = Color.Black.copy(alpha = 0.5f)
            )
        }
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            repeat(5) { // 테스트를 위해 여러 카드 추가
                SongCard(
                    songTitle = if (it % 2 == 0) "WHY, WHY, WHY" else "ENGLAND",
                    artist = if (it % 2 == 0) "SUMIN, Slom" else "YANG HONG WON",
                    coverArt = if (it % 2 == 0) R.drawable.album_cover_sample_1 else R.drawable.album_cover_sample_2
                )
            }
        }
    }
}

@Composable
fun SongCard(
    songTitle: String,
    artist: String,
    coverArt: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(160f / 265f) // 원래 비율 유지
            .clip(RoundedCornerShape(23.dp))
            .border(
                width = 0.5.dp,
                color = Color.Black.copy(alpha = 0.1f),
                shape = RoundedCornerShape(23.dp)
            )
            .shadow(elevation = 4.dp, spotColor = Color(0x59000000), ambientColor = Color(0x59000000))

    ) {
        Image(
            painter = painterResource(id = coverArt),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.2f))
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = songTitle,
                color = Color.White.copy(alpha = 0.95f),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = artist,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun HomeScreenPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        HomeScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun SongCardPreview() {
    SongCard(
        songTitle = "WHY, WHY, WHY",
        artist = "SUMIN, Slom",
        coverArt = R.drawable.album_cover_sample_1
    )
}

@Preview(showBackground = true)
@Composable
fun MusicServiceItemPreview() {
    MusicServiceItem(
        serviceName = "Spotify",
        logo = R.drawable.spotif_logotype,
        syncStatus = SyncStatus.SYNCED
    )
}

@Preview(showBackground = true)
@Composable
fun SyncStatusSectionPreview() {
    SyncStatusSection()
}