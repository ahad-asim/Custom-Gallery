package com.challenge.customgallery.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import com.challenge.customgallery.navigation.ChallengeNavHost
import com.challenge.customgallery.viewmodel.MainActivityViewModel
import com.challenge.design.theme.CustomGalleryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CustomGalleryTheme {
                Scaffold(modifier = Modifier, content = { paddingValues ->
                    Column(
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        ChallengeNavHost()
                    }
                })
            }
        }
    }
}