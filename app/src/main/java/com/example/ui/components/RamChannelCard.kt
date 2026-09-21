package com.example.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.runtime.remember
import com.example.model.ChannelCategory
import com.example.model.RamChannel
import com.example.ui.theme.RamBorder
import com.example.ui.theme.RamCardHover
import com.example.ui.theme.RamCatEntertainment
import com.example.ui.theme.RamCatMovies
import com.example.ui.theme.RamCatMusic
import com.example.ui.theme.RamCatNews
import com.example.ui.theme.RamCatOdia
import com.example.ui.theme.RamCatScience
import com.example.ui.theme.RamCatSports
import com.example.ui.theme.RamGoldStar
import com.example.ui.theme.RamLimeAccent
import com.example.ui.theme.RamLimeDark
import com.example.ui.theme.RamLimeGlow
import com.example.ui.theme.RamLiveRed
import com.example.ui.theme.RamSurface
import com.example.ui.theme.RamSurfaceVariant
import com.example.ui.theme.RamTextMuted
import com.example.ui.theme.RamTextPrimary
import com.example.ui.theme.RamTextSecondary

fun getCategoryGradient(category: ChannelCategory): Brush {
    return when (category) {
        ChannelCategory.ODIA -> Brush.linearGradient(listOf(RamCatOdia, Color(0xFFC2410C)))
        ChannelCategory.INDIA_FTA -> Brush.linearGradient(listOf(Color(0xFFFF9933), Color(0xFF138808)))
        ChannelCategory.NEWS -> Brush.linearGradient(listOf(RamCatNews, Color(0xFF1D4ED8)))
        ChannelCategory.SPORTS -> Brush.linearGradient(listOf(RamCatSports, Color(0xFF15803D)))
        ChannelCategory.MOVIES -> Brush.linearGradient(listOf(RamCatMovies, Color(0xFF9D174D)))
        ChannelCategory.ENTERTAINMENT -> Brush.linearGradient(listOf(RamCatEntertainment, Color(0xFFB45309)))
        ChannelCategory.MUSIC -> Brush.linearGradient(listOf(RamCatMusic, Color(0xFF6B21A8)))
        ChannelCategory.DEVOTIONAL -> Brush.linearGradient(listOf(Color(0xFFE11D48), Color(0xFFF59E0B)))
        ChannelCategory.REGIONAL -> Brush.linearGradient(listOf(Color(0xFF0D9488), Color(0xFF0284C7)))
        ChannelCategory.KIDS -> Brush.linearGradient(listOf(Color(0xFFF43F5E), Color(0xFF8B5CF6)))
        ChannelCategory.INFOTAINMENT -> Brush.linearGradient(listOf(Color(0xFF059669), Color(0xFF3B82F6)))
        else -> Brush.linearGradient(listOf(RamLimeDark, Color(0xFF3F6212)))
    }
}

@Composable
fun RamLiveIndicator(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Surface(
        shape = RoundedCornerShape(6.dp),
        color = RamLimeAccent.copy(alpha = 0.2f),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(RamLimeAccent.copy(alpha = alpha))
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "LIVE",
                color = RamLimeAccent,
                fontSize = 9.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
fun RamChannelCard(
    channel: RamChannel,
    isActive: Boolean,
    isFavorite: Boolean,
    onSelect: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(interactionSource = interactionSource, indication = null) { onSelect() }
            .focusable(interactionSource = interactionSource)
            .testTag("channel_card_${channel.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isFocused -> RamCardHover
                isActive -> RamSurfaceVariant
                else -> RamSurface
            }
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = when {
                isFocused -> 2.5.dp
                isActive -> 1.5.dp
                else -> 1.dp
            },
            color = when {
                isFocused -> RamLimeAccent
                isActive -> RamLimeDark
                else -> RamBorder
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Top Row: Logo Badge, Name & Live pill + Fav
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Channel Logo Badge with Channel Serial Number Pill
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(getCategoryGradient(channel.category)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = channel.logoText,
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "#${channel.serialNumber}",
                            color = RamLimeAccent,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Titles & Meta
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Channel remote code badge (e.g., CH 101)
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = RamLimeAccent.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "CH ${channel.serialNumber}",
                                color = RamLimeAccent,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }

                        Text(
                            text = channel.name,
                            color = RamTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        RamLiveIndicator()
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = channel.language,
                            color = RamTextSecondary,
                            fontSize = 11.sp
                        )
                        Text(text = "•", color = RamTextMuted, fontSize = 11.sp)
                        Text(
                            text = channel.resolution,
                            color = RamLimeGlow,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Favorite Star
                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("favorite_button_${channel.id}")
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Star else Icons.Outlined.StarBorder,
                        contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                        tint = if (isFavorite) RamGoldStar else RamTextMuted,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Now Playing Show Details & Category
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = channel.currentShow,
                        color = if (isActive) RamLimeAccent else RamTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = channel.currentShowCategory,
                        color = RamTextMuted,
                        fontSize = 11.sp
                    )
                }

                // Viewers pill
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        Icons.Default.Visibility,
                        contentDescription = "Viewers",
                        tint = RamTextSecondary,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = channel.viewersCount,
                        color = RamTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Program Progress Bar
            LinearProgressIndicator(
                progress = { channel.progressPercent },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(RoundedCornerShape(1.5.dp)),
                color = if (isActive) RamLimeAccent else RamLimeDark,
                trackColor = RamCardHover
            )
        }
    }
}
