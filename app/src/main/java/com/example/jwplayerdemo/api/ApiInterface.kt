package com.example.jwplayerdemo.api

import com.example.jwplayerdemo.models.BaseResponse
import com.example.jwplayerdemo.models.ChapterDetail
import com.example.jwplayerdemo.models.ChapterDetailVideo
import com.example.jwplayerdemo.models.DrmVideoDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("learn/v1/usage/get-drm-video-data")
    suspend fun getVideoList(): Response<DrmVideoDetail>

    @GET("learn/v1/usage/chapter-list")
    suspend fun fetchTopicsList(
        @Query("subject_id") subjectId: String,
    ): Response<BaseResponse<ChapterDetail>>

    @GET("https://mocki.io/v1/7aad5459-a022-4f32-9b9a-31994a4367b8")
    suspend fun fetchChapterDetail(
    ): Response<BaseResponse<List<ChapterDetailVideo>>>


    @GET("learn/v1/usage/get-drm-video-data")
    suspend fun fetchDrmVideoData(
        @Query("jw_media_id") mediaId: String,
        @Query("device_os") deviceOs: String,
        @Query("platform") platform: String,
        @Query("security_level") securityLevel: String,
        @Query("digital_output") digitalOutput: String,
    ): Response<BaseResponse<DrmVideoDetail>>
}
