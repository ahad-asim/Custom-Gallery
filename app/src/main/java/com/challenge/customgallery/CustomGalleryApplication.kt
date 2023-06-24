package com.challenge.customgallery

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CustomGalleryApplication : Application(){

//    @Inject
//    lateinit var imageLoader: Provider<ImageLoader>

    override fun onCreate() {
        super.onCreate()
        // Initialize Sync; the system responsible for keeping data in the app up to date.
//        Sync.initialize(context = this)
    }

//    override fun newImageLoader(): ImageLoader = imageLoader.get()
}