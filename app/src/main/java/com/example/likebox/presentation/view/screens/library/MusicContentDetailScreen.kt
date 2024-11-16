package com.example.likebox.presentation.view.screens.library

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.likebox.presentation.view.screens.library.LibraryComponents.DetailTopBar
import com.example.likebox.presentation.view.theme.PretendardFontFamily
import com.example.likebox.presentation.viewmodel.library.AlbumDetailViewModel
import com.example.likebox.presentation.viewmodel.library.ArtistDetailViewModel
import com.example.likebox.presentation.viewmodel.library.PlaylistDetailViewModel

@Composable
fun AlbumDetailScreen(
    albumId: String?,
    viewModel: AlbumDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    DetailScreenScaffold(
        title = uiState.album?.name ?: "",
        onNavigateBack = onNavigateBack
    ) { paddingValues ->
        // Album detail content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Album content implementation
        }
    }
}

@Composable
fun PlaylistDetailScreen(
    playlistId: String?,
    viewModel: PlaylistDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    DetailScreenScaffold(
        title = uiState.playlist?.name ?: "",
        onNavigateBack = onNavigateBack
    ) { paddingValues ->
        // Playlist detail content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Playlist content implementation
        }
    }
}

@Composable
fun ArtistDetailScreen(
    artistId: String?,
    viewModel: ArtistDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    DetailScreenScaffold(
        title = uiState.artist?.name ?: "",
        onNavigateBack = onNavigateBack
    ) { paddingValues ->
        // Artist detail content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Artist content implementation
        }
    }
}

@Composable
private fun DetailScreenScaffold(
    title: String,
    onNavigateBack: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            DetailTopBar(
                title = title,
                onNavigateBack = onNavigateBack
            )
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}