package com.challenge.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VideoModel(val name: String, val url: String) : Parcelable
