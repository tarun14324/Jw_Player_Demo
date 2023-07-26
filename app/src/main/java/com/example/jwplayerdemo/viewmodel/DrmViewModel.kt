package com.example.jwplayerdemo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jwplayerdemo.api.RetrofitHelper
import com.example.jwplayerdemo.models.DrmInfo
import com.example.jwplayerdemo.models.DrmVideoDetail
import com.example.jwplayerdemo.models.VideoContent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch


class DrmViewModel : ViewModel() {

    lateinit var drmData: VideoContent

    val videoMetaDataEvent = Channel<DrmVideoDetail?>(Channel.CONFLATED).apply {
        trySend(null)
    }

    fun getData(){
        viewModelScope.launch {
            fetchVideoMetadata()
        }
    }

     private suspend fun fetchVideoMetadata() {
        if (drmData.isDrm) {
            val drmInfo = DrmInfo.getDrmInfo()
            val drmVideoResponse =RetrofitHelper.getInstance().fetchDrmVideoData(
                drmData.jwMediaId!!, deviceOs = "Android",
                platform = "mobile", securityLevel = drmInfo.securityLevel, digitalOutput = drmInfo.maxHdcpLevel
            )
            videoMetaDataEvent.trySend(drmVideoResponse.body()?.data!!)
        } else {
            videoMetaDataEvent.trySend(null)
        }
    }
}