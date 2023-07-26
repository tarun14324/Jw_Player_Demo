package com.example.jwplayerdemo.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ChapterDetailVideo(
    @SerializedName("subject_id")
    val subjectId: String?,

    @SerializedName("chapter_id")
    val chapterId: String,

    @SerializedName("topic_id")
    val topicId: String,

    @SerializedName("subject_name")
    val subjectName: String,

    @SerializedName("chapter_name")
    val chapterName: String,

    @SerializedName("topic_name")
    val topicName: String = "Default Topic Name",

    @SerializedName("topic_image_url")
    val imageUrl: String?,

    @SerializedName("is_locked")
    val isLocked: Boolean,

    @SerializedName("content_list")
    var videoList: List<VideoContent>?,
)

@Parcelize
data class VideoContent(


    @SerializedName("id")
    var id: Long = 0,

    @SerializedName("subject_id")
    @Expose
    var subjectId: String? ="",

    @SerializedName("chapter_id")
    @Expose
    var chapterId: String? = "",

    @SerializedName("topic_id")
    @Expose
    var topicId: String? = "",

    @SerializedName("subject_name")
    var subjectName: String?,

    @SerializedName("chapter_name")
    var chapterName: String?,

    @SerializedName("topic_name")
    var topicName: String? = "Default Topic Name",

    @SerializedName("content_id")
    @Expose
    var videoId: String,

    @SerializedName("content_name")
    @Expose
    var contentName: String = "Default Content",

    @SerializedName("content_type")
    @Expose
    var contentType: String?,

    @SerializedName("content_url")
    @Expose
    var contentUrl: String?,

    @SerializedName("thumbnail_url")
    @Expose
    var thumbNailUrl: String?,

    @SerializedName("duration")
    @Expose
    var duration: Long? = 0,

    @SerializedName("source")
    @Expose
    var source: String? = "",

    @SerializedName("video_position")
    @Expose
    var videoPosition: Long? = 0,

    @SerializedName("video_source")
    var videoSource: String?,

    @SerializedName("spent_time")
    @Expose
    var spendTime: Long? = 0,

    @SerializedName("is_liked")
    var isLiked: Int = 0,

    @SerializedName("is_locked")
    var isLocked: Boolean = false,

    @SerializedName("is_drm")
    var isDrm: Boolean,

    @SerializedName("jw_media_id", alternate = ["content_media_id"])
    var jwMediaId: String?

):Parcelable