package com.challenge.album.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.challenge.album.navigation.albumArg
import com.challenge.design.theme.CustomGalleryTheme
import com.challenge.design.theme.Purple40
import com.challenge.design.theme.Purple80
import com.challenge.model.entity.AlbumModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumScreen(
    navController: NavHostController
) {
    val albumArguments =
        navController.previousBackStackEntry?.savedStateHandle?.get<AlbumModel>(
            albumArg
        )

    Scaffold(modifier = Modifier, topBar = {
        TopAppBar(title = { Text(albumArguments?.name ?: "Album") },

            navigationIcon = if (navController.previousBackStackEntry != null) {
                {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            } else {
                {}
            })

    }, content = { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            Text(
                text = "Hello ${albumArguments?.name}!"
            )
        }
    })
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!", modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview(onAlbumClick: ((String) -> Unit)? = null) {
    CustomGalleryTheme {
        Column {
            Greeting("ALBUM")
            Text(text = "  ALBUM  ",
                textAlign = TextAlign.Center, modifier = Modifier
                    .padding(end = 15.dp, start = 15.dp, bottom = 30.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Purple80,
                                Purple40,
                            )
                        ), shape = RoundedCornerShape(10.dp)
                    )
                    .padding(vertical = 10.dp)
                    .clickable {
                        onAlbumClick?.invoke("Album")
                    }
            )
        }

    }
}