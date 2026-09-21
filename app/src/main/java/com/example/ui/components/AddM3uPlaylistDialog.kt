package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.ui.theme.RamBorder
import com.example.ui.theme.RamLimeAccent
import com.example.ui.theme.RamLimeGlow
import com.example.ui.theme.RamSurface
import com.example.ui.theme.RamTextMuted
import com.example.ui.theme.RamTextPrimary
import com.example.ui.theme.RamTextSecondary

@Composable
fun AddM3uPlaylistDialog(
    onDismiss: () -> Unit,
    onAddPlaylist: (name: String, url: String) -> Unit
) {
    var playlistName by remember { mutableStateOf("") }
    var playlistUrl by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("add_m3u_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = RamSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, RamBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.FormatListBulleted,
                            contentDescription = null,
                            tint = RamLimeAccent
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Add M3U Playlist",
                            color = RamTextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_add_m3u_dialog")
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Close",
                            tint = RamTextSecondary
                        )
                    }
                }

                Text(
                    text = "Provide an authorized M3U/M3U8 playlist feed URL to sync external channels into RaM Tv.",
                    color = RamTextSecondary,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                )

                // Quick preset sample button
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = RamLimeAccent.copy(alpha = 0.12f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            playlistName = "Authorized Regional Sports & TV Feed"
                            playlistUrl = "https://iptv-org.github.io/iptv/countries/in.m3u"
                        }
                        .padding(bottom = 12.dp)
                ) {
                    Text(
                        text = "⚡ Tap to auto-fill sample verified playlist",
                        color = RamLimeGlow,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                    )
                }

                // Name field
                OutlinedTextField(
                    value = playlistName,
                    onValueChange = { playlistName = it; errorMsg = null },
                    label = { Text("Playlist Label") },
                    placeholder = { Text("e.g. My Premium Cable Feed") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RamLimeAccent,
                        unfocusedBorderColor = RamBorder,
                        focusedLabelColor = RamLimeAccent,
                        unfocusedLabelColor = RamTextSecondary,
                        focusedTextColor = RamTextPrimary,
                        unfocusedTextColor = RamTextPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("m3u_name_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // URL field
                OutlinedTextField(
                    value = playlistUrl,
                    onValueChange = { playlistUrl = it; errorMsg = null },
                    label = { Text("M3U Playlist URL") },
                    placeholder = { Text("https://example.com/playlist.m3u") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RamLimeAccent,
                        unfocusedBorderColor = RamBorder,
                        focusedLabelColor = RamLimeAccent,
                        unfocusedLabelColor = RamTextSecondary,
                        focusedTextColor = RamTextPrimary,
                        unfocusedTextColor = RamTextPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("m3u_url_input")
                )

                if (errorMsg != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMsg ?: "",
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        val name = playlistName.trim()
                        val url = playlistUrl.trim()
                        if (name.isEmpty()) {
                            errorMsg = "Please enter a playlist name"
                            return@Button
                        }
                        if (url.isEmpty() || (!url.startsWith("http://") && !url.startsWith("https://"))) {
                            errorMsg = "Please enter a valid HTTP/HTTPS URL"
                            return@Button
                        }
                        onAddPlaylist(name, url)
                        onDismiss()
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RamLimeAccent,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_m3u_button")
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(
                        text = "Import M3U Playlist",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
