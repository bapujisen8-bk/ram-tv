package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.RamChannel
import com.example.ui.components.RamChannelCard
import com.example.ui.components.getCategoryGradient
import com.example.ui.player.RamVideoPlayer
import com.example.ui.theme.RamBorder
import com.example.ui.theme.RamCardHover
import com.example.ui.theme.RamGoldStar
import com.example.ui.theme.RamLimeAccent
import com.example.ui.theme.RamLimeGlow
import com.example.ui.theme.RamLiveRed
import com.example.ui.theme.RamSurface
import com.example.ui.theme.RamSurfaceVariant
import com.example.ui.theme.RamTextMuted
import com.example.ui.theme.RamTextPrimary
import com.example.ui.theme.RamTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusedPlayerScreen(
    channel: RamChannel,
    channels: List<RamChannel>,
    favoriteIds: Set<String>,
    onChannelSelect: (RamChannel) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onPrevChannel: () -> Unit,
    onNextChannel: () -> Unit,
    onBack: () -> Unit = {},
    onMinimize: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var isFullscreen by remember { mutableStateOf(false) }
    var showScheduleSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    if (isFullscreen) {
        // Immersive Fullscreen Player
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .testTag("focused_fullscreen_player")
        ) {
            RamVideoPlayer(
                channel = channel,
                isFavorite = favoriteIds.contains(channel.id),
                isFullscreen = true,
                onBack = { isFullscreen = false; onBack() },
                onMinimize = { isFullscreen = false; onMinimize() },
                onToggleFullscreen = { isFullscreen = false },
                onPrevChannel = onPrevChannel,
                onNextChannel = onNextChannel,
                onToggleFavorite = { onToggleFavorite(channel.id) },
                onOpenGuide = { showScheduleSheet = true },
                modifier = Modifier.fillMaxSize()
            )
        }
    } else {
        // Standard Focused Route
        Column(
            modifier = modifier
                .fillMaxSize()
                .testTag("focused_player_screen")
        ) {
            // Player Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .background(Color.Black)
            ) {
                RamVideoPlayer(
                    channel = channel,
                    isFavorite = favoriteIds.contains(channel.id),
                    isFullscreen = false,
                    onBack = onBack,
                    onMinimize = onMinimize,
                    onToggleFullscreen = { isFullscreen = true },
                    onPrevChannel = onPrevChannel,
                    onNextChannel = onNextChannel,
                    onToggleFavorite = { onToggleFavorite(channel.id) },
                    onOpenGuide = { showScheduleSheet = true },
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Channel Quick Details & EPG Button Bar
            Surface(
                color = RamSurface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Remote Serial Code Pill
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = RamLimeAccent
                            ) {
                                Text(
                                    text = "CH ${channel.serialNumber}",
                                    color = Color.Black,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = channel.name,
                                color = RamTextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = RamLimeAccent.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = channel.category.displayName,
                                    color = RamLimeAccent,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Now: ${channel.currentShow}",
                            color = RamLimeGlow,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // EPG Program Guide button
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = RamSurfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(1.dp, RamBorder),
                        modifier = Modifier
                            .clickable { showScheduleSheet = true }
                            .testTag("open_epg_sheet_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                Icons.Default.Schedule,
                                contentDescription = null,
                                tint = RamLimeAccent,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "EPG Guide",
                                color = RamLimeAccent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Live Zapping Channel Carousel Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Quick Channel Zapping",
                    color = RamTextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${channels.size} Available",
                    color = RamTextSecondary,
                    fontSize = 11.sp
                )
            }

            // Channels List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(channels, key = { it.id }) { ch ->
                    RamChannelCard(
                        channel = ch,
                        isActive = ch.id == channel.id,
                        isFavorite = favoriteIds.contains(ch.id),
                        onSelect = { onChannelSelect(ch) },
                        onToggleFavorite = { onToggleFavorite(ch.id) }
                    )
                }
            }
        }
    }

    // Program Schedule Bottom Sheet
    if (showScheduleSheet) {
        ModalBottomSheet(
            onDismissRequest = { showScheduleSheet = false },
            sheetState = sheetState,
            containerColor = RamSurface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Program Schedule (EPG)",
                            color = RamTextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = channel.name,
                            color = RamLimeAccent,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    IconButton(onClick = { showScheduleSheet = false }) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = RamTextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (channel.schedule.isEmpty()) {
                    Text(
                        text = "Continuous 24/7 Live Stream Broadcast",
                        color = RamTextSecondary,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(vertical = 20.dp)
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.padding(bottom = 20.dp)
                    ) {
                        items(channel.schedule) { item ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (item.isLiveNow) RamSurfaceVariant else RamCardHover.copy(alpha = 0.3f),
                                border = if (item.isLiveNow) androidx.compose.foundation.BorderStroke(1.dp, RamLimeAccent) else null,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = item.time,
                                            color = if (item.isLiveNow) RamLimeAccent else RamTextMuted,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )

                                        if (item.isLiveNow) {
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = RamLimeAccent
                                            ) {
                                                Text(
                                                    text = "ON AIR",
                                                    color = Color.Black,
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Black,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = item.title,
                                        color = RamTextPrimary,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    if (item.description.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = item.description,
                                            color = RamTextSecondary,
                                            fontSize = 11.sp
                                        )
                                    }

                                    if (item.isLiveNow && item.progress > 0f) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        LinearProgressIndicator(
                                            progress = { item.progress },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(3.dp)
                                                .clip(RoundedCornerShape(1.5.dp)),
                                            color = RamLimeAccent,
                                            trackColor = RamCardHover
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
