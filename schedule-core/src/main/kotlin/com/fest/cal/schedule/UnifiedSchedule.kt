package com.fest.cal.schedule

import java.time.LocalDate
import java.time.LocalTime

/**
 * A merged view over many festival lineups — the "integrate all schedules into
 * one really useful view" that current festival apps lack.
 */
class UnifiedSchedule(val sets: List<SetSlot>) {

    /** All sets grouped by calendar day, each day ordered by start (untimed last) then artist. */
    fun byDay(): Map<LocalDate, List<SetSlot>> =
        sets.groupBy { it.day }
            .toSortedMap()
            .mapValues { (_, daySets) -> daySets.sortedWith(SLOT_ORDER) }

    /**
     * Distinct artists across every festival, deduped on the normalized name key
     * (prefers an entry that carries a `spotifyId`). Sorted by name.
     */
    fun artists(): List<ScheduleArtist> =
        sets.map { it.artist }
            .groupBy { it.key }
            .map { (_, group) -> group.firstOrNull { it.spotifyId != null } ?: group.first() }
            .sortedBy { it.name.lowercase() }

    /** Festival ids an artist appears at — cross-festival discovery, name-key matched. */
    fun festivalsFor(artistName: String): Set<String> {
        val k = ScheduleArtist(artistName).key
        return sets.filter { it.artist.key == k }.map { it.festivalId }.toSet()
    }

    /** Every set for one artist across all festivals, ordered. */
    fun setsFor(artistName: String): List<SetSlot> {
        val k = ScheduleArtist(artistName).key
        return sets.filter { it.artist.key == k }.sortedWith(SLOT_ORDER)
    }

    companion object {
        fun unify(lineups: List<FestivalLineup>): UnifiedSchedule =
            UnifiedSchedule(lineups.flatMap { it.sets })

        private val SLOT_ORDER: Comparator<SetSlot> =
            compareBy<SetSlot, LocalTime?>(nullsLast()) { it.start }
                .thenBy { it.artist.name.lowercase() }
    }
}
