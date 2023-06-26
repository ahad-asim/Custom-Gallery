package com.challenge.model.entity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MediaFiles(
    val type: MediaFileType,
    val uri: String,
    var thumbnail: Bitmap? = null,
) : Parcelable


@Parcelize
sealed interface MediaFileType : Parcelable {
    @SuppressLint("ParcelCreator")
    object Image : MediaFileType

    @SuppressLint("ParcelCreator")
    object Video : MediaFileType

}
