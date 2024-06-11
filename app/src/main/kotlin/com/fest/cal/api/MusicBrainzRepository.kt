package com.fest.cal.api

class MusicBrainzRepository {
    private val api = ApiClient.instance

    suspend fun getArtist(artistName: String): ArtistResponse? {
        return try {
            api.getArtist(artistName)
        } catch (e: Exception) {
            null
        }
    }
}