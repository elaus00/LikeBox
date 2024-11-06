package com.example.likebox

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

// SyncState sealed class 추가
sealed class SyncState {
    data object Idle : SyncState()
    data object Syncing : SyncState()
    data object Success : SyncState()
    data class Error(val message: String) : SyncState()
}

@Composable
fun HomeScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val syncState by viewModel.syncState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Sync Status Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "동기화 상태",
                    style = MaterialTheme.typography.titleMedium
                )
                when (syncState) {
                    is SyncState.Idle -> {
                        Button(onClick = { viewModel.startSync() }) {
                            Text("동기화 시작")
                        }
                    }
                    is SyncState.Syncing -> {
                        CircularProgressIndicator()
                    }
                    is SyncState.Success -> {
                        Text("동기화 완료")
                    }
                    is SyncState.Error -> {
                        Text(
                            text = (syncState as SyncState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }

        // Recent Tracks
        LazyColumn {
            items(
                items = List(10) { index ->
                    Track(
                        id = "$index",
                        title = "Track $index",
                        artist = "Artist $index",
                        platform = "Spotify"
                    )
                }
            ) { track ->
                TrackItem(track = track)
            }
        }
    }
}
