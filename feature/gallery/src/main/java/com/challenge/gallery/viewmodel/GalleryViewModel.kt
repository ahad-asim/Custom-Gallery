package com.challenge.gallery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.domain.usecases.GetGalleryUseCase
import com.challenge.model.entity.AlbumModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    val getGalleryUseCase: GetGalleryUseCase
) : ViewModel() {

    val galleryUiState: StateFlow<GalleryUiState> =
        getGalleryUseCase.invoke().map<List<AlbumModel>, GalleryUiState>(GalleryUiState::Success)
            .onStart { emit(GalleryUiState.Loading) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = GalleryUiState.Loading,
            )

}


sealed interface GalleryUiState {

    object Loading : GalleryUiState
    data class Error(val msg: String) : GalleryUiState

    data class Success(val albums: List<AlbumModel>) : GalleryUiState

}