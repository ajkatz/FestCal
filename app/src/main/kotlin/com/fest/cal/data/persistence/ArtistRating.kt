package com.fest.cal.data.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artist_ratings")
data class ArtistRating(
    @PrimaryKey val artistName: String,
    val rating: Int
)