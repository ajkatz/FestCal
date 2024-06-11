package com.fest.cal.data

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalTime

data class Performance(
    val artist: Artist,
    val performanceDate: LocalDate? = null,
    val performanceStartTime: LocalTime? = null,
    val durationMinutes: Int? = null,
    val stage: String? = null,
    val headlineTier: Int = 0,
): Serializable

fun List<Performance>.generateStageArtistMap(): Map<String, Set<Artist>> {
    return this.groupBy { it.stage ?: "TBA" }
        .mapValues { entry -> entry.value.map { it.artist }.toSet() }
}