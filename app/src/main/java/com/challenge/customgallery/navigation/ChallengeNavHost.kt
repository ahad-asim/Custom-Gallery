package com.challenge.customgallery.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.challenge.album.navigation.albumScreen
import com.challenge.album.navigation.navigateToAlbum
import com.challenge.gallery.navigation.galleryRoute
import com.challenge.gallery.navigation.galleryScreen

@Composable
fun ChallengeNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = galleryRoute
) {

    NavHost(navController = navController, startDestination = startDestination) {
        galleryScreen(
            onAlbumClick = navController::navigateToAlbum
        )

        albumScreen()
    }

}
