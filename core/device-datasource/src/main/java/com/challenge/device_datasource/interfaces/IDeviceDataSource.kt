package com.challenge.device_datasource.interfaces

import com.challenge.model.entity.AlbumModel
import com.challenge.model.entity.GalleryModel
import kotlinx.coroutines.flow.Flow

interface IDeviceDataSource {

    fun getGallery(): GalleryModel

    fun getLisOfAlbums(): Flow<List<AlbumModel>>

}