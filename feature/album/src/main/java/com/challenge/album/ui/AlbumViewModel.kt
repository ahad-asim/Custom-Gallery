package com.challenge.album.ui

import android.media.ThumbnailUtils
import android.os.Build
import android.os.CancellationSignal
import android.provider.MediaStore
import android.provider.Settings.Global
import android.util.Size
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.challenge.album.navigation.albumArg
import com.challenge.model.entity.AlbumModel
import com.challenge.model.entity.MediaFileType
import com.challenge.model.entity.MediaFiles
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor() : ViewModel() {

    var albumUiState: MutableStateFlow<AlbumUiState> = MutableStateFlow(AlbumUiState.Loading)

    var album: AlbumModel? = null

    @RequiresApi(Build.VERSION_CODES.Q)
    suspend fun updateAlbumState(navController: NavController) {
        album = navController.previousBackStackEntry?.savedStateHandle?.get<AlbumModel>(
            albumArg
        )
        viewModelScope.launch {
            album?.let {
                albumUiState.emit(AlbumUiState.Success(it))
            }
        }

        GlobalScope.launch {
            async {
                album?.let {
                    updateAlbumThumbnails(it.mediaFiles)
                }
            }
        }

    }


    @RequiresApi(Build.VERSION_CODES.Q)
    suspend fun updateAlbumThumbnails(mediaFiles: MutableList<MediaFiles>) {
        for (mediaFile in mediaFiles) {
            mediaFile.thumbnail = when (mediaFile.type) {
                is MediaFileType.Image -> ThumbnailUtils.createImageThumbnail(
                    File(mediaFile.uri), Size(200, 200), CancellationSignal()
                )

                is MediaFileType.Video -> ThumbnailUtils.createVideoThumbnail(
                    File(mediaFile.uri).absolutePath, MediaStore.Video.Thumbnails.MINI_KIND
                )
            }

            album?.let {
                it.mediaFiles = mediaFiles
                albumUiState.emit(AlbumUiState.Success(it))
            }

        }


    }


}


sealed interface AlbumUiState {

    object Loading : AlbumUiState
    data class Error(val msg: String) : AlbumUiState

    data class Success(val albumModel: AlbumModel) : AlbumUiState

}