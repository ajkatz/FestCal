package com.fest.cal

import android.app.Application
import androidx.room.Room
import com.fest.cal.data.persistence.ArtistRatingDatabase
import com.fest.cal.data.persistence.ArtistRatingRepository

class FestCalApplication: Application() {
    lateinit var artistRatingDatabase: ArtistRatingDatabase
    lateinit var artistRatingRepository: ArtistRatingRepository

    override fun onCreate() {
        super.onCreate()
        artistRatingDatabase = Room.databaseBuilder(
            applicationContext,
            ArtistRatingDatabase::class.java,
            "artist_ratings.db"
        ).fallbackToDestructiveMigration()
            .build()
        artistRatingRepository = ArtistRatingRepository(artistRatingDatabase.artistDao())
    }
}