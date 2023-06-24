package com.challenge.album.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.challenge.design.theme.CustomGalleryTheme
import com.challenge.design.theme.Purple40
import com.challenge.design.theme.Purple80

@Composable
fun AlbumScreen() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
    ) {
        GreetingPreview()
    }
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