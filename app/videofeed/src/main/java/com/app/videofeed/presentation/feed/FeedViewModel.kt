package com.app.videofeed.presentation.feed

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.app.videofeed.domain.models.VideoData
import com.app.videofeed.presentation.feed.effect.AnimationEffect
import com.app.videofeed.presentation.feed.effect.PlayerErrorEffect
import com.app.videofeed.presentation.feed.effect.ResetAnimationEffect
import com.app.videofeed.presentation.feed.effect.VideoEffect
import com.app.videofeed.presentation.feed.state.VideoUiState
import com.app.videofeed.util.toMediaItems
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
): ViewModel() {

    private val _state = MutableStateFlow(VideoUiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<VideoEffect>()
    val effect = _effect.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            flowOf(SampleData.sampleVideos).collect {
                _state.emit(VideoUiState(
                    videos = it
                ))
            }
        }
    }

    fun createPlayer(context: Context) {
        _state.update { state->
            if (state.player == null) {
                state.copy(player = ExoPlayer.Builder(context).build().apply {
                    repeatMode = Player.REPEAT_MODE_ONE
                    setMediaItems(state.videos.toMediaItems())
                    prepare()
                })
            }
            else
                state
        }
    }

    fun releasePlayer(isChangingConfigurations: Boolean) {
        if (isChangingConfigurations)
            return
        _state.update { state->
            state.player?.release()
            state.copy(player = null)
        }
    }

    fun onPlayerError() {
        viewModelScope.launch(Dispatchers.Main) {
            state.value.player?.let { player ->
                _effect.emit(
                    PlayerErrorEffect(
                    message = player.playerError?.message.toString(),
                    code = player.playerError?.errorCode ?: -1)
                )
            }
        }
    }

    fun onTappedScreen() {
        viewModelScope.launch(Dispatchers.Main) {
            _effect.emit(ResetAnimationEffect)
            state.value.player?.let { player ->
                val drawable = if (player.isPlaying) {
                    player.pause()
                    androidx.media3.ui.R.drawable.exo_icon_pause
                }
                else {
                    player.play()
                    androidx.media3.ui.R.drawable.exo_icon_play
                }
                _effect.emit(AnimationEffect(drawable))
            }
        }
    }
}

object SampleData {
    private val video1 = VideoData(
        id = 2342435,
        mediaUri = "https://assets.mixkit.co/videos/preview/mixkit-tree-with-yellow-flowers-1173-large.mp4",
        previewImageUri = "https://mixkit.imgix.net/videos/preview/mixkit-tree-with-yellow-flowers-1173-0.jpg",
        aspectRatio = .5625f
    )

    private val video2 = VideoData(
        id = 2342342,
        mediaUri = "https://assets.mixkit.co/videos/preview/mixkit-palm-tree-in-front-of-the-sun-1191-large.mp4",
        previewImageUri = "https://mixkit.imgix.net/videos/preview/mixkit-palm-tree-in-front-of-the-sun-1191-0.jpg",
        aspectRatio = .5625f
    )

    private val video3 = VideoData(
        id = 34234,
        mediaUri = "https://assets.mixkit.co/videos/preview/mixkit-red-frog-on-a-log-1487-large.mp4",
        previewImageUri = "https://mixkit.imgix.net/videos/preview/mixkit-red-frog-on-a-log-1487-0.jpg",
        aspectRatio = .5625f
    )

