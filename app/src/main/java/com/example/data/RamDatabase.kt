package com.example.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "ram_favorites")
data class RamFavoriteEntity(
    @PrimaryKey val channelId: String,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "ram_settings")
data class RamSettingEntity(
    @PrimaryKey val key: String,
    val value: String
)

@Entity(tableName = "ram_custom_m3u")
data class RamCustomM3uPlaylist(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val playlistUrl: String,
    val channelCount: Int = 0,
    val addedAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
)

@Dao
interface RamTvDao {
    @Query("SELECT * FROM ram_favorites")
    fun getAllFavorites(): Flow<List<RamFavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(fav: RamFavoriteEntity)

    @Query("DELETE FROM ram_favorites WHERE channelId = :channelId")
    suspend fun removeFavorite(channelId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM ram_favorites WHERE channelId = :channelId)")
    suspend fun isFavorite(channelId: String): Boolean

    // Settings
    @Query("SELECT value FROM ram_settings WHERE `key` = :key")
    suspend fun getSetting(key: String): String?

    @Query("SELECT * FROM ram_settings")
    fun getAllSettings(): Flow<List<RamSettingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSetting(setting: RamSettingEntity)

    // M3U Playlists
    @Query("SELECT * FROM ram_custom_m3u ORDER BY addedAt DESC")
    fun getAllM3uPlaylists(): Flow<List<RamCustomM3uPlaylist>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertM3uPlaylist(playlist: RamCustomM3uPlaylist): Long

    @Query("DELETE FROM ram_custom_m3u WHERE id = :id")
    suspend fun deleteM3uPlaylist(id: Long)
}

@Database(
    entities = [RamFavoriteEntity::class, RamSettingEntity::class, RamCustomM3uPlaylist::class],
    version = 2,
    exportSchema = false
)
abstract class RamDatabase : RoomDatabase() {
    abstract fun ramTvDao(): RamTvDao

    companion object {
        @Volatile
        private var INSTANCE: RamDatabase? = null

        fun getInstance(context: Context): RamDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RamDatabase::class.java,
                    "ram_tv_database_v2"
                ).fallbackToDestructiveMigration(dropAllTables = true).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
