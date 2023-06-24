package com.challenge.model.entity

import android.graphics.Bitmap


data class AlbumModel(
    val name: String,
    var thumbnail: Bitmap? = null,
    var images: MutableList<ImageModel> = mutableListOf(),
    var videos: MutableList<VideoModel> = mutableListOf(),
)
