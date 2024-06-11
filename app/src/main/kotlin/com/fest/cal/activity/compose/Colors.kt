package com.fest.cal.activity.compose

import androidx.compose.ui.graphics.Color
import com.akatz.colors.multiGradient

val RatingNotSelected = Color(0xFF868686)

val ratingColrs = multiGradient(
    listOf(
        Color.Red,
        Color.Yellow,
        Color.Green
    ),
    4
)

fun getRatingColor(rating: Int): Color {
    if (rating >= 11) return RatingNotSelected
    return ratingColrs[rating]
}