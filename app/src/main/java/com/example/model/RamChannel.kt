package com.example.model

enum class ChannelCategory(val displayName: String, val icon: String) {
    ALL("All Channels (400+)", "📺"),
    ODIA("Odia Regional (ଓଡ଼ିଆ)", "🪔"),
    INDIA_FTA("India All FTA (ଫ୍ରି)", "🇮🇳"),
    ENTERTAINMENT("Entertainment (ମନୋରଞ୍ଜନ)", "🎭"),
    MOVIES("Cinema & Movies (ଚଳଚ୍ଚିତ୍ର)", "🎬"),
    SPORTS("Sports Live (ଖେଳ)", "⚽"),
    MUSIC("24/7 Music (ସଙ୍ଗୀତ)", "🎵"),
    DEVOTIONAL("Devotional (ଭକ୍ତି)", "🕉️"),
    KIDS("Kids & Cartoon (କାର୍ଟୁନ୍)", "🎈"),
    NEWS("Live News (ଖବର)", "📰"),
    REGIONAL("Regional TV (ଅନ୍ୟ ଭାଷା)", "🌐"),
    INFOTAINMENT("Knowledge & Science", "🔬"),
    FEATURED("Featured", "🔥"),
    FAVORITES("Favorites", "⭐")
}

data class ProgramItem(
    val time: String,
    val title: String,
    val description: String = "",
    val isLiveNow: Boolean = false,
    val progress: Float = 0f
)

data class RamChannel(
    val id: String,
    val serialNumber: Int = 101, // Unique Channel Serial Number for TV Remote dialing
    val name: String,
    val logoText: String,
    val category: ChannelCategory,
    val streamUrl: String,
    val backupStreamUrls: List<String> = emptyList(),
    val viewersCount: String,
    val resolution: String = "1080p FHD",
    val currentShow: String,
    val currentShowCategory: String,
    val progressPercent: Float = 0.45f,
    val language: String = "Odia / Hindi / English",
    val isFeatured: Boolean = false,
    val description: String = "",
    val schedule: List<ProgramItem> = emptyList()
) {
    /**
     * All available stream URLs: primary followed by any backup URLs
     */
    val allStreams: List<String>
        get() = (listOf(streamUrl) + backupStreamUrls).distinct()
}

val DEMO_CHANNELS: List<RamChannel> by lazy {
    RamChannelCatalog.getAllChannels()
}
