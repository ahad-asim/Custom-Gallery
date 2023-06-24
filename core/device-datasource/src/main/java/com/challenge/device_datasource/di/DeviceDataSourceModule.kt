package com.challenge.device_datasource.di

import android.content.Context
import com.challenge.device_datasource.DeviceDataSource
import com.challenge.device_datasource.interfaces.IDeviceDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DeviceDataSourceModule {

//    @Binds
//    fun bindsDeviceDataSource(
//        deviceDataSource: DeviceDataSource
//    ): IDeviceDataSource

    @Provides
    fun provideDeviceDataSource(
        @ApplicationContext context: Context,
    ): IDeviceDataSource = DeviceDataSource(context)

}