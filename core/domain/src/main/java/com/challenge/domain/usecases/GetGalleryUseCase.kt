package com.challenge.domain.usecases

import com.challenge.data.repository.interfaces.IAlbumRepository
import javax.inject.Inject

class GetGalleryUseCase @Inject constructor(private val albumRepository: IAlbumRepository) {

    operator fun invoke() = albumRepository.getListOfAlbums()

}