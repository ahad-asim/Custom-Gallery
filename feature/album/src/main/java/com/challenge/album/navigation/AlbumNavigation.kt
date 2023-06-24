package com.challenge.album.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.challenge.album.ui.AlbumScreen

internal const val albumIdArg = "albumId"
//const val albumRoute = "album_route/{$albumIdArg}"

//internal class AlbumArgs(val albumId: String) {
//    constructor(savedStateHandle: SavedStateHandle, stringDecoder: StringDecoder) :
//            this(stringDecoder.decodeString(checkNotNull(savedStateHandle[topicIdArg])))
//}

fun NavController.navigateToAlbum(album: String) {
    this.navigate("album_route/$album") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.albumScreen() {
    composable(
        route = "album_route/{$albumIdArg}", arguments = listOf(
            navArgument(albumIdArg) { type = NavType.StringType },
        )
    ) {
        AlbumScreen()
    }
}
