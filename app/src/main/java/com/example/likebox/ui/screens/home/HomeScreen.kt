package com.example.likebox.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.likebox.R

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopBar()
        SyncStatusSection()
        RecentlySyncedSongs()
        BottomNavigationBar()
    }
}

@Composable
private fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp, vertical = 16.dp),
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
                    fontSize = 23.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "LIKEBOX",
                fontSize = 23.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
        // To-do : 아이콘 수정해야함. 지금은 임포트 문제로 잠깐 임시처리
        Icon(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            tint = Color(0xCC1D1B20)
        )
    }
}

@Composable
private fun SyncStatusSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Sync Status",
                fontSize = 16.5.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Synced 15 minutes ago",
                fontSize = 10.sp,
                color = Color.Black.copy(alpha = 0.5f)
            )
        }

        ServiceSyncItem(
            serviceName = "Spotify",
            backgroundColor = Color(0x4CF7F7F7),
            logoBackground = Color(0xFF1ED760)
        )
        Spacer(modifier = Modifier.height(6.dp))
        ServiceSyncItem(
            serviceName = "Apple Music",
            backgroundColor = Color(0x4CF7F7F7),
            logoBackground = Color(0xFF0096FF)
        )
        // Add other service items similarly
    }
}

@Composable
private fun ServiceSyncItem(
    serviceName: String,
    backgroundColor: Color,
    logoBackground: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(backgroundColor, RoundedCornerShape(18.dp))
            .border(
                width = 0.5.dp,
                color = Color.Black.copy(alpha = 0.2f),
                shape = RoundedCornerShape(18.dp)
            )
            .padding(horizontal = 15.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(70.dp)
                .height(21.dp)
                .background(logoBackground)
        )
        Icon(
            painter = painterResource(id = R.drawable.sync),
            contentDescription = null,
            tint = logoBackground,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun RecentlySyncedSongs() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recently Synced Songs",
                fontSize = 16.5.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "view more",
                fontSize = 10.sp,
                color = Color.Black.copy(alpha = 0.5f)
            )
        }

        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SongCard(
                songTitle = "WHY, WHY, WHY",
                artist = "SUMIN, Slom",
                cover = R.drawable.album_cover_sample_1
            )
            SongCard(
                songTitle = "ENGLAND",
                artist = "YANG HONG WON",
                cover = R.drawable.album_cover_sample_2
            )
        }
    }
}

@Composable
private fun SongCard(
    songTitle: String,
    artist: String,
    cover: Int
) {
    Box(
        modifier = Modifier
            .width(191.dp)
            .height(265.dp)
            .clip(RoundedCornerShape(23.dp))
            .border(
                width = 0.77.dp,
                color = Color.Black,
                shape = RoundedCornerShape(23.dp)
            )
    ) {
        Image(
            painter = painterResource(id = cover),
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

@Composable
private fun BottomNavigationBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        BottomNavItem(
            icon = R.drawable.house,
            isSelected = true
        )
        BottomNavItem(
            icon = R.drawable.search_icon,
            isSelected = false
        )
        BottomNavItem(
            icon = R.drawable.inbox,
            isSelected = false
        )
        BottomNavItem(
            icon = R.drawable.settings,
            isSelected = false
        )
    }
}

@Composable
private fun BottomNavItem(
    icon: Int,
    isSelected: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = if (isSelected) Color.Black else Color.Black.copy(alpha = 0.4f),
            modifier = Modifier.size(24.dp)
        )
        if (isSelected) {
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .background(Color(0xFFF93C58), CircleShape)
            )
        }
    }
}

@Preview
@Composable
fun PreviewHomeScreen(){
    HomeScreen()
}