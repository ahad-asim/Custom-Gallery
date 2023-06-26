@file:OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)

package com.challenge.gallery.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.challenge.design.theme.Purple80
import com.challenge.gallery.R
import com.challenge.gallery.viewmodel.GalleryUiState
import com.challenge.gallery.viewmodel.GalleryViewModel
import com.challenge.model.entity.AlbumModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale


@Composable
fun GalleryRoute(
    onAlbumClick: (AlbumModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    RequiredPermission(
        onAlbumClick = onAlbumClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(
    viewModel: GalleryViewModel = hiltViewModel(), onAlbumClick: (AlbumModel) -> Unit
) {
    val galleyUiState by viewModel.galleryUiState.collectAsState()
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
                GalleryListView(
                    viewModel = viewModel,
                    galleyUiState = galleyUiState,
                    onAlbumClick = onAlbumClick
                )
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

@OptIn(ExperimentalGlideComposeApi::class)
@Preview(showBackground = true)
@Composable
fun GalleryListView(
    @PreviewParameter(GalleryUiStateProvider::class) galleyUiState: GalleryUiState,
    viewModel: GalleryViewModel,
    onAlbumClick: (AlbumModel) -> Unit = {}
) {

    when (galleyUiState) {
        is GalleryUiState.Loading -> {
            CircularProgressIndicator(
                modifier = Modifier.size(100.dp),
                color = Purple80,
                strokeWidth = 10.dp
            )
        }

        is GalleryUiState.Success -> {

            if (galleyUiState.albums.isNotEmpty()) {
                LaunchedEffect(true) {
                    viewModel.updateGalleryThumbnails(galleyUiState.albums)
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2), content = {
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


                                    album.thumbnail?.let { bitmap ->
                                        val requestBuilder =
                                            Glide.with(LocalView.current).asDrawable().load(bitmap)
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
                                        val albumModel =
                                            if (album.name == Environment.DIRECTORY_PICTURES) {
                                                "Camera"
                                            } else {
                                                album.name
                                            }
                                        Text(text = albumModel)
                                        Spacer(modifier = Modifier.weight(1f))

                                        Text(
                                            modifier = Modifier.padding(end = 5.dp),
                                            text = album.mediaFiles.size.toString()
                                        )
                                    }
                                }
                            }
                        }
                    }, contentPadding = PaddingValues(
                        start = 12.dp, top = 16.dp, end = 12.dp, bottom = 16.dp
                    )
                )
            } else {
                EmptyView()
            }
        }

        is GalleryUiState.Error -> {
            EmptyView()
        }

    }

}

@Composable
fun RequiredPermission(
    onAlbumClick: (AlbumModel) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val state = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    )
    DisposableEffect(key1 = lifecycleOwner, effect = {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                state.launchMultiplePermissionRequest()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    })

    state.permissions.forEach { permissionState ->

        when (permissionState.permission) {

            Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                when {
                    permissionState.status.isGranted -> {
                        GalleryScreen(
                            onAlbumClick = onAlbumClick
                        )
                    }

                    else -> {
                        LaunchedEffect(Unit) {
                            permissionState.launchPermissionRequest()
                        }

                        Scaffold(modifier = Modifier, topBar = {
                            TopAppBar(title = { Text("My Gallery") })
                        }, content = { paddingValues ->
                            Column(
                                modifier = Modifier.padding(paddingValues)
                            ) {
                                Box(
                                    Modifier.fillMaxSize()
                                ) {
                                    Column(
                                        Modifier.padding(
                                            vertical = 120.dp, horizontal = 16.dp
                                        )
                                    ) {
                                        Spacer(Modifier.height(8.dp))
                                        Text(
                                            "Camera permission required", style = TextStyle(
                                                fontWeight = FontWeight.ExtraBold,
                                                color = Color.Red,
                                                fontSize = 18.sp
                                            )
                                        )
                                        Spacer(Modifier.height(10.dp))
                                        Text(
                                            "This is required for the app in order  to access media",
                                            style = TextStyle(
                                                fontWeight = FontWeight.Normal,
                                                color = Color.Red,
                                                fontSize = 14.sp
                                            )
                                        )
                                    }
                                    val context = LocalContext.current
                                    Button(modifier = Modifier
                                        .align(BottomCenter)
                                        .fillMaxWidth()
                                        .padding(16.dp), onClick = {
                                        val intent =
                                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                                data = Uri.fromParts(
                                                    "package", context.packageName, null
                                                )
                                            }
                                        startActivity(context, intent, null)
                                    }) {
                                        Text("Go to settings")
                                    }
                                }
                            }
                        })
                    }
                }
            }

        }

    }
}

class GalleryUiStateProvider : PreviewParameterProvider<GalleryUiState> {
    override val values: Sequence<GalleryUiState> = sequenceOf(
        GalleryUiState.Success(
            mutableListOf(
                AlbumModel(name = "Album 1", thumbnail = null),
                AlbumModel(name = "Album 2", thumbnail = null),
                AlbumModel(name = "Album 3", thumbnail = null)
            )
        ), GalleryUiState.Loading, GalleryUiState.Error("")
    )
}


