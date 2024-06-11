package com.fest.cal.activity.festlvalview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fest.cal.api.MusicBrainzRepository
import com.fest.cal.data.Artist
import com.fest.cal.data.Festival
import com.fest.cal.data.Performance
import com.fest.cal.data.persistence.ArtistRating
import com.fest.cal.data.persistence.ArtistRatingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

class FestivalViewViewModel(
    festival: Festival,
    private val artistRatingRepository: ArtistRatingRepository
): ViewModel() {
    private val _festival = MutableStateFlow(festival)
    val festival: StateFlow<Festival> = _festival

    private val _selectedArtist = MutableStateFlow<Artist?>(null)
    val selectedArtist: StateFlow<Artist?> = _selectedArtist

    private val _selectedArtistContent = MutableStateFlow<String?>(null)
    val selectedArtistContent: StateFlow<String?> = _selectedArtistContent

    private val _userRatings = MutableStateFlow<Map<String, Int>>(emptyMap())
    val userRatings: StateFlow<Map<String, Int>> = _userRatings

    private val _relevantPerformances = MutableStateFlow(festival.artistList)
    val relevantPerformances: StateFlow<List<Performance>> = _relevantPerformances

    init {
        viewModelScope.launch {
            val persistedRatings = mutableMapOf<String, Int>()
            festival.artistList.forEach {
                val rating = artistRatingRepository.getRatingsByArtistName(it.artist.name)
                persistedRatings[it.artist.name] = rating.firstOrNull()?.rating ?: 11
            }
            _userRatings.update { persistedRatings }
        }
    }

    fun selectArtist(item: Artist?) {
        _selectedArtist.update { item }
        item?.let { artist ->
            viewModelScope.launch {
                val response = MusicBrainzRepository().getArtist(artist.name)
                response?.let { artistResponse ->
                    artistResponse.artists.firstOrNull()?.let { foundArtist ->
                        val stringBuilder = StringBuilder().apply {
                            addItemToBuilder("From: ", foundArtist.country)
                            addItemToBuilder("Gender: ", foundArtist.gender)
                            addItemToBuilder("Type: ", foundArtist.type)
                            addListToBuilder("AKA: ", foundArtist.aliases) { it.name }
                            addListToBuilder("Tags: ", foundArtist.tags) { it.name }
                        }
                        _selectedArtistContent.update {
                            stringBuilder.toString()
                        }
                    }
                }
            }
        }
    }

    fun setRelevantPerformances(performances: List<Performance>) {
        _relevantPerformances.update { performances }
    }

    private fun StringBuilder.addItemToBuilder(label: String, value: String?) {
        if (!value.isNullOrEmpty()) {
            append(label)
            append(value.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() })
            append("\n\n")
        }
    }

    private fun <T> StringBuilder.addListToBuilder(
        label: String,
        list: List<T>?,
        func: (T) -> String,
    ) {
        if (!list.isNullOrEmpty()) {
            append(label)
            list.forEachIndexed { index, value ->
                if (index != 0) {
                    append(", ")
                }
                append(func(value).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() })
            }
            append("\n\n")
        }
    }

    fun rateArtist(selectedArtist: Artist, rating: Int) {
        val ratings = _userRatings.value
        val newRatings = ratings.toMutableMap()
        newRatings[selectedArtist.name] = rating
        _userRatings.update { newRatings }
        viewModelScope.launch {
            artistRatingRepository.insertArtistRating(ArtistRating(
                artistName = selectedArtist.name,
                rating = rating
            ))
        }
    }
}

class FestivalViewViewModelFactory(
    private val festival: Festival,
    private val artistRatingRepository: ArtistRatingRepository
): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FestivalViewViewModel::class.java)) {
            @Suppress("unchecked_cast")
            return FestivalViewViewModel(festival, artistRatingRepository) as T
        }
        throw IllegalArgumentException("Unknown FestivalViewViewModel class")
    }
}