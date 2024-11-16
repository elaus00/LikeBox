package com.example.likebox.presentation.view.screens.library

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.likebox.presentation.viewmodel.library.PlaylistDetailViewModel

@Composable
fun PlaylistDetailScreen(
    playlistId: String?,
    viewModel: PlaylistDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {}
