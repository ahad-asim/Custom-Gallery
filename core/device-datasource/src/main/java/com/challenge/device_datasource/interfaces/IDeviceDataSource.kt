package com.challenge.device_datasource.interfaces

import com.challenge.model.entity.AlbumModel
import com.challenge.model.entity.GalleryModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface IDeviceDataSource {

    fun getGallery(): GalleryModel

    fun getLisOfAlbums(): MutableStateFlow<List<AlbumModel>>

}