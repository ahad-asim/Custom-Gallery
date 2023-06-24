package com.challenge.gallery.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.challenge.design.theme.Purple80
import com.challenge.gallery.R
import com.challenge.gallery.viewmodel.GalleryUiState
import com.challenge.gallery.viewmodel.GalleryViewModel
import com.challenge.model.entity.AlbumModel
import java.util.Calendar


@Composable
fun GalleryRoute(
    onAlbumClick: (AlbumModel) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GalleryViewModel = hiltViewModel(),
) {
    val galleyUiState by viewModel.galleryUiState.collectAsState()
    GalleryScreen(galleyUiState = galleyUiState, onAlbumClick = onAlbumClick)
}

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun GalleryScreen(galleyUiState: GalleryUiState, onAlbumClick: (AlbumModel) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
    ) {
        Scaffold(modifier = Modifier, topBar = {
            TopAppBar(title = { Text("My Gallery") })
        }, content = { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues)
            ) {
                GalleryListView(galleyUiState = galleyUiState, onAlbumClick = onAlbumClick)
            }
        })
    }
}

@Composable
fun EmptyView() {
    Box(
        modifier = Modifier
            .background(color = Purple80)
            .fillMaxWidth()
            .padding(20.dp),
    ) {}
}

@Preview(showBackground = true)
@Composable
fun GalleryListView(
    @PreviewParameter(GalleryUiStateProvider::class) galleyUiState: GalleryUiState,
    onAlbumClick: (AlbumModel) -> Unit = {}
) {

    when (galleyUiState) {
        is GalleryUiState.Loading -> {
            EmptyView()
        }

        is GalleryUiState.Success -> {

            if (galleyUiState.albums.isNotEmpty()) {
                LazyVerticalGrid(columns = GridCells.Fixed(2), contentPadding = PaddingValues(
                    start = 12.dp, top = 16.dp, end = 12.dp, bottom = 16.dp
                ), content = {
                    items(galleyUiState.albums.size) { index ->

                        val album = galleyUiState.albums[index]

                        Card(
                            modifier = Modifier
                                .padding(4.dp)
                                .height(170.dp)
                                .fillMaxWidth()
                                .clickable {
                                    onAlbumClick.invoke(album)
                                },
                        ) {

                            Box(
                                modifier = Modifier.fillMaxSize()
                            ) {

                                album.thumbnail?.let {
                                    Image(
                                        modifier = Modifier.fillMaxSize(),
                                        bitmap = it.asImageBitmap(),
                                        contentDescription = "Photo",
                                        contentScale = ContentScale.FillBounds
                                    )
                                } ?: Image(
                                    modifier = Modifier.fillMaxSize(),
                                    painter = painterResource(id = R.drawable.ic_album_image),
                                    contentDescription = null,
                                )
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .align(BottomCenter)
                                        .background(color = Purple80),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        modifier = Modifier
                                            .wrapContentSize()
                                            .padding(4.dp),
                                        painter = painterResource(id = R.drawable.ic_album_image),
                                        contentDescription = null,
                                    )
                                    Text(text = album.name)
                                    Spacer(modifier = Modifier.weight(1f))

                                    Text(
                                        modifier = Modifier.padding(end = 5.dp),
                                        text = album.images.size.toString()
                                    )
                                }
                            }
                        }
                    }
                })
            } else {
                EmptyView()
            }
        }

        is GalleryUiState.Error -> {
            EmptyView()
        }

    }

}

class GalleryUiStateProvider : PreviewParameterProvider<GalleryUiState> {
    override val values: Sequence<GalleryUiState> = sequenceOf(
        GalleryUiState.Success(
            listOf(
                AlbumModel(name = "Album 1", thumbnail = null),
                AlbumModel(name = "Album 2", thumbnail = null),
                AlbumModel(name = "Album 3", thumbnail = null)
            )
        ), GalleryUiState.Loading, GalleryUiState.Error("")
    )
}