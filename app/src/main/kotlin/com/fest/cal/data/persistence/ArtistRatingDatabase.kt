package com.fest.cal.data.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fest.cal.data.ArtistListConverter

@Database(entities = [ArtistRating::class], version = 2)
@TypeConverters(ArtistListConverter::class)
abstract class ArtistRatingDatabase : RoomDatabase() {
    abstract fun artistDao(): ArtistDao
}
