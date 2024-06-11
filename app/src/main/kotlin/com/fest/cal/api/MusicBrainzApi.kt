package com.fest.cal.api

import retrofit2.http.GET
import retrofit2.http.Query

interface MusicBrainzApi {
    @GET("artist")
    suspend fun getArtist(@Query("query") artistName: String, @Query("fmt") format: String = "json"): ArtistResponse?
}

data class ArtistResponse(
    val artists: List<Artist>
)

data class Artist(
    val id: String,
    val name: String,
    val country: String?,
    val type: String?,
    val disambiguation: String?,
    val aliases: List<Alias>?,
    val beginArea: Area?,
    val endArea: Area?,
    val beginDate: String?,
    val endDate: String?,
    val gender: String?,
    val genres: List<Genre>?,
    val lifeSpan: LifeSpan?,
    val tags: List<Tag>?
)

data class Alias(
    val name: String,
    val sortName: String
)

data class Area(
    val id: String,
    val name: String,
    val sortName: String
)

data class Genre(
    val id: String,
    val name: String
)

data class LifeSpan(
    val begin: String?,
    val end: String?,
    val ended: Boolean?
)

data class Tag(
    val count: Int,
    val name: String
)