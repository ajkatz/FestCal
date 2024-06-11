package com.fest.cal.activity.compose

import android.graphics.Typeface
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fest.cal.activity.festlvalview.FestivalViewViewModel
import com.fest.cal.data.Artist

@Composable
fun ArtistList(
    viewModel: FestivalViewViewModel,
    userRatings: Map<String, Int>,
    artistsToShow: Set<Artist>,
    rows: Int = 3,
) {
    val itemList = artistsToShow.toList()
    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(rows),
            modifier = Modifier.fillMaxSize()
        ) {
            items(itemList) { item ->
                ArtistView(viewModel, item, userRatings)
            }
        }
    }
}

@Composable
fun ArtistList(
    viewModel: FestivalViewViewModel,
    userRatings: Map<String, Int>,
    artistsToShow: Map<String, Set<Artist>>,
    rows: Int = 3,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(artistsToShow.keys.toList()) { key ->
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = key,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Typeface.DEFAULT_BOLD)
                )
                Spacer(modifier = Modifier.height(8.dp))
                CustomGrid(
                    items = artistsToShow[key].orEmpty(),
                    rows = rows,
                    itemContent = { item ->
                        ArtistView(viewModel, item, userRatings)
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}


@Composable
fun CustomGrid(
    items: Set<Artist>,
    rows: Int,
    itemContent: @Composable (Artist) -> Unit
) {
    val itemList = items.toList()
    val columns = (items.size + rows - 1) / rows
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        for (column in 0 until columns) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (row in 0 until rows) {
                    val index = column * rows + row
                    if (index < items.size) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            itemContent(itemList[index])
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}