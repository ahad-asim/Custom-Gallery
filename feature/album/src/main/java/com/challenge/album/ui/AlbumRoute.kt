package com.challenge.album.ui

import android.media.ThumbnailUtils
import android.os.Build
import android.os.CancellationSignal
import android.provider.MediaStore
import android.util.Size
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.challenge.model.entity.AlbumModel
import com.challenge.model.entity.MediaFileType
import java.io.File

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumRoute(
    navController: NavHostController,
    viewModel: AlbumViewModel = hiltViewModel()
) {
    viewModel.updateAlbumState(navController = navController)
    val albumUiState by viewModel.albumUiState.collectAsState()

    AlbumScreen(albumUiState = albumUiState, onBackClick = {
        navController.navigateUp()
    })


}


@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun AlbumScreen(
    @PreviewParameter(AlbumUiStateProvider::class) albumUiState: AlbumUiState,
    onBackClick: () -> Unit = {},
) {
    when (albumUiState) {
        is AlbumUiState.Loading -> {}
        is AlbumUiState.Error -> {}
        is AlbumUiState.Success -> {
            val albumModel = albumUiState?.albumModel

            albumModel?.let {
                Scaffold(modifier = Modifier, topBar = {
                    TopAppBar(title = { Text(it.name) },
                        navigationIcon =
                        {
                            IconButton(onClick = {
                                onBackClick.invoke()
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    )
                }, content = { paddingValues ->
                    Column(
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        if (albumModel?.mediaFiles?.isNotEmpty() == true) {
                            LazyVerticalGrid(columns = GridCells.Fixed(3),
                                contentPadding = PaddingValues(
                                    start = 12.dp, top = 16.dp, end = 12.dp, bottom = 16.dp
                                ),
                                content = {
                                    items(it.mediaFiles.size) { index ->

                                        val mediaFile = it.mediaFiles[index]

                                        Card(
                                            modifier = Modifier
                                                .padding(4.dp)
                                                .height(110.dp)
                                                .fillMaxWidth(),
                                            shape = RoundedCornerShape(5.dp)
                                        ) {

                                            Box(
                                                modifier = Modifier.fillMaxSize()
                                            ) {
                                                val thumbnail = when (mediaFile.type) {
                                                    is MediaFileType.Image -> ThumbnailUtils.createImageThumbnail(
                                                        File(mediaFile.uri),
                                                        Size(200, 200),
                                                        CancellationSignal()
                                                    )

                                                    is MediaFileType.Video -> ThumbnailUtils.createVideoThumbnail(
                                                        File(mediaFile.uri).absolutePath,
                                                        MediaStore.Video.Thumbnails.MICRO_KIND
                                                    )
                                                }

                                                thumbnail?.let {
                                                    Image(
                                                        modifier = Modifier.fillMaxSize(),
                                                        bitmap = it.asImageBitmap(),
                                                        contentDescription = "Photo",
                                                        contentScale = ContentScale.FillBounds
                                                    )
                                                }
                                            }
                                        }
                                    }
                                })
                        } else {
                            //EmptyView()
                        }
                    }
                })
            }


        }
    }


}

class AlbumUiStateProvider : PreviewParameterProvider<AlbumUiState> {
    override val values: Sequence<AlbumUiState> = sequenceOf(
        AlbumUiState.Success(
            AlbumModel(name = "Album 1", thumbnail = null)
        ), AlbumUiState.Loading, AlbumUiState.Error("")
    )
}