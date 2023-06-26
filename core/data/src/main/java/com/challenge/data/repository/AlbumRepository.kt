package com.challenge.data.repository

import com.challenge.data.repository.interfaces.IAlbumRepository
import com.challenge.device_datasource.interfaces.IDeviceDataSource
import com.challenge.model.entity.AlbumModel
import com.challenge.model.entity.GalleryModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class AlbumRepository @Inject constructor(
    val deviceDataSource: IDeviceDataSource,
) : IAlbumRepository {

    override fun getGallery(): GalleryModel = deviceDataSource.getGallery()

    override fun getListOfAlbums(): MutableStateFlow<List<AlbumModel>> = deviceDataSource.getLisOfAlbums()

}