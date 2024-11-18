package com.example.likebox.presentation.view.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.likebox.R
import com.example.likebox.presentation.viewmodel.SearchViewModel
import com.example.likebox.presentation.state.SearchUiState
import com.example.likebox.domain.model.library.MusicPlatform
import com.example.likebox.domain.model.library.Track
import com.example.likebox.domain.model.library.Album
import com.example.likebox.domain.model.library.MusicContent
import com.example.likebox.domain.model.library.Playlist
import com.example.likebox.presentation.view.navigation.LikeboxNavigationBar

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    navController : NavController
) {
    val searchState by viewModel.searchState.collectAsState()
    val selectedPlatforms by viewModel.selectedPlatforms.collectAsState()

    Scaffold (
        modifier = Modifier.background(Color.White),
        containerColor = Color.White,
        bottomBar = {
            Surface(
                shadowElevation = 4.dp,
                tonalElevation = 4.dp,
                color = Color.Transparent
            ) {
                LikeboxNavigationBar(navController)
            }
        }
    )
    { paddingValues ->
        Column (
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .offset(y=(-40).dp)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp, horizontal = 24.dp)
                    .background(Color.White)
            ) {
                // Top Bar with Back Button and Search
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.back_arrow),
                            contentDescription = "Back",
                            tint = Color(0xFF171A1F).copy(0.8f)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    SearchBar(
                        modifier = Modifier.weight(1f),
                        onSearchQueryChanged = viewModel::onSearchQueryChanged
                    )
                }

//        // Platform Filter
//        PlatformFilter(
//            selectedPlatforms = selectedPlatforms,
//            onPlatformToggled = viewModel::togglePlatform
//        )

                // Content
                when (val state = searchState) {
                    SearchUiState.Initial,
                    is SearchUiState.RecentSearches -> RecentSearchesSection(
                        searches = listOf(
                            "BTS Dynamite",
                            "Taylor Swift Shake it off",
                            "Ed Sheeran Perfect",
                            "BlackPink How You Like That",
                            "IU Eight",
                            "Justin Bieber Peaches",
                            "TWICE Fancy"
                        ),
                        onSearchRemoved = viewModel::removeRecentSearch,
                        onClearSearches = viewModel::clearRecentSearches
                    )
                    SearchUiState.Loading -> LoadingIndicator()
                    is SearchUiState.Results -> SearchResultsSection(results = state.results)
                    is SearchUiState.Error -> ErrorMessage(message = state.message)
                }
            }
        }
    }
}

@Composable
private fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    onSearchQueryChanged: (String) -> Unit
) {
    TextField(
        value = "",
        onValueChange = onSearchQueryChanged,
        modifier = modifier
            .background(Color(0xFFF8F9FA), RoundedCornerShape(18.dp))
            .wrapContentHeight(),
        placeholder = {
            Text(
                "Search songs, artists, or playlists",
                color = Color(0x66171A1F),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.search_icon),
                contentDescription = null,
                tint = Color(0xFF7F7F7F)
            )
        }
    )
}

@Composable
private fun PlatformFilter(
    selectedPlatforms: Set<MusicPlatform>,
    onPlatformToggled: (MusicPlatform) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MusicPlatform.entries.forEach { platform ->
            FilterChip(
                selected = platform in selectedPlatforms,
                onClick = { onPlatformToggled(platform) },
                label = { Text(platform.name) },
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

// SearchScreen.kt의 컴포넌트들

@Composable
private fun RecentSearchesSection(
    searches: List<String>,
    onSearchRemoved: (String) -> Unit,
    onClearSearches: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        // Header
        Text(
            text = "Recent Search",
            fontSize = 16.5.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .padding(horizontal = 4.dp, vertical = 4.dp)
        )

        // Search Items
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        ) {
            items(searches) { search ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = search,
                        color = Color.Black.copy(alpha = 0.5f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    )
                    IconButton(
                        onClick = { onSearchRemoved(search) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove",
                            modifier = Modifier.size(18.dp),
                            tint = Color.Black.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }

        // Clear Button
        Button(
            onClick = onClearSearches,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(10.53.dp)
                .border(
                    0.5.dp,
                    Color.Black.copy(alpha = 0.3f),
                    RoundedCornerShape(36.43.dp)
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            )
        ) {
            Text(
                "Clear Recent Search History",
                color = Color.Black.copy(alpha = 0.75f),
                fontSize = 11.41.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun SearchResultsSection(results: List<MusicContent>) {
    LazyColumn {
        items(results) { result ->
            when (result) {
                is Track -> TrackResultItem(track = result)
                is Album -> AlbumResultItem(album = result)
                is Playlist -> PlaylistResultItem(playlist = result)
                else -> {} // Handle any other potential MusicContent implementations
            }
        }
    }
}

@Composable
private fun TrackResultItem(track: Track) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 1.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = track.thumbnailUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(7.5.dp))
            )

            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = track.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Song",
                        fontSize = 10.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.width(9.dp))
                    Box(
                        modifier = Modifier
                            .size(2.dp)
                            .background(Color.Black.copy(alpha = 0.8f), CircleShape)
                    )
                    Spacer(modifier = Modifier.width(9.dp))
                    Text(
                        text = track.artists.firstOrNull() ?: "",
                        fontSize = 10.sp,
                        color = Color.Black
                    )
                }
            }
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            modifier = Modifier
                .padding(end = 10.dp)
                .size(15.dp),
            tint = Color.Black.copy(alpha = 0.8f)
        )
    }
}

@Composable
private fun AlbumResultItem(album: Album) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 1.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = album.thumbnailUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(7.5.dp))
            )

            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = album.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Album",
                        fontSize = 10.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.width(9.dp))
                    Box(
                        modifier = Modifier
                            .size(2.dp)
                            .background(Color.Black.copy(alpha = 0.8f), CircleShape)
                    )
                    Spacer(modifier = Modifier.width(9.dp))
                    Text(
                        text = album.artists.firstOrNull() ?: "",
                        fontSize = 10.sp,
                        color = Color.Black
                    )
                }
            }
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            modifier = Modifier
                .padding(end = 10.dp)
                .size(15.dp),
            tint = Color.Black.copy(alpha = 0.8f)
        )
    }
}

@Composable
private fun PlaylistResultItem(playlist: Playlist) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 1.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = playlist.thumbnailUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(7.5.dp))
            )

            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = playlist.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Playlist",
                        fontSize = 10.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.width(9.dp))
                    Box(
                        modifier = Modifier
                            .size(2.dp)
                            .background(Color.Black.copy(alpha = 0.8f), CircleShape)
                    )
                    Spacer(modifier = Modifier.width(9.dp))
                    Text(
                        text = playlist.owner,
                        fontSize = 10.sp,
                        color = Color.Black
                    )
                }
            }
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            modifier = Modifier
                .padding(end = 10.dp)
                .size(15.dp),
            tint = Color.Black.copy(alpha = 0.8f)
        )
    }
}

@Composable
private fun ErrorMessage(message: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            color = Color.Red,
            textAlign = TextAlign.Center
        )
    }
}