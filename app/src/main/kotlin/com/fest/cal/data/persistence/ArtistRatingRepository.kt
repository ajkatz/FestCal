package com.fest.cal.data.persistence

class ArtistRatingRepository(private val artistDao: ArtistDao) {
    suspend fun insertArtistRating(artistRating: ArtistRating) {
        artistDao.insertArtistRating(artistRating)
    }

    suspend fun getRatingsByArtistName(artistName: String): List<ArtistRating> {
        return artistDao.getRatingsByArtistName(artistName)
    }
}