package com.challenge.model.entity

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AlbumModel(
    val name: String,
    var thumbnail: Bitmap? = null,
    var mediaFiles: MutableList<MediaFiles> = mutableListOf(),
) : Parcelable