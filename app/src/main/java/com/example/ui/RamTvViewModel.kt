package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.RamCustomM3uPlaylist
import com.example.data.RamRepository
import com.example.model.ChannelCategory
import com.example.model.DEMO_CHANNELS
import com.example.model.RamChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppNavDestination(val title: String, val iconName: String) {
    HOME("LIVE TV", "live_tv"),
    FAVORITES("FAVORITES", "star"),
    CATEGORY("CATEGORY", "category"),
    UPDATES("GET APK", "download"),
    SETTINGS("SETTING", "settings"),
    PLAYER("PLAYER", "play_circle")
}

data class PlaybackSettings(
    val streamQuality: String = "1080p FHD",
    val lowLatencyMode: Boolean = true,
    val hardwareAcceleration: Boolean = true,
    val backgroundAudio: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val autoPlayOnStart: Boolean = true
)

class RamTvViewModel(private val repository: RamRepository) : ViewModel() {

    // Navigation
    private val _currentDestination = MutableStateFlow(AppNavDestination.HOME)
    val currentDestination: StateFlow<AppNavDestination> = _currentDestination

    // Channels & Catalog
    private val _channels = MutableStateFlow<List<RamChannel>>(DEMO_CHANNELS)
    val channels: StateFlow<List<RamChannel>> = _channels

    // Currently playing channel
    private val _activeChannel = MutableStateFlow<RamChannel>(DEMO_CHANNELS.first())
    val activeChannel: StateFlow<RamChannel> = _activeChannel

    // Recently played channels for "Previous Play" horizontal shelf
    private val _recentlyPlayed = MutableStateFlow<List<RamChannel>>(DEMO_CHANNELS.take(8))
    val recentlyPlayed: StateFlow<List<RamChannel>> = _recentlyPlayed

