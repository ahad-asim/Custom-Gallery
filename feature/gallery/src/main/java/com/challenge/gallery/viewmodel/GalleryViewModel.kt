package com.challenge.gallery.viewmodel

import android.media.ThumbnailUtils
import android.os.Build
import android.os.CancellationSignal
import android.provider.MediaStore
import android.util.Size
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.domain.usecases.GetGalleryUseCase
import com.challenge.model.entity.AlbumModel
import com.challenge.model.entity.MediaFileType
import com.challenge.model.entity.MediaFiles
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    getGalleryUseCase: GetGalleryUseCase
) : ViewModel() {

    var galleryUiState: MutableStateFlow<GalleryUiState> = MutableStateFlow(GalleryUiState.Loading)
    var orientationUiState: MutableStateFlow<GalleryOrientationUiState> =
        MutableStateFlow(GalleryOrientationUiState.Grid)

//    var galleryUiState: MutableStateFlow<GalleryUiState> =
//        getGalleryUseCase.invoke().map<List<AlbumModel>, GalleryUiState>(GalleryUiState::Success)
//            .onStart { emit(GalleryUiState.Loading) }
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(5_000),
//                initialValue = GalleryUiState.Loading,
//            ) as MutableStateFlow<GalleryUiState>

    init {
        viewModelScope.launch { galleryUiState.emit(GalleryUiState.Success(getGalleryUseCase.invoke().value)) }
    }

    fun updateOrientationState(galleryOrientationUiState: GalleryOrientationUiState) {
        viewModelScope.launch {
            orientationUiState.emit(galleryOrientationUiState)
        }
    }

    suspend fun updateGalleryThumbnails(albumModels: List<AlbumModel>): List<AlbumModel> {
        for (albumModel in albumModels) {

            if (albumModel.mediaFiles.isNotEmpty()) {
                var mediaFile: MediaFiles = albumModel.mediaFiles[0]

                albumModel.thumbnail = when (mediaFile.type) {
                    is MediaFileType.Image -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        ThumbnailUtils.createImageThumbnail(
                            File(mediaFile.uri), Size(200, 200), CancellationSignal()
                        )
                    } else {
                        TODO("VERSION.SDK_INT < Q")
                    }

                    is MediaFileType.Video -> ThumbnailUtils.createVideoThumbnail(
                        File(mediaFile.uri).absolutePath, MediaStore.Video.Thumbnails.MINI_KIND
                    )
                }
                galleryUiState.emit(GalleryUiState.Success(albumModels))
            }

        }

        return albumModels
    }

}


sealed interface GalleryUiState {

    object Loading : GalleryUiState
    data class Error(val msg: String) : GalleryUiState

    data class Success(val albums: List<AlbumModel>) : GalleryUiState

}

sealed interface GalleryOrientationUiState {
    object Grid : GalleryOrientationUiState
    object List : GalleryOrientationUiState

}