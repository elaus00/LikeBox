package com.example.likebox.presentation.view.screens.library.detail

import androidx.compose.foundation.clickable
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
import com.example.likebox.domain.model.Playlist
import com.example.likebox.domain.model.Track
import com.example.likebox.presentation.view.theme.PretendardFontFamily
import com.example.likebox.presentation.viewmodel.detail.PlaylistDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistDetailScreen(
    navController: NavController,
    playlistId: String,
    viewModel: PlaylistDetailViewModel = hiltViewModel()
) {
    var showAddTrackDialog by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(playlistId) {
        viewModel.loadPlaylist(playlistId)
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddTrackDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, "Add tracks")
            }
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
            uiState.playlist != null -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    // Header Section
                    item {
                        PlaylistHeader(
                            playlist = uiState.playlist!!,
                            onLikeClick = {
                                viewModel.toggleLiked(uiState.playlist!!)
                            }
                        )
                    }

                    // Track List Section - playlist.tracks 사용
                    items(uiState.playlist!!.tracks) { track ->
                        TrackListItem(
                            track = track,
                            onTrackClick = { /* Navigation to track detail */ },
                            onMoreClick = { /* Show track options */ }
                        )
                    }
                }

                // Add Track Dialog
                if (showAddTrackDialog) {
                    AddTrackDialog(
                        onDismiss = { showAddTrackDialog = false },
                        onAddTrack = { tracks ->
                            viewModel.addTracksToPlaylist(uiState.playlist!!.id, tracks)
                            showAddTrackDialog = false
                        }
                    )
                }
            }
        }
    }
}
@Composable
private fun PlaylistHeader(playlist: Playlist, onLikeClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Playlist Cover
        AsyncImage(
            model = playlist.thumbnailUrl,
            contentDescription = "Playlist cover",
            modifier = Modifier
                .size(200.dp)
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Playlist Info
        Text(
            text = playlist.name,
            fontSize = 24.sp,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = playlist.description ?: "",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Playlist Stats
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "${playlist.trackCount} tracks",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = "Created by ${playlist.owner}",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        // Action Buttons
        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = { /* Like functionality */ },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Favorite, contentDescription = "Like")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Like")
            }

            Button(
                onClick = { /* Shuffle play functionality */ },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Shuffle, contentDescription = "Shuffle")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Shuffle")
            }
        }
    }
}

@Composable
fun TrackListItem(
    track: Track,
    onTrackClick: () -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onTrackClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = track.thumbnailUrl,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(MaterialTheme.shapes.small),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        ) {
            Text(
                text = track.name,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1
            )
            Text(
                text = track.artists.joinToString(", "),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                maxLines = 1
            )
        }

        IconButton(onClick = onMoreClick) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More options"
            )
        }
    }
}

@Composable
fun AddTrackDialog(
    onDismiss: () -> Unit,
    onAddTrack: (List<Track>) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf(emptyList<Track>()) }
    var selectedTracks by remember { mutableStateOf(setOf<Track>()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Tracks") },
        text = {
            Column {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search tracks") },
                    modifier = Modifier.fillMaxWidth()
                )

                LazyColumn {
                    items(searchResults) { track ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedTracks = if (track in selectedTracks) {
                                        selectedTracks - track
                                    } else {
                                        selectedTracks + track
                                    }
                                }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = track in selectedTracks,
                                onCheckedChange = { checked ->
                                    selectedTracks = if (checked) {
                                        selectedTracks + track
                                    } else {
                                        selectedTracks - track
                                    }
                                }
                            )
                            Text(
                                text = "${track.name} - ${track.artists.joinToString(", ")}",
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onAddTrack(selectedTracks.toList()) }
            ) {
                Text("Add Selected")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}