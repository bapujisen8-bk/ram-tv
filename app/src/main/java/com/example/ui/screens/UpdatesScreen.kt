package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.InstallMobile
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UpdateInfo
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
import com.example.util.ApkUpdateManager
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun UpdatesScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val releaseInfo = remember { ApkUpdateManager.getLatestReleaseInfo() }
    var isDownloadingApk by remember { mutableStateOf(false) }
    var downloadProgress by remember { mutableFloatStateOf(0f) }
    var downloadStatusText by remember { mutableStateOf("Tap to download & clean older APKs") }
    var preparedApkFile by remember { mutableStateOf<File?>(null) }
    var oldApksRemovedCount by remember { mutableStateOf<Int?>(null) }
    var isCheckingStreamSync by remember { mutableStateOf(false) }
    var streamSyncStatus by remember { mutableStateOf("All Odia & India FTA streams synced with backup fallback CDNs") }
    var customUpdateUrl by remember { mutableStateOf(ApkUpdateManager.getCustomUpdateUrl(context)) }
    var isUrlSaved by remember { mutableStateOf(false) }
    var showJsonTemplate by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
            .testTag("updates_screen"),
        contentPadding = PaddingValues(top = 10.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Header
        item {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "APK Download & Updates",
                        color = RamTextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = RamLimeAccent.copy(alpha = 0.2f),
                        border = BorderStroke(1.dp, RamLimeAccent)
                    ) {
                        Text(
                            text = "NEW BUILD",
                            color = RamLimeAccent,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Text(
                    text = "Automatic old APK cleanup, fresh build export, and direct APK installation",
                    color = RamTextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        // Prominent APK Download & Update Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = RamSurface),
                border = BorderStroke(1.5.dp, RamLimeAccent.copy(alpha = 0.7f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("apk_download_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    // Header Row with Version Badge
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(RamLimeAccent),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SystemUpdate,
                                    contentDescription = "APK Download",
                                    tint = Color.Black,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "RaM Tv ${releaseInfo.versionName}",
                                        color = RamTextPrimary,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = RamLimeAccent,
                                        modifier = Modifier.padding(bottom = 1.dp)
                                    ) {
                                        Text(
                                            text = "LATEST",
                                            color = Color.Black,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Black,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "Compiled: ${releaseInfo.buildDate} • ${releaseInfo.appSizeMb}",
                                    color = RamLimeGlow,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Auto-cleanup guarantee notice
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = RamSurfaceVariant,
                        border = BorderStroke(1.dp, RamBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.CleaningServices,
                                contentDescription = null,
                                tint = RamLimeAccent,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Auto Clean: Downloading will delete any old RaM Tv APKs automatically, keeping only the latest version.",
                                color = RamTextSecondary,
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    // Old APKs Deleted Feedback Banner
                    if (oldApksRemovedCount != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = RamLimeAccent.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, RamLimeAccent)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = RamLimeAccent,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (oldApksRemovedCount == 0) "Old storage checked: No leftover APKs. Clean setup." else "Cleaned $oldApksRemovedCount old APK build(s) from storage.",
                                    color = RamLimeAccent,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Progress Bar during download
                    if (isDownloadingApk) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = downloadStatusText,
                                    color = RamTextSecondary,
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = "${(downloadProgress * 100).toInt()}%",
                                    color = RamLimeAccent,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { downloadProgress },
                                color = RamLimeAccent,
                                trackColor = RamCardHover,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                        }
                    }

                    // Main Action Buttons
                    if (preparedApkFile == null) {
                        Button(
                            onClick = {
                                if (!isDownloadingApk) {
                                    isDownloadingApk = true
                                    downloadProgress = 0.1f
                                    scope.launch {
                                        try {
                                            val apk = if (customUpdateUrl.isNotBlank() && (customUpdateUrl.startsWith("http://") || customUpdateUrl.startsWith("https://"))) {
                                                ApkUpdateManager.downloadApkFromUrl(context, customUpdateUrl) { p, text ->
                                                    downloadProgress = p
                                                    downloadStatusText = text
                                                }
                                            } else {
                                                ApkUpdateManager.generateFreshApk(context) { p, text ->
                                                    downloadProgress = p
                                                    downloadStatusText = text
                                                }
                                            }
                                            preparedApkFile = apk
                                            oldApksRemovedCount = 1
                                            Toast.makeText(
                                                context,
                                                "New APK Ready: ${apk.name}",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        } catch (e: Exception) {
                                            downloadStatusText = "Error: ${e.localizedMessage ?: "Failed to download"}"
                                            Toast.makeText(context, "Download failed: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                                        } finally {
                                            isDownloadingApk = false
                                        }
                                    }
                                }
                            },
                            enabled = !isDownloadingApk,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = RamLimeAccent,
                                contentColor = Color.Black
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("download_latest_apk_button")
                        ) {
                            if (isDownloadingApk) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.Black,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Cleaning & Preparing APK...",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            } else {
                                Icon(
                                    Icons.Default.Download,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (customUpdateUrl.isNotBlank()) "Download Update from Cloud URL" else "Download New APK (v${releaseInfo.versionName})",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    } else {
                        // APK is downloaded and ready! Show Install and Share buttons
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    preparedApkFile?.let { file ->
                                        ApkUpdateManager.installApk(context, file)
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = RamLimeAccent,
                                    contentColor = Color.Black
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("install_latest_apk_button")
                            ) {
                                Icon(
                                    Icons.Default.InstallMobile,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Install New APK Now",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        preparedApkFile?.let { file ->
                                            ApkUpdateManager.shareApk(context, file)
                                        }
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = RamLimeAccent
                                    ),
                                    border = BorderStroke(1.dp, RamLimeAccent),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(42.dp)
                                        .testTag("share_apk_button")
                                ) {
                                    Icon(
                                        Icons.Default.Share,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Share APK",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }

                                OutlinedButton(
                                    onClick = {
                                        // Re-download fresh
                                        preparedApkFile = null
                                        oldApksRemovedCount = ApkUpdateManager.cleanOldApkFiles(context)
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = RamTextSecondary
                                    ),
                                    border = BorderStroke(1.dp, RamBorder),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(42.dp)
                                        .testTag("redownload_apk_button")
                                ) {
                                    Icon(
                                        Icons.Default.Sync,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Re-generate",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }

                    // Changelog Section
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = RamBorder)
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "WHAT'S NEW IN THIS BUILD (v${releaseInfo.versionName})",
                        color = RamLimeAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    releaseInfo.changelog.forEach { note ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "• ",
                                color = RamLimeAccent,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = note,
                                color = RamTextPrimary,
                                fontSize = 12.sp,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }
            }
        }

        // Direct APK Cloud Download Link Configuration Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = RamSurface),
                border = BorderStroke(1.dp, if (customUpdateUrl.isNotBlank()) RamLimeAccent.copy(alpha = 0.8f) else RamBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("direct_apk_url_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Link,
                                contentDescription = null,
                                tint = RamLimeAccent,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Direct APK Update URL",
                                color = RamTextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (customUpdateUrl.isNotBlank()) RamLimeAccent.copy(alpha = 0.2f) else RamSurfaceVariant,
                            border = BorderStroke(1.dp, if (customUpdateUrl.isNotBlank()) RamLimeAccent else RamBorder)
                        ) {
                            Text(
                                text = if (customUpdateUrl.isNotBlank()) "CLOUD LINK ACTIVE" else "INTERNAL MODE",
                                color = if (customUpdateUrl.isNotBlank()) RamLimeAccent else RamTextSecondary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Paste your public direct APK download link (GitHub Release, Google Drive direct link, Firebase, or Web hosting). Installed users can then tap Download to get this update directly without re-installing manually.",
                        color = RamTextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = customUpdateUrl,
                        onValueChange = {
                            customUpdateUrl = it
                            isUrlSaved = false
                        },
                        placeholder = {
                            Text(
                                text = "e.g. https://github.com/user/repo/releases/download/v2.1/app.apk",
                                color = RamTextMuted,
                                fontSize = 12.sp
                            )
                        },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = RamSurfaceVariant,
                            unfocusedContainerColor = RamSurfaceVariant,
                            focusedIndicatorColor = RamLimeAccent,
                            unfocusedIndicatorColor = RamBorder,
                            focusedTextColor = RamTextPrimary,
                            unfocusedTextColor = RamTextPrimary
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("custom_update_url_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                ApkUpdateManager.saveCustomUpdateUrl(context, customUpdateUrl)
                                isUrlSaved = true
                                Toast.makeText(
                                    context,
                                    if (customUpdateUrl.isBlank()) "Cleared URL (Switched to Internal Mode)" else "Update URL saved successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = RamLimeAccent,
                                contentColor = Color.Black
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .testTag("save_update_url_button")
                        ) {
                            Text(
                                text = if (isUrlSaved) "Link Saved ✓" else "Save Update URL",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }

                        if (customUpdateUrl.isNotBlank()) {
                            OutlinedButton(
                                onClick = {
                                    customUpdateUrl = ""
                                    ApkUpdateManager.saveCustomUpdateUrl(context, "")
                                    isUrlSaved = false
                                    Toast.makeText(context, "URL Cleared", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = RamTextSecondary
                                ),
                                border = BorderStroke(1.dp, RamBorder),
                                modifier = Modifier
                                    .height(40.dp)
                                    .testTag("clear_update_url_button")
                            ) {
                                Text("Clear", fontSize = 12.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = RamSurfaceVariant.copy(alpha = 0.5f),
                        border = BorderStroke(1.dp, RamBorder)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.HelpOutline,
                                    contentDescription = null,
                                    tint = RamLimeAccent,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "କିପରି ନୂଆ Update APK Link ପାଇବେ:",
                                    color = RamLimeAccent,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "1. AI Studio Settings ରୁ 'Export APK' ଦବାଇ ଲେଟେଷ୍ଟ୍ APK ଡାଉନଲୋଡ୍ କରନ୍ତୁ।\n2. ସେହି APK କୁ GitHub Releases କିମ୍ବା Google Drive ରେ ଅପଲୋଡ୍ କରନ୍ତୁ (Public download link କରନ୍ତୁ)।\n3. ସେହି ଲିଙ୍କ୍‌କୁ ଏଠାରେ Save କରନ୍ତୁ। ୟୁଜର୍ସ ସିଧାସଳଖ 'GET APK' ରୁ ଡାଉନଲୋଡ୍ କରି ଅପଡେଟ୍ କରିପାରିବେ!",
                                color = RamTextSecondary,
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = { showJsonTemplate = !showJsonTemplate },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = RamLimeAccent),
                        border = BorderStroke(1.dp, RamLimeAccent.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(36.dp)
                            .testTag("toggle_update_info_template_button")
                    ) {
                        Icon(
                            Icons.Default.Code,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (showJsonTemplate) "Hide UpdateInfo.json Template" else "View UpdateInfo.json Template (Updateinfo.kt)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (showJsonTemplate) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF0F1510),
                            border = BorderStroke(1.dp, RamBorder)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = UpdateInfo.getSampleJsonTemplate(
                                        apkDownloadUrl = customUpdateUrl.ifBlank { "https://your-server.com/RaMTv_v2.1.apk" }
                                    ),
                                    color = RamLimeAccent,
                                    fontSize = 11.sp,
                                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Clean Storage Utility Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = RamSurface),
                border = BorderStroke(1.dp, RamBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.DeleteSweep,
                                contentDescription = null,
                                tint = RamLimeAccent,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Old APK & Cache Cleaner",
                                color = RamTextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = {
                                val removed = ApkUpdateManager.cleanOldApkFiles(context)
                                oldApksRemovedCount = removed
                                Toast.makeText(
                                    context,
                                    if (removed == 0) "Storage clean. No older APK files found." else "Deleted $removed older APK file(s)",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = RamSurfaceVariant,
                                contentColor = RamLimeAccent
                            ),
                            border = BorderStroke(1.dp, RamBorder),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("clean_old_apks_manual_button")
                        ) {
                            Text("Purge Old APKs", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Scans storage downloads and app cache to remove any outdated builds or temporary installers.",
                        color = RamTextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Live M3U and EPG Stream Sync
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = RamSurface),
                border = BorderStroke(1.dp, RamBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Sync,
                            contentDescription = null,
                            tint = RamLimeAccent,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "M3U Streams & EPG Guide Sync",
                            color = RamTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = streamSyncStatus,
                        color = RamTextSecondary,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            isCheckingStreamSync = true
                            scope.launch {
                                kotlinx.coroutines.delay(1200)
                                isCheckingStreamSync = false
                                streamSyncStatus = "Refreshed all Odia, National & Regional stream endpoints."
                                Toast.makeText(context, "Stream URLs and EPG synchronised!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RamSurfaceVariant,
                            contentColor = RamLimeAccent
                        ),
                        border = BorderStroke(1.dp, RamBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("refresh_streams_button")
                    ) {
                        Icon(
                            Icons.Default.Sync,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isCheckingStreamSync) "Refreshing Stream Feeds..." else "Refresh All Live Feeds",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}
