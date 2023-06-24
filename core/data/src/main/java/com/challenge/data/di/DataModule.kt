package com.challenge.data.di

import com.challenge.data.repository.AlbumRepository
import com.challenge.data.repository.interfaces.IAlbumRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsAlbumRepository(
        albumRepository: AlbumRepository
    ): IAlbumRepository

}