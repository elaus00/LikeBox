package com.example.likebox.presentation.view.screens.library

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.likebox.domain.model.library.Album
import com.example.likebox.presentation.view.screens.library.detail.TrackListItem
import com.example.likebox.presentation.view.theme.PretendardFontFamily
import com.example.likebox.presentation.view.screens.library.viewmodel.AlbumDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailScreen(
    navController: NavController,
    albumId: String?,
    viewModel: AlbumDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(albumId) {
        if (albumId != null) {
            viewModel.loadAlbum(albumId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { /* Empty title */ },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Share functionality */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                    IconButton(onClick = { /* More options */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            uiState.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: ${uiState.error}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            uiState.album != null -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    // Header Section
                    item {
                        AlbumHeader(
                            album = uiState.album!!,
                            onLikeClick = {
                                viewModel.toggleLiked(uiState.album!!)
                            }
                        )
                    }

                    // Track List Section - uiState.album.tracks가 있다면 표시
                    uiState.album?.let { album ->
                        items(album.tracks) { track ->
                            TrackListItem(
                                track = track,
                                onTrackClick = { /* Navigation to track detail */ },
                                onMoreClick = { /* Show track options */ }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AlbumHeader(
    album: Album,
    onLikeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Album Cover
        AsyncImage(
            model = album.thumbnailUrl,
            contentDescription = "Album cover",
            modifier = Modifier
                .size(200.dp)
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Album Info
        Text(
            text = album.name,
            fontSize = 24.sp,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = album.artists.joinToString(", "),
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Album Stats
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "${album.trackCount} tracks",
                fontSize = 14.sp,
                color = Color.Gray
            )
            // Unix timestamp를 Date로 변환
            Text(
                text = "Released ${
                    java.text.SimpleDateFormat("yyyy.MM.dd").format(
                        java.util.Date(album.releaseDate * 1000)
                    )
                }",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        // Action Button
        OutlinedButton(
            onClick = onLikeClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Favorite, contentDescription = "Like")
            Spacer(modifier = Modifier.width(4.dp))
            Text("Like")
        }
    }
}