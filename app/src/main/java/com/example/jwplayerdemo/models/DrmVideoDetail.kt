package com.example.jwplayerdemo.models

import android.media.MediaDrm
import android.os.Build
import com.google.android.exoplayer2.C
import com.google.gson.annotations.SerializedName


data class DrmVideoDetail(
    @SerializedName("description")
    val description: String,
    @SerializedName("feed_instance_id")
    val feed_instance_id: String,
    @SerializedName("kind")
    val kind: String,
    @SerializedName("playlist")
    val playlist: List<Playlist>,
    @SerializedName("title")
    val title: String
)

data class Drm(
    @SerializedName("fairplay")
    val fairplay: Fairplay,
    @SerializedName("playready")
    val playready: Playready,
    @SerializedName("widevine")
    val widevine: Widevine
)

data class Fairplay(
    @SerializedName("certificateUrl")
    val certificateUrl: String,
    @SerializedName("processSpcUrl")
    val processSpcUrl: String
)

data class Image(
    @SerializedName("src")
    val src: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("width")
    val width: Int
)

data class Playlist(
    @SerializedName("description")
    val description: String,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("images")
    val images: List<Image>,
    @SerializedName("link")
    val link: String,
    @SerializedName("mediaid")
    val mediaid: String,
    @SerializedName("pubdate")
    val pubdate: Int,
    @SerializedName("sources")
    val sources: List<Source>,
    @SerializedName("title")
    val title: String,
    @SerializedName("tracks")
    val tracks: List<Track>,
    @SerializedName("variations")
    val variations: Variations
)

data class Playready(
    @SerializedName("url")
    val url: String
)

data class Source(
    @SerializedName("drm")
    val drm: Drm,
    @SerializedName("file")
    val file: String,
    @SerializedName("type")
    val type: String
)

data class Track(
    @SerializedName("file")
    val file: String,
    @SerializedName("kind")
    val kind: String
)

data class Widevine(
    @SerializedName("url")
    val url: String
)

class Variations


data class DrmInfo(
    val securityLevel: String,
    val maxHdcpLevel: String,
    val deviceUniqueId: String,
    val deviceName: String,
    val brandName: String,
    val deviceManufacture: String,
    val versionCode: String,
    val sdkVersion: Int,
) {
    companion object {
        fun getDrmInfo(): DrmInfo {
            var deviceUniqueId = ""
            var securityLevel = "NONE"
            var maxHdcpLevel = "NONE"
            try {
                val widevineDrm = MediaDrm(C.WIDEVINE_UUID)
                deviceUniqueId = try {
                    val id = widevineDrm.getPropertyByteArray(MediaDrm.PROPERTY_DEVICE_UNIQUE_ID)
                    android.util.Base64.encodeToString(id, android.util.Base64.NO_WRAP)
                } catch (e: Exception) {
                    e.printStackTrace()
                    ""
                }
                securityLevel = try {
                    widevineDrm.getPropertyString("securityLevel")
                } catch (e: Exception) {
                    e.printStackTrace()
                    "NONE"
                }
                maxHdcpLevel =
                    try {
                        widevineDrm.getPropertyString("maxHdcpLevel")
                    } catch (e: Exception) {
                        e.printStackTrace()
                        "NONE"
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val sdkVersion = Build.VERSION.SDK_INT
            val versionCode = Build.VERSION.RELEASE
            val deviceName = Build.MODEL
            val brandName = Build.BRAND
            val deviceManufacture = Build.MANUFACTURER
            return DrmInfo(
                securityLevel = securityLevel,
                maxHdcpLevel = maxHdcpLevel,
                deviceUniqueId = deviceUniqueId,
                sdkVersion = sdkVersion,
                deviceManufacture = deviceManufacture,
                deviceName = deviceName,
                brandName = brandName,
                versionCode = versionCode
            )
        }

    }

}