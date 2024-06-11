package com.fest.cal.data.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ArtistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtistRating(artistRating: ArtistRating)

    @Query("SELECT * FROM artist_ratings WHERE artistName = :artistName")
    suspend fun getRatingsByArtistName(artistName: String): List<ArtistRating>
}