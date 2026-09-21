package com.example.model

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * UpdateInfo represents the application update metadata,
 * including version codes, APK download links, changelog entries,
 * and remote update verification.
 */
data class UpdateInfo(
    val versionCode: Int,
    val versionName: String,
    val apkUrl: String,
    val title: String = "RaM Tv Update Available",
    val description: String = "",
    val releaseDate: String = "",
    val fileSizeMb: String = "26.4 MB",
    val isForceUpdate: Boolean = false,
    val minSupportedVersionCode: Int = 1,
    val changelog: List<String> = emptyList(),
    val directDownloadUrl: String = apkUrl
) {
    /**
     * Returns true if this remote update is newer than the currently installed app version.
     */
    fun isNewerThan(installedVersionCode: Int = BuildConfig.APP_VERSION_CODE): Boolean {
        return versionCode > installedVersionCode
    }

    /**
     * Serializes this UpdateInfo object to a formatted JSON string.
     */
    fun toJson(): String {
        val json = JSONObject()
        json.put("versionCode", versionCode)
        json.put("versionName", versionName)
        json.put("apkUrl", apkUrl)
        json.put("title", title)
        json.put("description", description)
        json.put("releaseDate", releaseDate)
        json.put("fileSizeMb", fileSizeMb)
        json.put("isForceUpdate", isForceUpdate)
        json.put("minSupportedVersionCode", minSupportedVersionCode)
        val arr = JSONArray()
        changelog.forEach { arr.put(it) }
        json.put("changelog", arr)
        return json.toString(2)
    }

    companion object {
        /**
         * Default metadata for the currently installed build.
         */
        val CURRENT: UpdateInfo = UpdateInfo(
            versionCode = BuildConfig.APP_VERSION_CODE,
            versionName = BuildConfig.APP_VERSION_NAME,
            apkUrl = "",
            title = "RaM Tv v${BuildConfig.APP_VERSION_NAME}",
            description = "Latest stable release of RaM TV Live Streaming",
            releaseDate = "21 Sep 2026",
            fileSizeMb = "26.4 MB",
            isForceUpdate = false,
            changelog = listOf(
                "Direct TV Code Remote Quick Dial (101-140) with instant playback",
                "Full Odia Regional Pack (DD Odia, Kanak, Kalinga, Argus, Tarang)",
                "All India National FTA channels (DD Sports, DD National, Aaj Tak, ABP, Republic)",
                "Direct APK In-App Update support with remote URL download",
                "Auto old APK cleaner & low-latency player reconnect"
            )
        )

        /**
         * Parses a JSON string into an UpdateInfo instance.
         */
        fun fromJson(jsonStr: String): UpdateInfo? {
            return try {
                val json = JSONObject(jsonStr)
                val vCode = json.optInt("versionCode", BuildConfig.APP_VERSION_CODE)
                val vName = json.optString("versionName", "v${BuildConfig.APP_VERSION_NAME}")
                val apkUrl = json.optString("apkUrl", json.optString("downloadUrl", ""))
                val title = json.optString("title", "RaM Tv Update Available")
                val desc = json.optString("description", "")
                val date = json.optString("releaseDate", "")
                val size = json.optString("fileSizeMb", "26.4 MB")
                val isForce = json.optBoolean("isForceUpdate", false)
                val minVer = json.optInt("minSupportedVersionCode", 1)

                val logList = mutableListOf<String>()
                val arr = json.optJSONArray("changelog")
                if (arr != null) {
                    for (i in 0 until arr.length()) {
                        logList.add(arr.getString(i))
                    }
                }

                UpdateInfo(
                    versionCode = vCode,
                    versionName = vName,
                    apkUrl = apkUrl,
                    title = title,
                    description = desc,
                    releaseDate = date,
                    fileSizeMb = size,
                    isForceUpdate = isForce,
                    minSupportedVersionCode = minVer,
                    changelog = logList
                )
            } catch (e: Exception) {
                null
            }
        }

        /**
         * Asynchronously fetches update information from any remote HTTP/HTTPS URL
         * (e.g. GitHub raw, Firebase, or custom web host).
         */
        suspend fun fetchFromRemoteUrl(urlStr: String): Result<UpdateInfo> = withContext(Dispatchers.IO) {
            try {
                val url = URL(urlStr)
                val conn = url.openConnection() as HttpURLConnection
                conn.connectTimeout = 10000
                conn.readTimeout = 15000
                conn.instanceFollowRedirects = true
                conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Android; RaMTv-UpdateChecker)")
                conn.connect()

                if (conn.responseCode in 200..299) {
                    val reader = BufferedReader(InputStreamReader(conn.inputStream))
                    val content = reader.readText()
                    reader.close()
                    val info = fromJson(content)
                    if (info != null) {
                        Result.success(info)
                    } else {
                        Result.failure(IllegalArgumentException("Invalid update_info JSON format"))
                    }
                } else {
                    Result.failure(java.io.IOException("HTTP response code: ${conn.responseCode}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

        /**
         * Generates a sample JSON template for developers to host on GitHub or cloud servers.
         */
        fun getSampleJsonTemplate(apkDownloadUrl: String = "https://your-server.com/RaMTv_v2.1.apk"): String {
            return """
            {
              "versionCode": ${BuildConfig.APP_VERSION_CODE},
              "versionName": "${BuildConfig.APP_VERSION_NAME}",
              "apkUrl": "$apkDownloadUrl",
              "title": "RaM Tv Update Available",
              "description": "New features and channel streams update",
              "releaseDate": "21 Sep 2026",
              "fileSizeMb": "26.4 MB",
              "isForceUpdate": false,
              "changelog": [
                "Direct TV Code Remote quick dial",
                "New Odia & FTA live channels added",
                "Performance improvements and bug fixes"
              ]
            }
            """.trimIndent()
        }
    }
}

/**
 * Typealias supporting lowercase naming conventions (Updateinfo).
 */
typealias Updateinfo = UpdateInfo
