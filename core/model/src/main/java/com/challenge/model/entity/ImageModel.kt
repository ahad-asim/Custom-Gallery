package com.challenge.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageModel(
    val name: String,
    val uri: String
) : Parcelable
