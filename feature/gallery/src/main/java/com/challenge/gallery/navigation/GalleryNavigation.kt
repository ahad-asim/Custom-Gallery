package com.challenge.gallery.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.challenge.gallery.ui.GalleryRoute

const val galleryRoute = "gallery_route"

fun NavController.navigateToGallery() {
    this.navigate(galleryRoute)
}

fun NavGraphBuilder.galleryScreen(
    onAlbumClick: (String) -> Unit,
) {
    composable(route = galleryRoute) {
        GalleryRoute(onAlbumClick = onAlbumClick)
    }
}
