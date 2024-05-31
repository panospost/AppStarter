package com.app.videofeed.domain.models

data class VideoData(
    val id: Int,
    val mediaUri: String,
    val previewImageUri: String = "",
    val aspectRatio: Float? =  .5625f,
)