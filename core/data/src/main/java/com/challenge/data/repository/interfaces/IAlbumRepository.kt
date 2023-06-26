package com.challenge.data.repository.interfaces

import com.challenge.model.entity.AlbumModel
import com.challenge.model.entity.GalleryModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow


interface IAlbumRepository {

    fun getGallery(): GalleryModel

    fun getListOfAlbums(): MutableStateFlow<List<AlbumModel>>

}