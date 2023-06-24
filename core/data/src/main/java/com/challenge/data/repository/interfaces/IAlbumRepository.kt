package com.challenge.data.repository.interfaces

import com.challenge.model.entity.AlbumModel
import com.challenge.model.entity.GalleryModel
import kotlinx.coroutines.flow.Flow


interface IAlbumRepository {

    fun getGallery(): GalleryModel

    fun getListOfAlbums(): Flow<List<AlbumModel>>

}