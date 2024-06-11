package com.fest.cal.activity.compose

import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun SpotifyView(containerId: Int) {
    AndroidView(factory = { context ->
        FrameLayout(context).apply {
            id = containerId
        }
    })
}