package com.challenge.device_datasource

import android.content.Context
import android.media.ThumbnailUtils
import android.os.Build
import android.os.CancellationSignal
import android.provider.MediaStore
import android.util.Size
import androidx.annotation.RequiresApi
import com.challenge.device_datasource.interfaces.IDeviceDataSource
import com.challenge.model.entity.AlbumModel
import com.challenge.model.entity.GalleryModel
import com.challenge.model.entity.MediaFiles
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File
import javax.inject.Inject


class DeviceDataSource @Inject constructor(
    val mContext: Context
) : IDeviceDataSource {

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun getGallery(): GalleryModel {
        getDirectories(mContext = mContext)

        return GalleryModel(
            name = "", listOf()
        )

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun getLisOfAlbums(): Flow<List<AlbumModel>> {
        return MutableStateFlow(getDirectories(mContext = mContext))
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getDirectories(mContext: Context): List<AlbumModel> {

        val directories = ArrayList<String>()
        val albumDirectories = ArrayList<AlbumModel>()
        val allImagesDirectories = ArrayList<MediaFiles>()

        val contentResolver = mContext.contentResolver
        val queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Images.Media.DATA
        )

        val includeImages = MediaStore.Images.Media.MIME_TYPE + " LIKE 'image/%' "
        val includeVideos = MediaStore.Images.Media.MIME_TYPE + " LIKE 'video/%' "
        val excludeGif =
            " AND " + MediaStore.Images.Media.MIME_TYPE + " != 'image/gif' " + " AND " + MediaStore.Images.Media.MIME_TYPE + " != 'image/giff' "
        val selection = includeImages + excludeGif

        val cursor = contentResolver.query(queryUri, projection, includeImages, null, null)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                if (projection.isNotEmpty()) {
                    val photoUri = cursor.getString(cursor.getColumnIndex(projection[0]))
                    allImagesDirectories.add(MediaFiles(name = photoUri, uri = photoUri))
                    if (!directories.contains(File(photoUri).parent)) {
                        directories.add(File(photoUri).parent)
                        val file = File(photoUri)
                        val albumName = file.parentFile.name
                        val count = file.parentFile.listFiles().size
                        val thumbnail = ThumbnailUtils.createImageThumbnail(
                            file, Size(200, 200), CancellationSignal()
                        )

                        albumDirectories.add(
                            AlbumModel(
                                name = albumName,
                                thumbnail = thumbnail,
                                mediaFiles = mutableListOf(MediaFiles(name = photoUri, uri = photoUri))
                            )
                        )
                    } else {

                        albumDirectories.find { it.name == File(photoUri).parentFile.name }?.let {
                            it.mediaFiles.add(MediaFiles(name = photoUri, uri = photoUri))
                        }
                    }
                }
            } while (cursor.moveToNext())
        }

        if (allImagesDirectories.isNotEmpty()) {
            val thumbnail = ThumbnailUtils.createImageThumbnail(
                File(allImagesDirectories[0].uri), Size(200, 200), CancellationSignal()
            )
            albumDirectories.add(
                0, AlbumModel(
                    name = "All Images",
                    thumbnail = thumbnail,
                    mediaFiles = allImagesDirectories
                )
            )
        }

        return albumDirectories
    }


}
