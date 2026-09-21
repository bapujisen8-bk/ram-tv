package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SettingsRemote
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material.icons.filled.Dialpad
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.RamLiveIndicator
import com.example.ui.components.RamTvRemoteCodeDialog
import com.example.ui.screens.CategoryScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.FocusedPlayerScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.UpdatesScreen
import com.example.ui.theme.RamBorder
import com.example.ui.theme.RamCardHover
import com.example.ui.theme.RamDarkBackground
import com.example.ui.theme.RamLimeAccent
import com.example.ui.theme.RamLimeGlow
import com.example.ui.theme.RamSurface
import com.example.ui.theme.RamSurfaceVariant
import com.example.ui.theme.RamTextMuted
import com.example.ui.theme.RamTextPrimary
import com.example.ui.theme.RamTextSecondary
import kotlinx.coroutines.launch

fun getNavIcon(destination: AppNavDestination): ImageVector {
    return when (destination) {
        AppNavDestination.HOME -> Icons.Default.LiveTv
        AppNavDestination.FAVORITES -> Icons.Default.Star
        AppNavDestination.CATEGORY -> Icons.Default.Category
        AppNavDestination.UPDATES -> Icons.Default.Download
        AppNavDestination.SETTINGS -> Icons.Default.Settings
        AppNavDestination.PLAYER -> Icons.Default.PlayCircle
    }
}

