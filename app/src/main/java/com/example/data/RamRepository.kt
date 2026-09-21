package com.example.data

import com.example.model.DEMO_CHANNELS
import com.example.model.RamChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class RamRepository(private val database: RamDatabase) {
    private val dao = database.ramTvDao()

    val favoritesFlow: Flow<Set<String>> = dao.getAllFavorites()
        .map { list -> list.map { it.channelId }.toSet() }
        .catch { emit(emptySet()) }

    val m3uPlaylistsFlow: Flow<List<RamCustomM3uPlaylist>> = dao.getAllM3uPlaylists()
        .catch { emit(emptyList()) }

    val settingsFlow: Flow<Map<String, String>> = dao.getAllSettings()
        .map { list -> list.associate { it.key to it.value } }
        .catch { emit(emptyMap()) }

    fun getChannels(): List<RamChannel> {
        return DEMO_CHANNELS
    }

    suspend fun toggleFavorite(channelId: String, isCurrentFavorite: Boolean) {
        if (isCurrentFavorite) {
            dao.removeFavorite(channelId)
        } else {
            dao.addFavorite(RamFavoriteEntity(channelId = channelId))
        }
    }

    suspend fun saveSetting(key: String, value: String) {
        dao.saveSetting(RamSettingEntity(key = key, value = value))
    }

    suspend fun addM3uPlaylist(name: String, url: String, channelCount: Int = 12): Long {
        return dao.insertM3uPlaylist(
            RamCustomM3uPlaylist(
                name = name,
                playlistUrl = url,
                channelCount = channelCount
            )
        )
    }

    suspend fun deleteM3uPlaylist(id: Long) {
        dao.deleteM3uPlaylist(id)
    }
}
