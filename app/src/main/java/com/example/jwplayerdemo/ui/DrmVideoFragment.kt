package com.example.jwplayerdemo.ui

import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.jwplayerdemo.databinding.FragmentDrmVideoBinding
import com.example.jwplayerdemo.models.DrmVideoDetail
import com.example.jwplayerdemo.utils.Util
import com.example.jwplayerdemo.viewmodel.DrmViewModel
import com.google.android.exoplayer2.drm.ExoMediaDrm
import com.jwplayer.pub.api.JWPlayer
import com.jwplayer.pub.api.configuration.PlayerConfig
import com.jwplayer.pub.api.configuration.UiConfig
import com.jwplayer.pub.api.events.ErrorEvent
import com.jwplayer.pub.api.events.EventType
import com.jwplayer.pub.api.events.FirstFrameEvent
import com.jwplayer.pub.api.events.listeners.VideoPlayerEvents
import com.jwplayer.pub.api.license.LicenseUtil
import com.jwplayer.pub.api.media.drm.MediaDrmCallback
import com.jwplayer.pub.api.media.playlists.PlaylistItem
import com.jwplayer.pub.view.JWPlayerView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.UUID


class DrmVideoFragment : Fragment() {

    private var _binding: FragmentDrmVideoBinding? = null
    private lateinit var mPlayerView: JWPlayerView
    private var mPlayer: JWPlayer? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DrmViewModel
    private var videoPlayer: JWPlayer? = null

    private var isVideoStarted = false
    private var isPlayingVideoOnError = false


    private val args: DrmVideoFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDrmVideoBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[DrmViewModel::class.java]
        viewModel.drmData= args.item
        activity?.actionBar?.title = args.item.contentName
        viewModel.getData()
        mPlayerView = binding.jwplayer
        mPlayer = mPlayerView.getPlayer(this)
        // adding licenceKey
        LicenseUtil().setLicenseKey(requireContext(), "Ai0ZRuaM2EmDzgIy8QLZv08BxzZzXjw2Zg3W4XCnX9fnU33W")
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.videoMetaDataEvent.receiveAsFlow().collectLatest {
                initVideoSetup(it)
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (mPlayer?.fullscreen == true) {
                mPlayer?.setFullscreen(false, true)
            }
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (!mPlayer?.fullscreen!!) {
                mPlayer?.setFullscreen(true, true)
            }
        }
    }

    private fun initVideoSetup(data: DrmVideoDetail?) {
        val videoUrl = if (args.item.isDrm) {
            data?.let {
                data.playlist.firstOrNull()?.sources?.firstOrNull()?.file
                    ?: return
            }
        } else {
            args.item.contentUrl
        }
        val playlistItemBuilder = PlaylistItem.Builder()
            .title(args.item.chapterName)
            .file(videoUrl)

        // Added Drm call back only if wideVineUrl is not null
        if (args.item.isDrm) {
            val wideVineUrl =
                data?.playlist?.firstOrNull()?.sources?.firstOrNull()?.drm?.widevine?.url ?: return
            playlistItemBuilder.mediaDrmCallback(object : MediaDrmCallback {

                override fun describeContents() = 0

                override fun writeToParcel(dest: Parcel, flags: Int) {
                    dest.writeString(wideVineUrl)
                }

                override fun executeProvisionRequest(
                    uuid: UUID?, request: ExoMediaDrm.ProvisionRequest?
                ): ByteArray {
                    val url = request?.defaultUrl + "&signedRequest=" + String(
                        request?.data ?: byteArrayOf()
                    )
                 //   Log.e(TAG, "executeProvisionRequest: ", )("URL :" + "\t").e("URL executeProvisionRequest %s", url)
                    return Util.executePost(url, null, null)
                }

                override fun executeKeyRequest(
                    uuid: UUID?, request: ExoMediaDrm.KeyRequest?
                ): ByteArray {
                    var url = request?.licenseServerUrl
                 //   Timber.tag("URL :" + "\t").e("URL executeKeyRequest %s", url)
                    if (url.isNullOrBlank()) url = wideVineUrl
                    return Util.executePost(url, request?.data, null)
                }

            })
        }

        val playlistItem = playlistItemBuilder.build()

        // we can set multiple videos for a playlist here.
        val playList = arrayListOf<PlaylistItem>(playlistItem)

        val uiConfig = UiConfig.Builder().displayAllControls()
            .build()  // TODO modify display controller based on app

        val playerConfig = PlayerConfig.Builder()
            .playlist(playList)
            .uiConfig(uiConfig) // to display controls
            .build()
        videoPlayer = binding.jwplayer.getPlayer(this).apply {
            setup(playerConfig)
            addListener(EventType.FIRST_FRAME, object : VideoPlayerEvents.OnFirstFrameListener {
                override fun onFirstFrame(p0: FirstFrameEvent?) {
                    if (!isVideoStarted) {
                        // Seeking the video progress on video first frame appearance
                        args.item.videoPosition?.let { videoPlayer?.setCurrentPositionAsMillis(it) }
                        isVideoStarted = true
                    }
                    isPlayingVideoOnError = false
                }
            })
            addListener(EventType.ERROR, object : VideoPlayerEvents.OnErrorListener {
                override fun onError(errorEvent: ErrorEvent?) {
                //    viewModel.sendVideoError(errorEvent!!)

                    // Reloading the video on replay video error status
                    val replayErrorCode = 271403
                    if (errorEvent != null) {
                        if (errorEvent.errorCode == replayErrorCode && !isPlayingVideoOnError) {
                            isPlayingVideoOnError = true
                            // Reload Video
                          //  reloadVideo()
                        }
                    }
                }
            })
        }
        videoPlayer?.setFullscreen(true, true)
        videoPlayer?.play()
    }

    private fun isCompleted(): Boolean {
        if (videoPlayer == null) return false

        return videoPlayer!!.position >= videoPlayer!!.duration
    }

    private fun JWPlayer.getCurrentPositionInMillis(): Long {
        return this.position.toLong() * 1000
    }

    private fun JWPlayer.getDurationInMillis(): Long {
        return this.duration.toLong() * 1000
    }

    private fun JWPlayer.setCurrentPositionAsMillis(timeInMillis: Long) {
        val seekTime = (timeInMillis / 1000).toDouble()
        this.seek(seekTime)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}