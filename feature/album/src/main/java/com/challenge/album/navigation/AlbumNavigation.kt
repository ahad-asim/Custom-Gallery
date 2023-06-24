package com.challenge.album.navigation

import android.os.Build
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.challenge.album.ui.AlbumRoute
import com.challenge.model.entity.AlbumModel

internal const val albumArg = "album"
const val albumRoute = "album_route"

fun NavController.navigateToAlbum(album: AlbumModel) {
    this.currentBackStackEntry?.savedStateHandle?.apply {
        set(albumArg, album)
    }
    this.navigate(albumRoute) {
        launchSingleTop = true
    }

}

fun NavGraphBuilder.albumScreen(navController: NavHostController) {
    composable(
        route = albumRoute
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            AlbumRoute(navController = navController)
        }
    }
}