@Composable
fun RamTvApp(viewModel: RamTvViewModel) {
    val currentDest by viewModel.currentDestination.collectAsState()
    val channels by viewModel.channels.collectAsState()
    val filteredChannels by viewModel.filteredChannels.collectAsState()
    val activeChannel by viewModel.activeChannel.collectAsState()
    val recentlyPlayed by viewModel.recentlyPlayed.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val playbackSettings by viewModel.playbackSettings.collectAsState()
    val m3uPlaylists by viewModel.m3uPlaylists.collectAsState()
    val isMuted by viewModel.isMuted.collectAsState()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showRemoteCodeDialog by remember { mutableStateOf(false) }
    var isSearchExpanded by remember { mutableStateOf(false) }

    // Dialog for direct TV serial code jump
    if (showRemoteCodeDialog) {
        RamTvRemoteCodeDialog(
            allChannels = channels,
            onDismiss = { showRemoteCodeDialog = false },
            onPlayChannel = { ch ->
                viewModel.selectChannel(ch, navigateToPlayer = true)
            }
        )
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(RamDarkBackground)) {
        val isWideScreen = maxWidth >= 720.dp

        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = !isWideScreen,
            drawerContent = {
                ModalDrawerSheet(
                    drawerContainerColor = RamSurface,
                    modifier = Modifier.width(300.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp)
                            .statusBarsPadding()
                    ) {
                        // RaM Tv Brand Header
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(RamLimeAccent),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "RaM",
                                    color = Color.Black,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "RaM Tv Live",
                                    color = RamTextPrimary,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Premium Live Streaming",
                                    color = RamLimeGlow,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        HorizontalDivider(color = RamBorder)
                        Spacer(modifier = Modifier.height(16.dp))

                        // Drawer Navigation Items
                        AppNavDestination.values().forEach { dest ->
                            val isSelected = currentDest == dest
                            NavigationDrawerItem(
                                label = {
                                    Text(
                                        text = dest.title,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                },
                                icon = {
                                    Icon(
                                        imageVector = getNavIcon(dest),
                                        contentDescription = dest.title
                                    )
                                },
                                selected = isSelected,
                                onClick = {
                                    viewModel.navigateTo(dest)
                                    scope.launch { drawerState.close() }
                                },
                                colors = NavigationDrawerItemDefaults.colors(
                                    selectedContainerColor = RamLimeAccent.copy(alpha = 0.15f),
                                    selectedIconColor = RamLimeAccent,
                                    selectedTextColor = RamLimeAccent,
                                    unselectedContainerColor = Color.Transparent,
                                    unselectedIconColor = RamTextSecondary,
                                    unselectedTextColor = RamTextPrimary
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .padding(vertical = 4.dp)
                                    .testTag("drawer_item_${dest.name}")
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        // Active Channel mini status in drawer
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = RamSurfaceVariant,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "CURRENT STREAM",
                                        color = RamLimeAccent,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    RamLiveIndicator()
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = activeChannel.name,
                                    color = RamTextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = activeChannel.currentShow,
                                    color = RamTextSecondary,
                                    fontSize = 11.sp,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                // Desktop / Tablet Sidebar Navigation Rail
                if (isWideScreen) {
                    NavigationRail(
                        containerColor = RamSurface,
                        contentColor = RamTextPrimary,
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(80.dp)
                            .testTag("sidebar_navigation_rail")
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))

                        // RaM Logo in rail
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(RamLimeAccent),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "RaM",
                                color = Color.Black,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        AppNavDestination.values().forEach { dest ->
                            val isSelected = currentDest == dest
                            NavigationRailItem(
                                selected = isSelected,
                                onClick = { viewModel.navigateTo(dest) },
                                icon = {
                                    Icon(
                                        imageVector = getNavIcon(dest),
                                        contentDescription = dest.title
                                    )
                                },
                                label = {
                                    Text(
                                        text = dest.title,
                                        fontSize = 10.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = Color.Black,
                                    selectedTextColor = RamLimeAccent,
                                    indicatorColor = RamLimeAccent,
                                    unselectedIconColor = RamTextSecondary,
                                    unselectedTextColor = RamTextSecondary
                                ),
                                modifier = Modifier.testTag("rail_item_${dest.name}")
                            )
                        }
                    }
                }

                // Main App Body
                Scaffold(
                    topBar = {
                        // Top App Bar for mobile / general viewport
                        Surface(
                            color = RamSurface,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (isSearchExpanded) {
                                // Sleek expanded search bar with TV Code remote button on left
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .statusBarsPadding()
                                        .padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // TV Code button (remote logo) side re (left)
                                    Surface(
                                        onClick = { showRemoteCodeDialog = true },
                                        shape = RoundedCornerShape(18.dp),
                                        color = RamSurfaceVariant,
                                        border = androidx.compose.foundation.BorderStroke(1.dp, RamLimeAccent.copy(alpha = 0.6f)),
                                        modifier = Modifier
                                            .height(38.dp)
                                            .testTag("topbar_tv_code_button_expanded")
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.SettingsRemote,
                                                contentDescription = "TV Remote Code",
                                                tint = RamLimeAccent,
                                                modifier = Modifier.size(17.dp)
                                            )
                                            Text(
                                                text = "TV Code",
                                                color = RamTextPrimary,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    OutlinedTextField(
                                        value = searchQuery,
                                        onValueChange = { viewModel.setSearchQuery(it) },
                                        placeholder = {
                                            Text(
                                                "Search 400+ channels...",
                                                fontSize = 12.sp,
                                                color = RamTextMuted
                                            )
                                        },
                                        leadingIcon = {
                                            Icon(
                                                Icons.Default.Search,
                                                contentDescription = "Search",
                                                tint = RamLimeAccent,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        },
                                        trailingIcon = {
                                            IconButton(
                                                onClick = {
                                                    viewModel.setSearchQuery("")
                                                    isSearchExpanded = false
                                                },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Icon(
                                                    Icons.Default.Close,
                                                    contentDescription = "Close",
                                                    tint = RamTextSecondary,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        },
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = RamLimeAccent,
                                            unfocusedBorderColor = RamBorder,
                                            focusedTextColor = RamTextPrimary,
                                            unfocusedTextColor = RamTextPrimary,
                                            cursorColor = RamLimeAccent
                                        ),
                                        shape = RoundedCornerShape(20.dp),
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(46.dp)
                                            .testTag("topbar_search_input")
                                    )
                                }
                            } else {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .statusBarsPadding()
                                        .padding(horizontal = 14.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        if (!isWideScreen) {
                                            IconButton(
                                                onClick = { scope.launch { drawerState.open() } },
                                                modifier = Modifier.testTag("mobile_menu_button")
                                            ) {
                                                Icon(
                                                    Icons.Default.Menu,
                                                    contentDescription = "Menu Drawer",
                                                    tint = RamLimeAccent
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(4.dp))
                                        }

                                        // Logo & Brand Name
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(32.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(RamLimeAccent),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "RaM",
                                                    color = Color.Black,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Black
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Column {
                                                Text(
                                                    text = "RaM Tv",
                                                    color = RamTextPrimary,
                                                    fontSize = 17.sp,
                                                    fontWeight = FontWeight.Black
                                                )
                                                Text(
                                                    text = "Live HD Streams",
                                                    color = RamLimeGlow,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                        }
                                    }

                                    // Right Action: TV Code (with Remote Logo) on the left side of Search 🔍
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        // TV Code button with remote logo
                                        Surface(
                                            onClick = { showRemoteCodeDialog = true },
                                            shape = RoundedCornerShape(18.dp),
                                            color = RamSurfaceVariant,
                                            border = androidx.compose.foundation.BorderStroke(1.dp, RamLimeAccent.copy(alpha = 0.6f)),
                                            modifier = Modifier
                                                .height(34.dp)
                                                .testTag("topbar_tv_code_button")
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 10.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                                            ) {
                                                Icon(
                                                    Icons.Default.SettingsRemote,
                                                    contentDescription = "TV Remote Code",
                                                    tint = RamLimeAccent,
                                                    modifier = Modifier.size(17.dp)
                                                )
                                                Text(
                                                    text = "TV Code",
                                                    color = RamTextPrimary,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }

                                        // Small size Search 🔍 Icon in top heading
                                        IconButton(
                                            onClick = {
                                                isSearchExpanded = true
                                                if (currentDest != AppNavDestination.HOME) {
                                                    viewModel.navigateTo(AppNavDestination.HOME)
                                                }
                                            },
                                            modifier = Modifier
                                                .size(34.dp)
                                                .clip(CircleShape)
                                                .background(RamSurfaceVariant)
                                                .testTag("topbar_search_button")
                                        ) {
                                            Icon(
                                                Icons.Default.Search,
                                                contentDescription = "Search Channels",
                                                tint = RamLimeAccent,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    },
                    bottomBar = {
                        // Mobile Bottom Navigation Bar: LIVE TV, FAVORITES, CATEGORY, GET APK, SETTING
                        if (!isWideScreen) {
                            val bottomNavDestinations = listOf(
                                AppNavDestination.HOME,
                                AppNavDestination.FAVORITES,
                                AppNavDestination.CATEGORY,
                                AppNavDestination.UPDATES,
                                AppNavDestination.SETTINGS
                            )
                            NavigationBar(
                                containerColor = RamSurface,
                                contentColor = RamTextPrimary,
                                modifier = Modifier
                                    .navigationBarsPadding()
                                    .testTag("mobile_bottom_navigation")
                            ) {
                                bottomNavDestinations.forEach { dest ->
                                    val isSelected = currentDest == dest
                                    NavigationBarItem(
                                        selected = isSelected,
                                        onClick = { viewModel.navigateTo(dest) },
                                        icon = {
                                            Icon(
                                                imageVector = getNavIcon(dest),
                                                contentDescription = dest.title
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = dest.title,
                                                fontSize = 9.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = Color.Black,
                                            selectedTextColor = RamLimeAccent,
                                            indicatorColor = RamLimeAccent,
                                            unselectedIconColor = RamTextSecondary,
                                            unselectedTextColor = RamTextSecondary
                                        ),
                                        modifier = Modifier.testTag("bottom_nav_${dest.name}")
                                    )
                                }
                            }
                        }
                    },
                    containerColor = RamDarkBackground,
                    modifier = Modifier.weight(1f)
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentDest) {
                            AppNavDestination.HOME -> {
                                HomeScreen(
                                    featuredChannel = channels.firstOrNull { it.isFeatured } ?: channels.first(),
                                    channels = filteredChannels,
                                    allChannels = channels,
                                    recentlyPlayed = recentlyPlayed,
                                    activeChannel = activeChannel,
                                    selectedCategory = selectedCategory,
                                    searchQuery = searchQuery,
                                    favoriteIds = favoriteIds,
                                    isMuted = isMuted,
                                    onCategorySelect = { viewModel.selectCategory(it) },
                                    onChannelSelect = { ch, navigateToPlayer ->
                                        viewModel.selectChannel(ch, navigateToPlayer)
                                    },
                                    onToggleFavorite = { viewModel.toggleFavorite(it) },
                                    onToggleMute = { viewModel.toggleMute() }
                                )
                            }
                            AppNavDestination.CATEGORY -> {
                                CategoryScreen(
                                    channels = channels,
                                    activeChannel = activeChannel,
                                    favoriteIds = favoriteIds,
                                    onChannelSelect = { ch, navigateToPlayer ->
                                        viewModel.selectChannel(ch, navigateToPlayer)
                                    },
                                    onToggleFavorite = { viewModel.toggleFavorite(it) }
                                )
                            }
                            AppNavDestination.PLAYER -> {
                                FocusedPlayerScreen(
                                    channel = activeChannel,
                                    channels = channels,
                                    favoriteIds = favoriteIds,
                                    onChannelSelect = { viewModel.selectChannel(it, true) },
                                    onToggleFavorite = { viewModel.toggleFavorite(it) },
                                    onPrevChannel = { viewModel.zapPrevious() },
                                    onNextChannel = { viewModel.zapNext() },
                                    onBack = { viewModel.navigateTo(AppNavDestination.HOME) },
                                    onMinimize = { viewModel.navigateTo(AppNavDestination.HOME) }
                                )
                            }
                            AppNavDestination.FAVORITES -> {
                                FavoritesScreen(
                                    allChannels = channels,
                                    favoriteIds = favoriteIds,
                                    activeChannel = activeChannel,
                                    onChannelSelect = { viewModel.selectChannel(it, true) },
                                    onToggleFavorite = { viewModel.toggleFavorite(it) }
                                )
                            }
                            AppNavDestination.UPDATES -> {
                                UpdatesScreen()
                            }
                            AppNavDestination.SETTINGS -> {
                                SettingsScreen(
                                    settings = playbackSettings,
                                    m3uPlaylists = m3uPlaylists,
                                    onUpdateQuality = { viewModel.updateStreamQuality(it) },
                                    onToggleLowLatency = { viewModel.toggleLowLatency(it) },
                                    onToggleHardwareAccel = { viewModel.toggleHardwareAcceleration(it) },
                                    onToggleBackgroundAudio = { viewModel.toggleBackgroundAudio(it) },
                                    onToggleNotifications = { viewModel.toggleNotifications(it) },
                                    onAddM3u = { name, url -> viewModel.addM3uPlaylist(name, url) },
                                    onDeleteM3u = { viewModel.deleteM3uPlaylist(it) },
                                    onAddCustomChannel = { name, cat, stream, logo ->
                                        viewModel.createAndAddChannel(name, cat, stream, logo)
                                    },
                                    onUpdateChannelStream = { id, stream ->
                                        viewModel.updateChannelStream(id, stream)
                                    },
                                    onNavigateToApkDownload = { viewModel.navigateTo(AppNavDestination.UPDATES) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
