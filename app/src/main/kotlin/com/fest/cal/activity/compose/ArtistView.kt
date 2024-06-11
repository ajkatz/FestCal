package com.fest.cal.activity.compose

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fest.cal.activity.festlvalview.FestivalViewViewModel
import com.fest.cal.data.Artist

@Composable
fun ArtistView(viewModel: FestivalViewViewModel, item: Artist, userRatings: Map<String, Int>) {
    val ratingColor = getRatingColor(userRatings.getOrDefault(item.name, 11))
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .border(6.dp, ratingColor, RoundedCornerShape(8.dp))
            .clickable { viewModel.selectArtist(item) }
    ) {
        Text(
            text = item.name,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        )
    }
}