    private val video4 = VideoData(id= 1, mediaUri="https://content.jwplatform.com/manifests/vM7nH0Kl.m3u8")
    private val video5 = VideoData(id= 2, mediaUri=  "https://sample.vodobox.net/skate_phantom_flex_4k/skate_phantom_flex_4k.m3u8")
    private val video6 = VideoData(id= 3, mediaUri= "https://content.jwplatform.com/manifests/vM7nH0Kl.m3u8", )
    private val video7 =  VideoData(id= 4, mediaUri=  "https://playertest.longtailvideo.com/adaptive/wowzaid3/playlist.m3u8", )
    private val video8 =  VideoData(id= 6, mediaUri= "https://diceyk6a7voy4.cloudfront.net/e78752a1-2e83-43fa-85ae-3d508be29366/hls/fitfest-sample-1_Ott_Hls_Ts_Avc_Aac_16x9_1280x720p_30Hz_6.0Mbps_qvbr.m3u8", )
    private val video9 =   VideoData(id= 1, mediaUri="https://content.jwplatform.com/manifests/vM7nH0Kl.m3u8",)
    private val video10 = VideoData(id= 2, mediaUri=  "https://sample.vodobox.net/skate_phantom_flex_4k/skate_phantom_flex_4k.m3u8", )
    private val video11 =VideoData(id= 3, mediaUri= "https://content.jwplatform.com/manifests/vM7nH0Kl.m3u8", )
    private val video12 =  VideoData(id= 4, mediaUri=  "https://playertest.longtailvideo.com/adaptive/wowzaid3/playlist.m3u8", )
    private val video13 = VideoData(id= 6, mediaUri= "https://diceyk6a7voy4.cloudfront.net/e78752a1-2e83-43fa-85ae-3d508be29366/hls/fitfest-sample-1_Ott_Hls_Ts_Avc_Aac_16x9_1280x720p_30Hz_6.0Mbps_qvbr.m3u8", )
    private val video14 =  VideoData(id= 11, mediaUri="https://content.jwplatform.com/manifests/vM7nH0Kl.m3u8")
    private val video15 =  VideoData(id= 12, mediaUri=  "https://sample.vodobox.net/skate_phantom_flex_4k/skate_phantom_flex_4k.m3u8", )
    private val video16 =  VideoData(id= 31, mediaUri= "https://content.jwplatform.com/manifests/vM7nH0Kl.m3u8", )
    private val video17 =  VideoData(id= 41, mediaUri=  "https://playertest.longtailvideo.com/adaptive/wowzaid3/playlist.m3u8", )
    private val video18 =  VideoData(id= 61, mediaUri= "https://diceyk6a7voy4.cloudfront.net/e78752a1-2e83-43fa-85ae-3d508be29366/hls/fitfest-sample-1_Ott_Hls_Ts_Avc_Aac_16x9_1280x720p_30Hz_6.0Mbps_qvbr.m3u8", )
    private val video19 =  VideoData(id= 111, mediaUri="https://content.jwplatform.com/manifests/vM7nH0Kl.m3u8")
    private val video20 =  VideoData(id= 2111, mediaUri=  "https://sample.vodobox.net/skate_phantom_flex_4k/skate_phantom_flex_4k.m3u8", )
    private val video21 =  VideoData(id= 3111, mediaUri= "https://content.jwplatform.com/manifests/vM7nH0Kl.m3u8")
    private val video22 =   VideoData(id= 4111, mediaUri=  "https://playertest.longtailvideo.com/adaptive/wowzaid3/playlist.m3u8", )
    private val video23 =  VideoData(id= 61111, mediaUri= "https://diceyk6a7voy4.cloudfront.net/e78752a1-2e83-43fa-85ae-3d508be29366/hls/fitfest-sample-1_Ott_Hls_Ts_Avc_Aac_16x9_1280x720p_30Hz_6.0Mbps_qvbr.m3u8", )
    private val video24 =  VideoData(id= 122, mediaUri="https://content.jwplatform.com/manifests/vM7nH0Kl.m3u8")
    private val video25 =  VideoData(id= 222, mediaUri=  "https://sample.vodobox.net/skate_phantom_flex_4k/skate_phantom_flex_4k.m3u8", )
    private val video26 =  VideoData(id= 322, mediaUri= "https://content.jwplatform.com/manifests/vM7nH0Kl.m3u8", )
    private val video27 =   VideoData(id= 422, mediaUri=  "https://playertest.longtailvideo.com/adaptive/wowzaid3/playlist.m3u8", )
    private val video28 =  VideoData(id= 622, mediaUri= "https://diceyk6a7voy4.cloudfront.net/e78752a1-2e83-43fa-85ae-3d508be29366/hls/fitfest-sample-1_Ott_Hls_Ts_Avc_Aac_16x9_1280x720p_30Hz_6.0Mbps_qvbr.m3u8", )

    val sampleVideos = listOf(
        video1,
        video2,
        video3,
        video4,
        video5,
        video6,
        video7,
        video8,
        video9,
        video10,
        video11,
        video12,
        video13,
        video14,
        video15,
        video16,
        video17,
        video18,
        video19,
        video20,
        video21,
        video22,
        video23,
        video24,
        video25,
        video26,
        video27,
        video28,
    )
}