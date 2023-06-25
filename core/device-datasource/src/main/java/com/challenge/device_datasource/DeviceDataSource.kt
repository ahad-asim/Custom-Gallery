package com.challenge.device_datasource

import android.content.Context
import android.database.Cursor
import android.media.ThumbnailUtils
import android.os.Build
import android.os.CancellationSignal
import android.os.Environment
import android.provider.MediaStore
import android.util.Size
import androidx.annotation.RequiresApi
import com.challenge.device_datasource.interfaces.IDeviceDataSource
import com.challenge.model.entity.AlbumModel
import com.challenge.model.entity.GalleryModel
import com.challenge.model.entity.MediaFileType
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

        val cursor = contentResolver.query(queryUri, projection, null, null, null)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val photoUri = cursor.getString(cursor.getColumnIndex(projection[0]))
                allImagesDirectories.add(MediaFiles(type = MediaFileType.Image, uri = photoUri))
                if (!directories.contains(File(photoUri).parent)) {

                    directories.add(File(photoUri).parent)


                    val file = File(photoUri)
                    var albumName = file.parentFile.name
                    val count = file.parentFile.listFiles().size
                    val thumbnail = ThumbnailUtils.createImageThumbnail(
                        file, Size(200, 200), CancellationSignal()
                    )

                    albumDirectories.add(
                        AlbumModel(
                            name = albumName,
                            thumbnail = thumbnail,
                            mediaFiles = mutableListOf(
                                MediaFiles(
                                    type = MediaFileType.Image,
                                    uri = photoUri
                                )
                            )
                        )
                    )
                } else {

                    albumDirectories.find { it.name == File(photoUri).parentFile.name }?.let {
                        it.mediaFiles.add(MediaFiles(type = MediaFileType.Image, uri = photoUri))
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

        getVideoList(mContext, directories, albumDirectories).let {
            if (it.isNotEmpty()) {

                val thumbnail = ThumbnailUtils.createVideoThumbnail(
                    File(it[0].uri).absolutePath,
                    MediaStore.Video.Thumbnails.MICRO_KIND
                )

                albumDirectories.add(
                    1, AlbumModel(
                        name = "All Videos",
                        thumbnail = thumbnail,
                        mediaFiles = it
                    )
                )
            }
        }

        return albumDirectories
    }

    private fun getVideoList(
        mContext: Context,
        directories: ArrayList<String>,
        albums: ArrayList<AlbumModel>
    ): ArrayList<MediaFiles> {

        val contentResolver = mContext.contentResolver

        val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection =
            arrayOf(MediaStore.Video.VideoColumns.DATA, MediaStore.MediaColumns.DATE_MODIFIED)
        val cursor: Cursor? = contentResolver.query(uri, projection, null, null, null)
        val videoArrList: ArrayList<MediaFiles> = ArrayList()
        if (cursor != null) {
            while (cursor.moveToNext()) {

                val absolutePathOfImage = cursor.getString(0)
                videoArrList.add(MediaFiles(MediaFileType.Video, absolutePathOfImage))

                if (!directories.contains(File(absolutePathOfImage).parent)) {

                    directories.add(File(absolutePathOfImage).parent)
                    val file = File(absolutePathOfImage)
                    val albumName = file.parentFile.name

                    val thumbnail = ThumbnailUtils.createVideoThumbnail(
                        File(absolutePathOfImage).absolutePath,
                        MediaStore.Video.Thumbnails.MICRO_KIND
                    )

                    albums.add(
                        AlbumModel(
                            name = albumName,
                            thumbnail = thumbnail,
                            mediaFiles = mutableListOf(
                                MediaFiles(
                                    type = MediaFileType.Video,
                                    uri = absolutePathOfImage
                                )
                            )
                        )
                    )
                } else {

                    albums.find { it.name == File(absolutePathOfImage).parentFile.name }?.let {
                        it.mediaFiles.add(
                            MediaFiles(
                                type = MediaFileType.Video,
                                uri = absolutePathOfImage
                            )
                        )
                    }

                }

            }
            cursor.close()
        }
        return videoArrList
    }


}
