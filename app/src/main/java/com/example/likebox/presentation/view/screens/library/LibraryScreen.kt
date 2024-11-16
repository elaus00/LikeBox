package com.example.likebox.presentation.view.screens.library

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.likebox.domain.model.*
import com.example.likebox.presentation.view.navigation.LikeboxNavigationBar
import com.example.likebox.presentation.view.screens.components.getDummyContent
import com.example.likebox.presentation.view.theme.PretendardFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onNavigateToPlaylist: (String) -> Unit,
    onNavigateToAlbum: (String) -> Unit,
    onNavigateToArtist: (String) -> Unit
) {
    var selectedContentType by remember { mutableStateOf(ContentType.TRACK) }
    var selectedPlatforms by remember { mutableStateOf(setOf<MusicPlatform>()) }
    var showFilterSheet by remember { mutableStateOf(false) }
    var showSortDropdown by remember { mutableStateOf(false) }
    var selectedSort by remember { mutableStateOf("Latest") }

    Scaffold(
        modifier = Modifier.background(Color.White),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My Library",
                    fontSize = 24.sp,
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold
                )
                IconButton(onClick = { showFilterSheet = true }) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filter",
                        tint = Color.Black
                    )
                }
            }
        },
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Content Type & Sort Options
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                ) {
                    ContentTypeSelector(
                        selectedType = selectedContentType,
                        onTypeSelected = { selectedContentType = it }
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, top = 8.dp)
                ) {
                    SortButton(
                        selectedSort = selectedSort,
                        onSortClick = { showSortDropdown = true }
                    )

                    SortDropdownMenu(
                        expanded = showSortDropdown,
                        selectedSort = selectedSort,
                        onSortSelected = {
                            selectedSort = it
                            showSortDropdown = false
                        },
                        onDismiss = { showSortDropdown = false }
                    )
                }
            }

            // Content List
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    getDummyContent(selectedContentType)
                        .filter { selectedPlatforms.isEmpty() || it.platform in selectedPlatforms }
                ) { content ->
                    MusicContentListItem(
                        content = content,
                        onItemClick = { id ->
                            when (content) {
                                is Playlist -> onNavigateToPlaylist(id)
                                is Album -> onNavigateToAlbum(id)
                                is Track -> Unit
                            }
                        }
                    )
                }
            }
        }

        if (showFilterSheet) {
            ModalBottomSheet(
                onDismissRequest = { showFilterSheet = false },
                containerColor = Color.White,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            ) {
                FilterBottomSheetContent(
                    selectedPlatforms = selectedPlatforms,
                    onPlatformSelectionChanged = { selectedPlatforms = it }
                )
            }
        }
    }
}