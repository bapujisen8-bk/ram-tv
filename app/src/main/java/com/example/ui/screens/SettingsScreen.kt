package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.RamCustomM3uPlaylist
import com.example.ui.PlaybackSettings
import com.example.ui.components.AddM3uPlaylistDialog
import com.example.ui.theme.RamBorder
import com.example.ui.theme.RamCardHover
import com.example.ui.theme.RamLimeAccent
import com.example.ui.theme.RamLimeGlow
import com.example.ui.theme.RamLiveRed
import com.example.ui.theme.RamSurface
import com.example.ui.theme.RamSurfaceVariant
import com.example.ui.theme.RamTextMuted
import com.example.ui.theme.RamTextPrimary
import com.example.ui.theme.RamTextSecondary

@Composable
fun SettingsScreen(
    settings: PlaybackSettings,
    m3uPlaylists: List<RamCustomM3uPlaylist>,
    onUpdateQuality: (String) -> Unit,
    onToggleLowLatency: (Boolean) -> Unit,
    onToggleHardwareAccel: (Boolean) -> Unit,
    onToggleBackgroundAudio: (Boolean) -> Unit,
    onToggleNotifications: (Boolean) -> Unit,
    onAddM3u: (String, String) -> Unit,
    onDeleteM3u: (Long) -> Unit,
    onAddCustomChannel: (String, String, String, String) -> Unit = { _, _, _, _ -> },
    onUpdateChannelStream: (String, String) -> Unit = { _, _ -> },
    onNavigateToApkDownload: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showAddM3uDialog by remember { mutableStateOf(false) }
    var showAddChannelDialog by remember { mutableStateOf(false) }
    var showEditStreamDialog by remember { mutableStateOf(false) }
    var editChannelId by remember { mutableStateOf("") }
    var editChannelStream by remember { mutableStateOf("") }
    val qualityOptions = listOf("Auto (Adaptive)", "1080p FHD", "720p HD", "480p SD")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
            .testTag("settings_screen"),
        contentPadding = PaddingValues(top = 10.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        item {
            Column {
                Text(
                    text = "App & Stream Settings",
                    color = RamTextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Configure video quality, notifications, and custom M3U playlist feeds",
                    color = RamTextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        // Section 1: Playback Quality
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = RamSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, RamBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Speed,
                            contentDescription = null,
                            tint = RamLimeAccent,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Stream Quality Preset",
                            color = RamTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    qualityOptions.forEach { q ->
                        val isSelected = settings.streamQuality == q
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) RamLimeAccent.copy(alpha = 0.15f) else RamSurfaceVariant,
                            border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, RamLimeAccent) else null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onUpdateQuality(q) }
                                .padding(vertical = 4.dp)
                                .testTag("quality_option_$q")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = q,
                                    color = if (isSelected) RamLimeAccent else RamTextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                                if (isSelected) {
                                    Text(
                                        text = "Active",
                                        color = RamLimeAccent,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section 2: Engine & Playback Toggles
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = RamSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, RamBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Tv,
                            contentDescription = null,
                            tint = RamLimeAccent,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Playback & Hardware Engine",
                            color = RamTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Low Latency
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Low Latency Live Mode", color = RamTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            Text(text = "Reduces live buffer latency for instant sport/news", color = RamTextSecondary, fontSize = 11.sp)
                        }
                        Switch(
                            checked = settings.lowLatencyMode,
                            onCheckedChange = onToggleLowLatency,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.Black,
                                checkedTrackColor = RamLimeAccent,
                                uncheckedTrackColor = RamCardHover
                            ),
                            modifier = Modifier.testTag("switch_low_latency")
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Hardware Acceleration
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Hardware Accelerated Codecs", color = RamTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            Text(text = "Uses GPU decoders for 1080p 60fps smoothness", color = RamTextSecondary, fontSize = 11.sp)
                        }
                        Switch(
                            checked = settings.hardwareAcceleration,
                            onCheckedChange = onToggleHardwareAccel,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.Black,
                                checkedTrackColor = RamLimeAccent,
                                uncheckedTrackColor = RamCardHover
                            ),
                            modifier = Modifier.testTag("switch_hardware_accel")
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Background Audio
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Background Audio Playback", color = RamTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            Text(text = "Keep audio streaming when screen is turned off", color = RamTextSecondary, fontSize = 11.sp)
                        }
                        Switch(
                            checked = settings.backgroundAudio,
                            onCheckedChange = onToggleBackgroundAudio,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.Black,
                                checkedTrackColor = RamLimeAccent,
                                uncheckedTrackColor = RamCardHover
                            ),
                            modifier = Modifier.testTag("switch_background_audio")
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Notifications
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Live Show Notifications", color = RamTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            Text(text = "Get alerts when your favorited channel starts prime show", color = RamTextSecondary, fontSize = 11.sp)
                        }
                        Switch(
                            checked = settings.notificationsEnabled,
                            onCheckedChange = onToggleNotifications,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.Black,
                                checkedTrackColor = RamLimeAccent,
                                uncheckedTrackColor = RamCardHover
                            ),
                            modifier = Modifier.testTag("switch_notifications")
                        )
                    }
                }
            }
        }

        // Section 3: Custom M3U Playlists (Admin/Authorized M3U feeds)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = RamSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, RamBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.FormatListBulleted,
                                contentDescription = null,
                                tint = RamLimeAccent,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Custom M3U Playlists",
                                    color = RamTextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Structure ready for authorized feeds",
                                    color = RamTextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Button(
                            onClick = { showAddM3uDialog = true },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = RamLimeAccent,
                                contentColor = Color.Black
                            ),
                            modifier = Modifier.testTag("add_m3u_playlist_button")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Add M3U", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (m3uPlaylists.isEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = RamSurfaceVariant,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "No custom M3U feeds imported yet.",
                                    color = RamTextSecondary,
                                    fontSize = 12.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Tap 'Add M3U' above to import custom authorized streaming providers.",
                                    color = RamLimeGlow,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            m3uPlaylists.forEach { playlist ->
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = RamSurfaceVariant,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, RamBorder),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = playlist.name,
                                                color = RamTextPrimary,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = playlist.playlistUrl,
                                                color = RamTextMuted,
                                                fontSize = 11.sp,
                                                maxLines = 1
                                            )
                                            Text(
                                                text = "${playlist.channelCount} Channels Connected",
                                                color = RamLimeAccent,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }

                                        IconButton(
                                            onClick = { onDeleteM3u(playlist.id) },
                                            modifier = Modifier.testTag("delete_m3u_${playlist.id}")
                                        ) {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = "Delete",
                                                tint = RamLiveRed.copy(alpha = 0.8f),
                                                modifier = Modifier.size(18.dp)
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

        // Section 4: Admin Folder & Channel Stream Manager
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = RamSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, RamLimeAccent.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.AdminPanelSettings,
                                contentDescription = null,
                                tint = RamLimeAccent,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Admin Stream & Folder Manager",
                                    color = RamTextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Add custom channels or update streams directly",
                                    color = RamTextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Button(
                            onClick = { showAddChannelDialog = true },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = RamLimeAccent,
                                contentColor = Color.Black
                            ),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("admin_add_channel_btn")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("New Channel", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "You can add custom stream files (.m3u8, .mpd) to any category folder (Odia, Entertainment, Movies, News, Sports, etc.) to expand your 400+ channels lineup.",
                        color = RamTextMuted,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        // Section 5: APK Download & Build Information Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = RamSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, RamLimeAccent.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Download,
                                contentDescription = null,
                                tint = RamLimeAccent,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "RaM Tv APK Build v2.0",
                                    color = RamTextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Auto old APK purge enabled",
                                    color = RamLimeGlow,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Button(
                            onClick = onNavigateToApkDownload,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = RamLimeAccent,
                                contentColor = Color.Black
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("settings_download_apk_btn")
                        ) {
                            Text("Download APK", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Whenever changes or new designs are added, downloading a new build will automatically clean out older APK files and export the fresh build ready for install.",
                        color = RamTextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }

    if (showAddM3uDialog) {
        AddM3uPlaylistDialog(
            onDismiss = { showAddM3uDialog = false },
            onAddPlaylist = { name, url ->
                onAddM3u(name, url)
            }
        )
    }

    if (showAddChannelDialog) {
        var newChannelName by remember { mutableStateOf("") }
        var newChannelCategory by remember { mutableStateOf("Odia") }
        var newChannelStream by remember { mutableStateOf("") }
        var newChannelLogo by remember { mutableStateOf("") }

        Dialog(onDismissRequest = { showAddChannelDialog = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = RamSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, RamBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Admin: Add Custom Channel",
                        color = RamTextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Add a new channel stream to any category folder",
                        color = RamTextSecondary,
                        fontSize = 12.sp
                    )

                    OutlinedTextField(
                        value = newChannelName,
                        onValueChange = { newChannelName = it },
                        label = { Text("Channel Name", color = RamTextSecondary) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RamLimeAccent,
                            unfocusedBorderColor = RamBorder,
                            focusedTextColor = RamTextPrimary,
                            unfocusedTextColor = RamTextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = newChannelCategory,
                        onValueChange = { newChannelCategory = it },
                        label = { Text("Category (e.g. Odia, Movies, News)", color = RamTextSecondary) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RamLimeAccent,
                            unfocusedBorderColor = RamBorder,
                            focusedTextColor = RamTextPrimary,
                            unfocusedTextColor = RamTextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = newChannelStream,
                        onValueChange = { newChannelStream = it },
                        label = { Text("Stream URL (.m3u8, .mpd)", color = RamTextSecondary) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RamLimeAccent,
                            unfocusedBorderColor = RamBorder,
                            focusedTextColor = RamTextPrimary,
                            unfocusedTextColor = RamTextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = newChannelLogo,
                        onValueChange = { newChannelLogo = it },
                        label = { Text("Logo URL (optional)", color = RamTextSecondary) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RamLimeAccent,
                            unfocusedBorderColor = RamBorder,
                            focusedTextColor = RamTextPrimary,
                            unfocusedTextColor = RamTextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showAddChannelDialog = false }) {
                            Text("Cancel", color = RamTextSecondary)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (newChannelName.isNotBlank() && newChannelStream.isNotBlank()) {
                                    onAddCustomChannel(
                                        newChannelName.trim(),
                                        newChannelCategory.trim().ifBlank { "Entertainment" },
                                        newChannelStream.trim(),
                                        newChannelLogo.trim()
                                    )
                                    showAddChannelDialog = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = RamLimeAccent,
                                contentColor = Color.Black
                            )
                        ) {
                            Text("Save Channel", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
