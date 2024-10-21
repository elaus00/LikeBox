package com.example.likebox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LikeBoxApp()
        }
    }
}

@Composable
fun LikeBoxApp() {
    var currentTab by remember { mutableStateOf("Home") }
    var isOffline by remember { mutableStateOf(false) }
    var syncStatus by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    val platforms = listOf(
        Platform("Spotify", true),
        Platform("Apple Music", true),
        Platform("YouTube Music", false)
    )

    val likedSongs = listOf(
        Song("Song 1", "Artist 1", "Spotify"),
        Song("Song 2", "Artist 2", "Apple Music"),
        Song("Song 3", "Artist 3", "YouTube Music")
    )

    val playlists = listOf(
        Playlist("My Playlist 1", 15, "Spotify"),
        Playlist("My Playlist 2", 20, "Apple Music")
    )

    MaterialTheme {
        Scaffold(
            topBar = { AppHeader() },
            bottomBar = { BottomNavigationBar(currentTab) { currentTab = it } },
            floatingActionButton = { NewPlaylistFAB() },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                if (isOffline) {
                    OfflineIndicator()
                }
                SearchBar()
                PlatformStatus(platforms)
                when (currentTab) {
                    "Home" -> HomeContent(likedSongs, playlists)
                    // Implement other tabs as needed
                }
                if (syncStatus.isNotEmpty()) {
                    SyncNotification(snackbarHostState, syncStatus)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader() {
    TopAppBar(
        title = { Text("LikeBox", fontWeight = FontWeight.Bold) },
        actions = {
            IconButton(onClick = { /* TODO: Open settings */ }) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }
            IconButton(onClick = { /* TODO: Open profile */ }) {
                Icon(Icons.Default.Person, contentDescription = "Profile")
            }
        }
    )
}

@Composable
fun OfflineIndicator() {
    Surface(color = Color.Red, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Default.WifiOff, contentDescription = "Offline", tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("You're offline", color = Color.White)
        }
    }
}

@Composable
fun SearchBar() {
    OutlinedTextField(
        value = "",
        onValueChange = { /* TODO: Implement search functionality */ },
        placeholder = { Text("Search songs, artists, or playlists") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Composable
fun PlatformStatus(platforms: List<Platform>) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Connected Platforms", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            platforms.forEach { platform ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.MusicNote,
                        contentDescription = platform.name,
                        tint = if (platform.connected) Color.Green else Color.Gray
                    )
                    Text(platform.name, fontSize = 12.sp)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { /* TODO: Implement sync functionality */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sync Now")
        }
    }
}

@Composable
fun HomeContent(likedSongs: List<Song>, playlists: List<Playlist>) {
    LazyColumn {
        item { LikedSongsSection(likedSongs) }
        item { PlaylistsSection(playlists) }
    }
}

@Composable
fun LikedSongsSection(songs: List<Song>) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Liked Songs", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp))
        songs.forEach { song ->
            SongItem(song)
            Divider()
        }
    }
}

@Composable
fun SongItem(song: Song) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(song.name, fontWeight = FontWeight.Medium)
            Text(song.artist, fontSize = 14.sp, color = Color.Gray)
        }
        Text(song.platform, fontSize = 12.sp, color = Color.Gray)
        IconButton(onClick = { /* TODO: Implement play functionality */ }) {
            Icon(Icons.Default.PlayArrow, contentDescription = "Play")
        }
        IconButton(onClick = { /* TODO: Implement add to playlist functionality */ }) {
            Icon(Icons.Default.Add, contentDescription = "Add to playlist")
        }
    }
}

@Composable
fun PlaylistsSection(playlists: List<Playlist>) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Playlists", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp))
        playlists.forEach { playlist ->
            PlaylistItem(playlist)
            Divider()
        }
    }
}

@Composable
fun PlaylistItem(playlist: Playlist) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(playlist.name, fontWeight = FontWeight.Medium)
            Text("${playlist.songCount} songs", fontSize = 14.sp, color = Color.Gray)
        }
        Text(playlist.platform, fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun BottomNavigationBar(currentTab: String, onTabSelected: (String) -> Unit) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentTab == "Home",
            onClick = { onTabSelected("Home") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.List, contentDescription = "Playlists") },
            label = { Text("Playlists") },
            selected = currentTab == "Playlists",
            onClick = { onTabSelected("Playlists") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Explore, contentDescription = "Discover") },
            label = { Text("Discover") },
            selected = currentTab == "Discover",
            onClick = { onTabSelected("Discover") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = currentTab == "Profile",
            onClick = { onTabSelected("Profile") }
        )
    }
}

@Composable
fun NewPlaylistFAB() {
    FloatingActionButton(
        onClick = { /* TODO: Implement new playlist creation */ },
        content = { Icon(Icons.Default.Add, contentDescription = "New Playlist") }
    )
}

@Composable
fun SyncNotification(snackbarHostState: SnackbarHostState, status: String) {
    LaunchedEffect(status) {
        snackbarHostState.showSnackbar(status)
    }
}

data class Platform(val name: String, val connected: Boolean)
data class Song(val name: String, val artist: String, val platform: String)
data class Playlist(val name: String, val songCount: Int, val platform: String)
