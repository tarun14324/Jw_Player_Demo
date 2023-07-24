package com.example.jwplayerdemo.models

import com.google.gson.annotations.SerializedName

class BaseResponse<T>(
    @SerializedName("status")
    val status: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val data: T?,
    @SerializedName("code")
    val code: String?,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("current_page")
    val currentPage: Int,
    @SerializedName("errorCode")
    var errorCode: String? = null
)