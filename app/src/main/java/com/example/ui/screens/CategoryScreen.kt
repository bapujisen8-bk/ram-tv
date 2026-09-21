package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.model.ChannelCategory
import com.example.model.RamChannel
import com.example.ui.components.RamChannelCard
import com.example.ui.components.getCategoryGradient
import com.example.ui.theme.RamBorder
import com.example.ui.theme.RamLimeAccent
import com.example.ui.theme.RamLimeGlow
import com.example.ui.theme.RamSurface
import com.example.ui.theme.RamSurfaceVariant
import com.example.ui.theme.RamTextMuted
import com.example.ui.theme.RamTextPrimary
import com.example.ui.theme.RamTextSecondary

@Composable
fun CategoryScreen(
    channels: List<RamChannel>,
    activeChannel: RamChannel,
    favoriteIds: Set<String>,
    onChannelSelect: (RamChannel, Boolean) -> Unit,
    onToggleFavorite: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFolder by remember { mutableStateOf<ChannelCategory?>(null) }

    val categories = remember {
        ChannelCategory.values().filter { it != ChannelCategory.ALL && it != ChannelCategory.FAVORITES && it != ChannelCategory.FEATURED }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
            .testTag("category_screen")
    ) {
        if (selectedFolder == null) {
            // Category Folders Grid Overview
            LazyColumn(
                contentPadding = PaddingValues(top = 10.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Category,
                                contentDescription = null,
                                tint = RamLimeAccent,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Channel Categories",
                                color = RamTextPrimary,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "Browse 400+ channels grouped by Odia, Movies, News, Sports, Devotional & more",
                            color = RamTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }

                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        categories.forEach { cat ->
                            val catChannels = channels.filter { it.category == cat }
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = RamSurface),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, RamBorder, RoundedCornerShape(12.dp))
                                    .clickable { selectedFolder = cat }
                                    .testTag("category_folder_${cat.name}")
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(46.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(getCategoryGradient(cat)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(text = cat.icon, fontSize = 22.sp)
                                        }

                                        Spacer(modifier = Modifier.width(14.dp))

                                        Column {
                                            Text(
                                                text = cat.displayName,
                                                color = RamTextPrimary,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "${catChannels.size} Live Channels",
                                                color = RamLimeAccent,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }

                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = "Open",
                                        tint = RamTextMuted,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Selected Folder View showing all channels in that category
            val currentCat = selectedFolder!!
            val catChannels = channels.filter { it.category == currentCat }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { selectedFolder = null },
                    modifier = Modifier.testTag("category_back_button")
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = RamLimeAccent
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = currentCat.icon, fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = currentCat.displayName,
                            color = RamTextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "${catChannels.size} live channels ready for direct playback",
                        color = RamLimeGlow,
                        fontSize = 11.sp
                    )
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(top = 4.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(catChannels, key = { "${it.id}_${it.serialNumber}" }) { channel ->
                    RamChannelCard(
                        channel = channel,
                        isActive = activeChannel.id == channel.id,
                        isFavorite = favoriteIds.contains(channel.id),
                        onSelect = { onChannelSelect(channel, true) }, // Direct play!
                        onToggleFavorite = { onToggleFavorite(channel.id) }
                    )
                }
            }
        }
    }
}
