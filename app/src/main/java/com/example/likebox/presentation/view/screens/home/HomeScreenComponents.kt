package com.example.likebox.presentation.view.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.likebox.R
import com.example.likebox.domain.model.*
import com.example.likebox.domain.model.library.Album
import com.example.likebox.domain.model.library.ContentType
import com.example.likebox.domain.model.library.MusicContent
import com.example.likebox.domain.model.library.MusicPlatform
import com.example.likebox.domain.model.library.Playlist
import com.example.likebox.domain.model.library.Track
import com.example.likebox.presentation.view.theme.PretendardFontFamily
import com.example.likebox.presentation.view.theme.TextStyle

object HomeTheme {
    val cardShape = RoundedCornerShape(23.dp)
    val contentTypeBackgroundAlpha = 0.7f
    val gradientAlpha = 0.6f
    val cardBorderWidth = 0.5.dp
    val defaultSpacing = 16.dp

    // Content Type Label Colors
    object ContentTypeColors {
        val track = Color(0xFF1DB954)      // Spotify Green
        val album = Color(0xFFF93C58)      // LikeBox Pink
        val playlist = Color(0xFF0096FF)    // Bright Blue
    }

    // 공통으로 사용되는 상수들
    object HomeScreenConstants {
        val DefaultHorizontalPadding = 25.dp
        val DefaultCornerRadius = 18.dp
        val DefaultSpacing = 8.dp
        val BorderWidth = 0.5.dp
        val CardBorderColor = Color.Black.copy(alpha = 0.1f)
        val SubtextColor = Color.Black.copy(alpha = 0.5f)

    }
}

/**
 * 콘텐츠 타입을 나타내는 라벨 컴포넌트
 */
@Composable
fun ContentTypeLabel(
    contentType: ContentType,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when(contentType) {
        ContentType.TRACK -> HomeTheme.ContentTypeColors.track
        ContentType.ALBUM -> HomeTheme.ContentTypeColors.album
        ContentType.PLAYLIST -> HomeTheme.ContentTypeColors.playlist
        ContentType.ARTIST -> Color.Transparent
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor.copy(alpha = HomeTheme.contentTypeBackgroundAlpha))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = contentType.name.lowercase().capitalize(),
            style = TextStyle.body1.copy(
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp
            ),
            fontFamily = PretendardFontFamily
        )
    }
}

// 더미 데이터 생성 함수
fun getDummyMusicContents(): List<MusicContent> = listOf(
    Track(
        id = "1",
        platformId = "spotify_1",
        platform = MusicPlatform.SPOTIFY,
        name = "WHY, WHY, WHY",
        thumbnailUrl = R.drawable.album_cover_sample_1.toString(),
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis(),
        artists = listOf("SUMIN", "Slom"),
        album = "WHY",
        durationMs = 180000
    ),
    Album(
        id = "2",
        platformId = "spotify_2",
        platform = MusicPlatform.SPOTIFY,
        name = "ENGLAND",
        thumbnailUrl = R.drawable.album_cover_sample_2.toString(),
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis(),
        artists = listOf("YANG HONG WON"),
        releaseDate = System.currentTimeMillis(),
        tracks = emptyList(),
        trackCount = 12
    ),
    Playlist(
        id = "3",
        platformId = "apple_1",
        platform = MusicPlatform.APPLE_MUSIC,
        name = "K-Pop Mix",
        thumbnailUrl = R.drawable.album_cover_sample_2.toString(),
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis(),
        description = "Best K-Pop mix",
        trackCount = 25,
        owner = "Likebox",
        tracks = emptyList()
    )
)