package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SettingsRemote
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.RamChannel
import com.example.ui.theme.RamBorder
import com.example.ui.theme.RamCardHover
import com.example.ui.theme.RamDarkBackground
import com.example.ui.theme.RamLimeAccent
import com.example.ui.theme.RamLimeDark
import com.example.ui.theme.RamSurface
import com.example.ui.theme.RamSurfaceVariant
import com.example.ui.theme.RamTextMuted
import com.example.ui.theme.RamTextPrimary
import com.example.ui.theme.RamTextSecondary

@Composable
fun RamTvKeypadButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    Surface(
        onClick = onClick,
        interactionSource = interactionSource,
        shape = RoundedCornerShape(10.dp),
        color = if (isFocused) RamLimeAccent else RamSurfaceVariant,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isFocused) RamLimeAccent else RamBorder
        ),
        modifier = modifier
            .size(54.dp)
            .focusable(interactionSource = interactionSource)
            .testTag("keypad_btn_$text")
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                color = if (isFocused) Color.Black else RamTextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * TV Remote Code Dialog: Dial 3-digit channel serial number (e.g. 101, 102, 131, 201...)
 * to jump and play that channel directly from any screen or folder.
 */
@Composable
fun RamTvRemoteCodeDialog(
    allChannels: List<RamChannel>,
    onDismiss: () -> Unit,
    onPlayChannel: (RamChannel) -> Unit
) {
    var codeInput by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val matchedChannel = remember(codeInput) {
        val num = codeInput.toIntOrNull()
        if (num != null) allChannels.firstOrNull { it.serialNumber == num } else null
    }

    // Auto-play as soon as 3 digits are entered and channel matches
    LaunchedEffect(codeInput) {
        if (codeInput.length == 3) {
            val num = codeInput.toIntOrNull()
            if (num != null) {
                val found = allChannels.firstOrNull { it.serialNumber == num }
                if (found != null) {
                    kotlinx.coroutines.delay(200)
                    onPlayChannel(found)
                    onDismiss()
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = RamSurface),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, RamLimeAccent),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .focusRequester(focusRequester)
                .focusable()
                .onKeyEvent { event ->
                    if (event.type == KeyEventType.KeyDown) {
                        when (event.key) {
                            Key.Zero, Key.NumPad0 -> {
                                if (codeInput.length < 3) codeInput += "0" else codeInput = "0"
                                true
                            }
                            Key.One, Key.NumPad1 -> {
                                if (codeInput.length < 3) codeInput += "1" else codeInput = "1"
                                true
                            }
                            Key.Two, Key.NumPad2 -> {
                                if (codeInput.length < 3) codeInput += "2" else codeInput = "2"
                                true
                            }
                            Key.Three, Key.NumPad3 -> {
                                if (codeInput.length < 3) codeInput += "3" else codeInput = "3"
                                true
                            }
                            Key.Four, Key.NumPad4 -> {
                                if (codeInput.length < 3) codeInput += "4" else codeInput = "4"
                                true
                            }
                            Key.Five, Key.NumPad5 -> {
                                if (codeInput.length < 3) codeInput += "5" else codeInput = "5"
                                true
                            }
                            Key.Six, Key.NumPad6 -> {
                                if (codeInput.length < 3) codeInput += "6" else codeInput = "6"
                                true
                            }
                            Key.Seven, Key.NumPad7 -> {
                                if (codeInput.length < 3) codeInput += "7" else codeInput = "7"
                                true
                            }
                            Key.Eight, Key.NumPad8 -> {
                                if (codeInput.length < 3) codeInput += "8" else codeInput = "8"
                                true
                            }
                            Key.Nine, Key.NumPad9 -> {
                                if (codeInput.length < 3) codeInput += "9" else codeInput = "9"
                                true
                            }
                            Key.Enter, Key.NumPadEnter, Key.DirectionCenter -> {
                                if (matchedChannel != null) {
                                    onPlayChannel(matchedChannel)
                                    onDismiss()
                                    true
                                } else false
                            }
                            Key.Backspace -> {
                                if (codeInput.isNotEmpty()) {
                                    codeInput = codeInput.dropLast(1)
                                    true
                                } else false
                            }
                            Key.Escape, Key.Back -> {
                                onDismiss()
                                true
                            }
                            else -> false
                        }
                    } else false
                }
                .testTag("tv_remote_code_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.SettingsRemote,
                            contentDescription = "TV Remote",
                            tint = RamLimeAccent,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "TV Remote Code",
                            color = RamTextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Close",
                            tint = RamTextSecondary
                        )
                    }
                }

                Text(
                    text = "Put any TV Serial Code (101 - 355) to play directly",
                    color = RamTextMuted,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                )

                // Big Dial Display
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = RamDarkBackground,
                    border = androidx.compose.foundation.BorderStroke(1.dp, RamBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "CH",
                            color = RamLimeDark,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (codeInput.isEmpty()) "---" else codeInput,
                            color = RamLimeAccent,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 4.sp
                        )
                        IconButton(
                            onClick = {
                                if (codeInput.isNotEmpty()) {
                                    codeInput = codeInput.dropLast(1)
                                }
                            },
                            enabled = codeInput.isNotEmpty()
                        ) {
                            Icon(
                                Icons.Default.Backspace,
                                contentDescription = "Delete",
                                tint = if (codeInput.isNotEmpty()) RamTextPrimary else RamTextMuted
                            )
                        }
                    }
                }

                // Matched Channel Preview
                if (matchedChannel != null) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = RamCardHover,
                        border = androidx.compose.foundation.BorderStroke(1.dp, RamLimeAccent),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 14.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(getCategoryGradient(matchedChannel.category)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = matchedChannel.logoText,
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = matchedChannel.name,
                                    color = RamTextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${matchedChannel.language} • ${matchedChannel.category.displayName}",
                                    color = RamLimeAccent,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                } else if (codeInput.length >= 3) {
                    Text(
                        text = "No channel found for code #$codeInput",
                        color = Color(0xFFF87171),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                // Dial Pad 1-9, Clear, 0, Play
                val rows = listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf("CLR", "0", "OK")
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    rows.forEach { row ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            row.forEach { key ->
                                when (key) {
                                    "CLR" -> {
                                        Surface(
                                            onClick = { codeInput = "" },
                                            shape = RoundedCornerShape(10.dp),
                                            color = RamSurfaceVariant,
                                            modifier = Modifier.size(54.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Text(
                                                    text = "C",
                                                    color = Color(0xFFF87171),
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                    "OK" -> {
                                        Surface(
                                            onClick = {
                                                if (matchedChannel != null) {
                                                    onPlayChannel(matchedChannel)
                                                    onDismiss()
                                                }
                                            },
                                            shape = RoundedCornerShape(10.dp),
                                            color = if (matchedChannel != null) RamLimeAccent else RamSurfaceVariant,
                                            modifier = Modifier.size(54.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    Icons.Default.PlayArrow,
                                                    contentDescription = "Play",
                                                    tint = if (matchedChannel != null) Color.Black else RamTextMuted
                                                )
                                            }
                                        }
                                    }
                                    else -> {
                                        RamTvKeypadButton(
                                            text = key,
                                            onClick = {
                                                if (codeInput.length < 3) {
                                                    codeInput += key
                                                } else {
                                                    codeInput = key
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Play Button
                Button(
                    onClick = {
                        if (matchedChannel != null) {
                            onPlayChannel(matchedChannel)
                            onDismiss()
                        }
                    },
                    enabled = matchedChannel != null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RamLimeAccent,
                        contentColor = Color.Black,
                        disabledContainerColor = RamSurfaceVariant,
                        disabledContentColor = RamTextMuted
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("remote_code_play_button")
                ) {
                    Text(
                        text = if (matchedChannel != null) "Direct Play CH #${matchedChannel.serialNumber}" else "Enter Valid Code",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
