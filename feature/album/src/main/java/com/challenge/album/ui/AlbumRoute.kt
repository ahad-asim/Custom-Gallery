package com.challenge.album.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.challenge.album.R
import com.challenge.design.theme.Purple80
import com.challenge.model.entity.AlbumModel

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun AlbumRoute(
    navController: NavHostController, viewModel: AlbumViewModel = hiltViewModel()
) {

    LaunchedEffect(true) {
        viewModel.updateAlbumState(navController = navController)
    }
    val albumUiState by viewModel.albumUiState.collectAsState()
    AlbumScreen(viewModel = viewModel, albumUiState = albumUiState, onBackClick = {
        navController.navigateUp()
    })

}


@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Preview(showBackground = true)
@Composable
fun AlbumScreen(
    @PreviewParameter(AlbumUiStateProvider::class) albumUiState: AlbumUiState,
    viewModel: AlbumViewModel,
    onBackClick: () -> Unit = {},
) {
    val orientationUiState by viewModel.orientationUiState.collectAsState()
    when (albumUiState) {
        is AlbumUiState.Loading -> {
            CircularProgressIndicator(
                modifier = Modifier.size(100.dp), color = Purple80, strokeWidth = 10.dp
            )
        }

        is AlbumUiState.Error -> {}
        is AlbumUiState.Success -> {
            val albumModel = albumUiState?.albumModel

            albumModel?.let {
                Scaffold(modifier = Modifier, topBar = {
                    TopAppBar(title = { Text(it.name) }, navigationIcon = {
                        IconButton(onClick = {
                            onBackClick.invoke()
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },

                        actions = {
                            when (orientationUiState) {
                                is AlbumOrientationUiState.List -> {
                                    IconButton(onClick = {
                                        viewModel.updateOrientationState(AlbumOrientationUiState.Grid)
                                    }) {
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_grid),
                                            contentDescription = null,
                                        )
                                    }
                                }

                                is AlbumOrientationUiState.Grid -> {
                                    IconButton(onClick = {
                                        viewModel.updateOrientationState(AlbumOrientationUiState.List)
                                    }) {
                                        Icon(
                                            imageVector = Icons.Outlined.List,
                                            contentDescription = "List"
                                        )
                                    }
                                }

                            }
                        })
                }, content = { paddingValues ->
                    Column(
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        if (albumModel?.mediaFiles?.isNotEmpty() == true) {


                            when (orientationUiState) {
                                is AlbumOrientationUiState.List -> {
                                    LazyColumn(
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

                                                        mediaFile.thumbnail?.let { bitmap ->

                                                            val requestBuilder =
                                                                Glide.with(LocalView.current)
                                                                    .asDrawable()
                                                                    .placeholder(R.drawable.ic_album_image)
                                                                    .load(bitmap)

                                                            GlideImage(
                                                                model = "",
                                                                contentDescription = "thumbnail",
                                                                contentScale = ContentScale.FillBounds,
                                                                modifier = Modifier.fillMaxSize(),
                                                            ) {
                                                                it.thumbnail(
                                                                    requestBuilder
                                                                )
                                                            }

                                                        } ?: Image(
                                                            modifier = Modifier
                                                                .fillMaxSize()
                                                                .padding(25.dp),
                                                            painter = painterResource(id = R.drawable.ic_album_image),
                                                            colorFilter = ColorFilter.tint(Color.Gray),
                                                            contentDescription = null,
                                                        )
                                                    }
                                                }
                                            }
                                        }, contentPadding = PaddingValues(
                                            start = 12.dp, top = 16.dp, end = 12.dp, bottom = 16.dp
                                        )
                                    )
                                }

                                is AlbumOrientationUiState.Grid -> {
                                    LazyVerticalGrid(
                                        columns = GridCells.Fixed(3), content = {
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

                                                        mediaFile.thumbnail?.let { bitmap ->

                                                            val requestBuilder =
                                                                Glide.with(LocalView.current)
                                                                    .asDrawable()
                                                                    .placeholder(R.drawable.ic_album_image)
                                                                    .load(bitmap)

                                                            GlideImage(
                                                                model = "",
                                                                contentDescription = "thumbnail",
                                                                contentScale = ContentScale.FillBounds,
                                                                modifier = Modifier.fillMaxSize(),
                                                            ) {
                                                                it.thumbnail(
                                                                    requestBuilder
                                                                )
                                                            }

                                                        } ?: Image(
                                                            modifier = Modifier
                                                                .fillMaxSize()
                                                                .padding(25.dp),
                                                            painter = painterResource(id = R.drawable.ic_album_image),
                                                            colorFilter = ColorFilter.tint(Color.Gray),
                                                            contentDescription = null,
                                                        )
                                                    }
                                                }
                                            }
                                        }, contentPadding = PaddingValues(
                                            start = 12.dp, top = 16.dp, end = 12.dp, bottom = 16.dp
                                        )
                                    )
                                }
                            }

                        } else {
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