    // Category and Search Filtering
    private val _selectedCategory = MutableStateFlow(ChannelCategory.ALL)
    val selectedCategory: StateFlow<ChannelCategory> = _selectedCategory

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    // Favorites from Room
    val favoriteIds: StateFlow<Set<String>> = repository.favoritesFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptySet())

    // Custom M3U Playlists
    val m3uPlaylists: StateFlow<List<RamCustomM3uPlaylist>> = repository.m3uPlaylistsFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // Filtered channel list
    val filteredChannels: StateFlow<List<RamChannel>> = combine(
        _channels,
        _selectedCategory,
        _searchQuery,
        favoriteIds
    ) { all, category, query, favorites ->
        var list = all

        // Category filter
        list = when (category) {
            ChannelCategory.ALL -> list
            ChannelCategory.FEATURED -> list.filter { it.isFeatured }
            ChannelCategory.FAVORITES -> list.filter { favorites.contains(it.id) }
            else -> list.filter { it.category == category }
        }

        // Search Query (Supports name, show, language, category, and remote code number)
        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            list = list.filter {
                it.serialNumber.toString().contains(q) ||
                it.name.lowercase().contains(q) ||
                it.currentShow.lowercase().contains(q) ||
                it.language.lowercase().contains(q) ||
                it.category.displayName.lowercase().contains(q)
            }
        }

        list
    }.stateIn(viewModelScope, SharingStarted.Eagerly, DEMO_CHANNELS)

    // Settings
    private val _playbackSettings = MutableStateFlow(PlaybackSettings())
    val playbackSettings: StateFlow<PlaybackSettings> = _playbackSettings

    // Player state
    private val _isPlaying = MutableStateFlow(true)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _isMuted = MutableStateFlow(false)
    val isMuted: StateFlow<Boolean> = _isMuted

    fun navigateTo(destination: AppNavDestination) {
        _currentDestination.value = destination
    }

    // Remote Channel Code Buffer for TV remote number dialing
    private val _remoteCodeBuffer = MutableStateFlow("")
    val remoteCodeBuffer: StateFlow<String> = _remoteCodeBuffer

    private val _remoteCodeNotification = MutableStateFlow<String?>(null)
    val remoteCodeNotification: StateFlow<String?> = _remoteCodeNotification

    fun handleRemoteDigit(digit: Char) {
        if (!digit.isDigit()) return
        val current = _remoteCodeBuffer.value + digit
        if (current.length > 3) {
            _remoteCodeBuffer.value = digit.toString()
        } else {
            _remoteCodeBuffer.value = current
        }

        val codeInt = _remoteCodeBuffer.value.toIntOrNull()
        if (codeInt != null) {
            val matched = _channels.value.firstOrNull { it.serialNumber == codeInt }
            if (matched != null) {
                _remoteCodeNotification.value = "Switching to CH ${matched.serialNumber}: ${matched.name}"
                selectChannel(matched, navigateToPlayer = true)
                _remoteCodeBuffer.value = ""
            } else {
                _remoteCodeNotification.value = "Channel $codeInt not found"
            }
        }
    }

    fun clearRemoteCodeBuffer() {
        _remoteCodeBuffer.value = ""
        _remoteCodeNotification.value = null
    }

    fun playBySerialNumber(code: Int): Boolean {
        val matched = _channels.value.firstOrNull { it.serialNumber == code }
        return if (matched != null) {
            selectChannel(matched, navigateToPlayer = true)
            true
        } else {
            false
        }
    }

    fun selectChannel(channel: RamChannel, navigateToPlayer: Boolean = true) {
        _activeChannel.value = channel
        _isPlaying.value = true
        val updated = listOf(channel) + _recentlyPlayed.value.filter { it.id != channel.id }
        _recentlyPlayed.value = updated.take(15)
        if (navigateToPlayer) {
            _currentDestination.value = AppNavDestination.PLAYER
        }
    }

    fun addCustomChannel(channel: RamChannel) {
        _channels.value = listOf(channel) + _channels.value
    }

    fun updateChannelStream(channelId: String, newStreamUrl: String) {
        _channels.value = _channels.value.map {
            if (it.id == channelId) it.copy(streamUrl = newStreamUrl) else it
        }
        if (_activeChannel.value.id == channelId) {
            _activeChannel.value = _activeChannel.value.copy(streamUrl = newStreamUrl)
        }
    }

    fun selectCategory(category: ChannelCategory) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleFavorite(channelId: String) {
        viewModelScope.launch {
            val isCurrent = favoriteIds.value.contains(channelId)
            repository.toggleFavorite(channelId, isCurrent)
        }
    }

    fun togglePlayPause() {
        _isPlaying.value = !_isPlaying.value
    }

    fun toggleMute() {
        _isMuted.value = !_isMuted.value
    }

    fun zapNext() {
        val currentList = filteredChannels.value.ifEmpty { _channels.value }
        if (currentList.isEmpty()) return
        val currentIndex = currentList.indexOfFirst { it.id == _activeChannel.value.id }
        val nextIndex = if (currentIndex in 0 until currentList.lastIndex) currentIndex + 1 else 0
        selectChannel(currentList[nextIndex])
    }

    fun zapPrevious() {
        val currentList = filteredChannels.value.ifEmpty { _channels.value }
        if (currentList.isEmpty()) return
        val currentIndex = currentList.indexOfFirst { it.id == _activeChannel.value.id }
        val prevIndex = if (currentIndex > 0) currentIndex - 1 else currentList.lastIndex
        selectChannel(currentList[prevIndex])
    }

    fun updateStreamQuality(quality: String) {
        _playbackSettings.value = _playbackSettings.value.copy(streamQuality = quality)
        viewModelScope.launch {
            repository.saveSetting("streamQuality", quality)
        }
    }

    fun toggleLowLatency(enabled: Boolean) {
        _playbackSettings.value = _playbackSettings.value.copy(lowLatencyMode = enabled)
        viewModelScope.launch {
            repository.saveSetting("lowLatencyMode", enabled.toString())
        }
    }

    fun toggleHardwareAcceleration(enabled: Boolean) {
        _playbackSettings.value = _playbackSettings.value.copy(hardwareAcceleration = enabled)
        viewModelScope.launch {
            repository.saveSetting("hardwareAcceleration", enabled.toString())
        }
    }

    fun toggleBackgroundAudio(enabled: Boolean) {
        _playbackSettings.value = _playbackSettings.value.copy(backgroundAudio = enabled)
        viewModelScope.launch {
            repository.saveSetting("backgroundAudio", enabled.toString())
        }
    }

    fun toggleNotifications(enabled: Boolean) {
        _playbackSettings.value = _playbackSettings.value.copy(notificationsEnabled = enabled)
        viewModelScope.launch {
            repository.saveSetting("notificationsEnabled", enabled.toString())
        }
    }

    fun addM3uPlaylist(name: String, url: String) {
        viewModelScope.launch {
            repository.addM3uPlaylist(name, url, channelCount = (10..40).random())
        }
    }

    fun deleteM3uPlaylist(id: Long) {
        viewModelScope.launch {
            repository.deleteM3uPlaylist(id)
        }
    }

    fun createAndAddChannel(name: String, categoryName: String, streamUrl: String, logoUrl: String) {
        val cat = when (categoryName.trim().uppercase()) {
            "ODIA" -> ChannelCategory.ODIA
            "MOVIES", "CINEMA" -> ChannelCategory.MOVIES
            "NEWS" -> ChannelCategory.NEWS
            "SPORTS" -> ChannelCategory.SPORTS
            "KIDS", "CARTOON" -> ChannelCategory.KIDS
            "MUSIC" -> ChannelCategory.MUSIC
            "DEVOTIONAL" -> ChannelCategory.DEVOTIONAL
            else -> ChannelCategory.ENTERTAINMENT
        }
        val newCh = RamChannel(
            id = "custom_${System.currentTimeMillis()}",
            serialNumber = _channels.value.size + 101,
            name = name,
            logoText = name.take(3).uppercase(),
            category = cat,
            streamUrl = streamUrl,
            backupStreamUrls = emptyList(),
            viewersCount = "12.4K",
            resolution = "1080p FHD",
            currentShow = "Live Broadcast",
            currentShowCategory = "Live"
        )
        addCustomChannel(newCh)
    }
}

class RamTvViewModelFactory(private val repository: RamRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RamTvViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RamTvViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
