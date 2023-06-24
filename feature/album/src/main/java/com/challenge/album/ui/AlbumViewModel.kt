package com.challenge.album.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.challenge.album.navigation.albumArg
import com.challenge.model.entity.AlbumModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor() : ViewModel() {

    var albumUiState: MutableStateFlow<AlbumUiState> =
        MutableStateFlow(AlbumUiState.Loading)

    fun updateAlbumState(navController: NavController) {
        viewModelScope.launch {
            val album = navController.previousBackStackEntry?.savedStateHandle?.get<AlbumModel>(
                albumArg
            )
            album?.let {
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