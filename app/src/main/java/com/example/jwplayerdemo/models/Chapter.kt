package com.example.jwplayerdemo.models

import com.google.gson.annotations.SerializedName


data class ChapterDetail(
    @SerializedName("total_chapters")
    val totalChapters: Int?,

    @SerializedName("practiced_chapters")
    val practicedChapters: Int?,

    @SerializedName("chapter_list")
    val chapterList: List<Chapter>?,
)

data class Chapter(

    @SerializedName("subject_id")
    val subjectId: String?,

    @SerializedName("subject_name")
    val subjectName: String?,

    @SerializedName("subject_image_url")
    val subjectImageUrl: String?,

    @SerializedName("chapter_id")
    val chapterId: String?,

    @SerializedName("chapter_name")
    val chapterName: String?,

    @SerializedName("chapter_white_icon_url")
    val chapterImageUrl: String?,

    @SerializedName("chapter_color_icon_url")
    val chapterColorImageUrl: String?,

    @SerializedName("videos_count")
    val videosCount: Long = 0,

    @SerializedName("qa_count")
    val qaCount: Long = 0,

    @SerializedName("sub_topic_count")
    val subTopicCount: Long = 0,

    @SerializedName("resource_count")
    val resourceCount: Long = 0,

    @SerializedName("is_locked")
    val isLocked: Boolean = false,

    @SerializedName("video_watched_count")
    val videoWatchedCount: Long = 0,

    @SerializedName("completed_percentage")
    val completedPercentage: Float = 0f,

    @SerializedName("read_in")
    val readIn: String,
)