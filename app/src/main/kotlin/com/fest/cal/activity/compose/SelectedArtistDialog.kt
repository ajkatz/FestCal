package com.fest.cal.activity.compose

import android.graphics.Typeface
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.fest.cal.activity.festlvalview.FestivalViewViewModel
import com.fest.cal.data.Artist
import com.fest.cal.data.Performance

@Composable
fun SelectedArtistDialog(
    viewModel: FestivalViewViewModel,
    selectedArtist: Artist,
    userRatings: Map<String, Int>,
    currentArtistSelection: List<Performance>,
) {
    Dialog(
        onDismissRequest = { viewModel.selectArtist(null) },
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = Color.White.copy(alpha = 0.9f)
        ) {
            Column {
                ActivityHeader(text = selectedArtist.name, size = 30)
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = viewModel.selectedArtistContent.collectAsState().value ?: "",
                    fontSize = 16.sp
                )
                if (selectedArtist.collaborativeArtists.isNotEmpty()) {
                    ArtistList(
                        viewModel = viewModel,
                        userRatings = userRatings,
                        artistsToShow = selectedArtist.collaborativeArtists.toSet(),
                        rows = 2)
                }
            }
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                val artistsInList = currentArtistSelection.map { it.artist }.toSet().toMutableList()
                var artistIndex = artistsInList.indexOf(selectedArtist)
                if (artistIndex == -1) {
                    artistsInList.add(0, selectedArtist)
                    artistIndex = 0
                }
                Button(
                    colors = ButtonColors(Color.Transparent, Color.Transparent, Color.Transparent, Color.Transparent),
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .background(Color.Transparent),
                    onClick = { viewModel.selectArtist(artistsInList[(artistIndex + artistsInList.size - 1) % artistsInList.size]) },
                ) {
                    Text(
                        text = "<",
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(Typeface.DEFAULT_BOLD),
                        fontSize = 30.sp,
                        modifier = Modifier.padding(8.dp),
                        color = Color.Gray
                    )
                }
                Button(
                    colors = ButtonColors(Color.Transparent, Color.Transparent, Color.Transparent, Color.Transparent),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .background(Color.Transparent),
                    onClick = { viewModel.selectArtist(artistsInList[(artistIndex + 1) % artistsInList.size]) },
                ) {
                    Text(
                        text = ">",
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(Typeface.DEFAULT_BOLD),
                        fontSize = 30.sp,
                        modifier = Modifier.padding(8.dp),
                        color = Color.Gray
                    )
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                ) {
                    UserRatingWidget(userRatings, selectedArtist, viewModel)
                }
            }
        }
    }
}

@Composable
private fun UserRatingWidget(
    userRatings: Map<String, Int>,
    selectedArtist: Artist,
    viewModel: FestivalViewViewModel
) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = "How much do you want to see this artist?",
        textAlign = TextAlign.Center
    )
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        items(IntRange(0, 11).toList()) { rating ->
            val isSelected = userRatings.getOrDefault(selectedArtist.name, 11) == rating
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp)
                    .background(if (isSelected) Color.Gray else Color.White)
                    .border(
                        3.dp,
                        getRatingColor(rating),
                        RoundedCornerShape(3.dp)
                    )
                    .clickable { viewModel.rateArtist(selectedArtist, rating) }
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    textAlign = TextAlign.Center,
                    text = if (rating == 11) "?" else rating.toString(),
                    color = Color.Black
                )
            }
        }
    }
}