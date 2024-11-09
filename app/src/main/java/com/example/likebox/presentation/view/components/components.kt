package com.example.likebox.presentation.view.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text  // Text composable import 추가
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// Track 데이터 클래스 정의 (androidx.media3.extractor.mp4.Track 대신 사용)
data class Track(
    val title: String,
    val artist: String,
    val id: String,
    val platform: String
)

@Composable
fun TrackItem(
    track: Track,
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = { Text(track.title) },
        supportingContent = { Text(track.artist) },
        leadingContent = {
            Icon(
                imageVector = Icons.Default.MusicNote,
                contentDescription = null
            )
        },
        trailingContent = {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "더보기"
            )
        },
        modifier = modifier
    )
}