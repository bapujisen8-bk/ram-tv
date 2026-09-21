package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.example.ui.components.RamHeroFeaturedCard
import com.example.ui.components.RamHorizontalChannelCard
import com.example.ui.theme.RamBorder
import com.example.ui.theme.RamLimeAccent
import com.example.ui.theme.RamLimeGlow
import com.example.ui.theme.RamSurface
import com.example.ui.theme.RamSurfaceVariant
import com.example.ui.theme.RamTextMuted
import com.example.ui.theme.RamTextPrimary
import com.example.ui.theme.RamTextSecondary

@Composable
fun HomeScreen(
    featuredChannel: RamChannel,
    channels: List<RamChannel>,
    allChannels: List<RamChannel>,
    recentlyPlayed: List<RamChannel>,
    activeChannel: RamChannel,
    selectedCategory: ChannelCategory,
    searchQuery: String,
    favoriteIds: Set<String>,
    isMuted: Boolean,
    onCategorySelect: (ChannelCategory) -> Unit,
    onChannelSelect: (RamChannel, Boolean) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onToggleMute: () -> Unit,
    modifier: Modifier = Modifier
) {
    val favoriteChannels = remember(allChannels, favoriteIds) {
        allChannels.filter { favoriteIds.contains(it.id) }
    }

    val odiaChannels = remember(allChannels) {
        allChannels.filter { it.category == ChannelCategory.ODIA }
    }

    val entertainmentChannels = remember(allChannels) {
        allChannels.filter { it.category == ChannelCategory.ENTERTAINMENT }
    }

    val movieChannels = remember(allChannels) {
        allChannels.filter { it.category == ChannelCategory.MOVIES }
    }

    val newsChannels = remember(allChannels) {
        allChannels.filter { it.category == ChannelCategory.NEWS }
    }

    val sportsChannels = remember(allChannels) {
        allChannels.filter { it.category == ChannelCategory.SPORTS }
    }

    val kidsChannels = remember(allChannels) {
        allChannels.filter { it.category == ChannelCategory.KIDS }
    }

    val musicChannels = remember(allChannels) {
        allChannels.filter { it.category == ChannelCategory.MUSIC }
    }

    val devotionalChannels = remember(allChannels) {
        allChannels.filter { it.category == ChannelCategory.DEVOTIONAL }
    }

    val ftaChannels = remember(allChannels) {
        allChannels.filter { it.category == ChannelCategory.INDIA_FTA }
    }

    val regionalChannels = remember(allChannels) {
        allChannels.filter { it.category == ChannelCategory.REGIONAL }
    }

    val infotainmentChannels = remember(allChannels) {
        allChannels.filter { it.category == ChannelCategory.INFOTAINMENT }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen_column"),
        contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 8.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Category Folder / Filter Chips Carousel (Shown exclusively on Home page)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ChannelCategory.values().forEach { cat ->
                    val isSelected = selectedCategory == cat
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = if (isSelected) RamLimeAccent else RamSurfaceVariant,
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .clickable { onCategorySelect(cat) }
                            .testTag("category_filter_${cat.name}")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                        ) {
                            Text(text = cat.icon, fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = cat.displayName,
                                color = if (isSelected) Color.Black else RamTextPrimary,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // Active Search Results View
        if (searchQuery.isNotBlank()) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Search: \"$searchQuery\"",
                        color = RamTextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${channels.size} Found",
                        color = RamLimeAccent,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (channels.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No channels match your search. Try another name or category.",
                            color = RamTextSecondary,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                items(channels, key = { "${it.id}_${it.serialNumber}" }) { ch ->
                    RamChannelCard(
                        channel = ch,
                        isActive = activeChannel.id == ch.id,
                        isFavorite = favoriteIds.contains(ch.id),
                        onSelect = { onChannelSelect(ch, true) }, // Direct play!
                        onToggleFavorite = { onToggleFavorite(ch.id) }
                    )
                }
            }
        } else if (selectedCategory != ChannelCategory.ALL) {
            // Selected specific category view: list all channels in this category
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = selectedCategory.icon, fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = selectedCategory.displayName,
                            color = RamTextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "${channels.size} Channels",
                        color = RamLimeAccent,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            items(channels, key = { "${it.id}_${it.serialNumber}" }) { ch ->
                RamChannelCard(
                    channel = ch,
                    isActive = activeChannel.id == ch.id,
                    isFavorite = favoriteIds.contains(ch.id),
                    onSelect = { onChannelSelect(ch, true) }, // Direct play!
                    onToggleFavorite = { onToggleFavorite(ch.id) }
                )
            }
        } else {
            // Main Home Screen: Categories by Channel (Previous Play, Odia, Entertainment, Movies, News, Sports, etc.)
            
            // Hero Featured Spotlight
            item {
                RamHeroFeaturedCard(
                    channel = featuredChannel,
                    isMuted = isMuted,
                    onPlay = { onChannelSelect(featuredChannel, true) },
                    onToggleMute = onToggleMute
                )
            }

            // 1. Previous Play (left corner title, right corner view all, bottom line 5+ tv show)
            val prevPlayList = if (recentlyPlayed.isNotEmpty()) recentlyPlayed else allChannels.take(8)
            item {
                HomeCategoryShelfRow(
                    title = "Previous Play (ପୂର୍ବରୁ ଚାଲୁଥିବା)",
                    icon = "🕒",
                    channels = prevPlayList,
                    activeChannelId = activeChannel.id,
                    favoriteIds = favoriteIds,
                    onViewAll = { onCategorySelect(ChannelCategory.FEATURED) },
                    onDirectPlay = { onChannelSelect(it, true) },
                    onToggleFavorite = onToggleFavorite
                )
            }

            // 2. Favorites Shelf (if user has any favorites, or top picks)
            val favDisplayList = if (favoriteChannels.isNotEmpty()) favoriteChannels else allChannels.filter { it.isFeatured }
            item {
                HomeCategoryShelfRow(
                    title = "Favorites (ପସନ୍ଦିତା)",
                    icon = "⭐",
                    channels = favDisplayList,
                    activeChannelId = activeChannel.id,
                    favoriteIds = favoriteIds,
                    onViewAll = { onCategorySelect(ChannelCategory.FAVORITES) },
                    onDirectPlay = { onChannelSelect(it, true) },
                    onToggleFavorite = onToggleFavorite
                )
            }

            // 3. Odia Regional (ଓଡ଼ିଆ)
            if (odiaChannels.isNotEmpty()) {
                item {
                    HomeCategoryShelfRow(
                        title = "Odia Regional (ଓଡ଼ିଆ ଟିଭି)",
                        icon = "🪔",
                        channels = odiaChannels,
                        activeChannelId = activeChannel.id,
                        favoriteIds = favoriteIds,
                        onViewAll = { onCategorySelect(ChannelCategory.ODIA) },
                        onDirectPlay = { onChannelSelect(it, true) },
                        onToggleFavorite = onToggleFavorite
                    )
                }
            }

            // 4. Entertainment (ମନୋରଞ୍ଜନ)
            if (entertainmentChannels.isNotEmpty()) {
                item {
                    HomeCategoryShelfRow(
                        title = "Entertainment (ମନୋରଞ୍ଜନ)",
                        icon = "🎭",
                        channels = entertainmentChannels,
                        activeChannelId = activeChannel.id,
                        favoriteIds = favoriteIds,
                        onViewAll = { onCategorySelect(ChannelCategory.ENTERTAINMENT) },
                        onDirectPlay = { onChannelSelect(it, true) },
                        onToggleFavorite = onToggleFavorite
                    )
                }
            }

            // 5. Movies (ସିନେମା)
            if (movieChannels.isNotEmpty()) {
                item {
                    HomeCategoryShelfRow(
                        title = "Movies & Cinema (ସିନେମା)",
                        icon = "🎬",
                        channels = movieChannels,
                        activeChannelId = activeChannel.id,
                        favoriteIds = favoriteIds,
                        onViewAll = { onCategorySelect(ChannelCategory.MOVIES) },
                        onDirectPlay = { onChannelSelect(it, true) },
                        onToggleFavorite = onToggleFavorite
                    )
                }
            }

            // 6. News (ଖବର)
            if (newsChannels.isNotEmpty()) {
                item {
                    HomeCategoryShelfRow(
                        title = "Live News (ଖବର)",
                        icon = "📰",
                        channels = newsChannels,
                        activeChannelId = activeChannel.id,
                        favoriteIds = favoriteIds,
                        onViewAll = { onCategorySelect(ChannelCategory.NEWS) },
                        onDirectPlay = { onChannelSelect(it, true) },
                        onToggleFavorite = onToggleFavorite
                    )
                }
            }

            // 7. Sports (ଖେଳ)
            if (sportsChannels.isNotEmpty()) {
                item {
                    HomeCategoryShelfRow(
                        title = "Sports Live (ଖେଳ)",
                        icon = "🏏",
                        channels = sportsChannels,
                        activeChannelId = activeChannel.id,
                        favoriteIds = favoriteIds,
                        onViewAll = { onCategorySelect(ChannelCategory.SPORTS) },
                        onDirectPlay = { onChannelSelect(it, true) },
                        onToggleFavorite = onToggleFavorite
                    )
                }
            }

            // 8. Kids & Cartoons (କାର୍ଟୁନ୍)
            if (kidsChannels.isNotEmpty()) {
                item {
                    HomeCategoryShelfRow(
                        title = "Kids & Cartoons (କାର୍ଟୁନ୍)",
                        icon = "🎨",
                        channels = kidsChannels,
                        activeChannelId = activeChannel.id,
                        favoriteIds = favoriteIds,
                        onViewAll = { onCategorySelect(ChannelCategory.KIDS) },
                        onDirectPlay = { onChannelSelect(it, true) },
                        onToggleFavorite = onToggleFavorite
                    )
                }
            }

            // 9. 24/7 Music (ସଙ୍ଗୀତ)
            if (musicChannels.isNotEmpty()) {
                item {
                    HomeCategoryShelfRow(
                        title = "24/7 Music (ସଙ୍ଗୀତ)",
                        icon = "🎵",
                        channels = musicChannels,
                        activeChannelId = activeChannel.id,
                        favoriteIds = favoriteIds,
                        onViewAll = { onCategorySelect(ChannelCategory.MUSIC) },
                        onDirectPlay = { onChannelSelect(it, true) },
                        onToggleFavorite = onToggleFavorite
                    )
                }
            }

            // 10. Devotional & Darshan (ଭକ୍ତି)
            if (devotionalChannels.isNotEmpty()) {
                item {
                    HomeCategoryShelfRow(
                        title = "Devotional & Darshan (ଭକ୍ତି)",
                        icon = "🕉️",
                        channels = devotionalChannels,
                        activeChannelId = activeChannel.id,
                        favoriteIds = favoriteIds,
                        onViewAll = { onCategorySelect(ChannelCategory.DEVOTIONAL) },
                        onDirectPlay = { onChannelSelect(it, true) },
                        onToggleFavorite = onToggleFavorite
                    )
                }
            }

            // 11. India All FTA (ସରକାରୀ ଫ୍ରି ଟିଭି)
            if (ftaChannels.isNotEmpty()) {
                item {
                    HomeCategoryShelfRow(
                        title = "India All FTA (ସରକାରୀ ଟିଭି)",
                        icon = "🇮🇳",
                        channels = ftaChannels,
                        activeChannelId = activeChannel.id,
                        favoriteIds = favoriteIds,
                        onViewAll = { onCategorySelect(ChannelCategory.INDIA_FTA) },
                        onDirectPlay = { onChannelSelect(it, true) },
                        onToggleFavorite = onToggleFavorite
                    )
                }
            }

            // 12. Regional Multi-Language (ଆଞ୍ଚଳିକ)
            if (regionalChannels.isNotEmpty()) {
                item {
                    HomeCategoryShelfRow(
                        title = "Regional India (ଆଞ୍ଚଳିକ)",
                        icon = "🌐",
                        channels = regionalChannels,
                        activeChannelId = activeChannel.id,
                        favoriteIds = favoriteIds,
                        onViewAll = { onCategorySelect(ChannelCategory.REGIONAL) },
                        onDirectPlay = { onChannelSelect(it, true) },
                        onToggleFavorite = onToggleFavorite
                    )
                }
            }

            // 13. Infotainment & Wildlife (ବିଜ୍ଞାନ ଓ ପ୍ରକୃତି)
            if (infotainmentChannels.isNotEmpty()) {
                item {
                    HomeCategoryShelfRow(
                        title = "Infotainment & Wildlife",
                        icon = "🔬",
                        channels = infotainmentChannels,
                        activeChannelId = activeChannel.id,
                        favoriteIds = favoriteIds,
                        onViewAll = { onCategorySelect(ChannelCategory.INFOTAINMENT) },
                        onDirectPlay = { onChannelSelect(it, true) },
                        onToggleFavorite = onToggleFavorite
                    )
                }
            }
        }
    }
}

/**
 * Reusable Category Shelf Row:
 * - Left corner: Category Title + Icon
 * - Right corner: "View All ▶" button
 * - Bottom line: Horizontal LazyRow showing 5+ channel cards with smooth direct play
 */
@Composable
fun HomeCategoryShelfRow(
    title: String,
    icon: String,
    channels: List<RamChannel>,
    activeChannelId: String,
    favoriteIds: Set<String>,
    onViewAll: () -> Unit,
    onDirectPlay: (RamChannel) -> Unit,
    onToggleFavorite: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Shelf Header (Left: Title, Right: View All)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = icon, fontSize = 16.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = title,
                    color = RamTextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "(${channels.size})",
                    color = RamTextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // View All button (Right Corner)
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = RamSurfaceVariant,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onViewAll() }
                    .testTag("shelf_view_all_${title}")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "View All",
                        color = RamLimeAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "View All",
                        tint = RamLimeAccent,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }

        // Horizontal Row of 5+ TV Shows / Channels
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
            items(channels.take(15), key = { "${it.id}_${it.serialNumber}" }) { channel ->
                RamHorizontalChannelCard(
                    channel = channel,
                    isActive = activeChannelId == channel.id,
                    isFavorite = favoriteIds.contains(channel.id),
                    onDirectPlay = { onDirectPlay(channel) }, // Instant direct playback
                    onToggleFavorite = { onToggleFavorite(channel.id) }
                )
            }
        }
    }
}
