package com.challenge.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MediaFiles(
    val name: String,
    val uri: String
) : Parcelable
